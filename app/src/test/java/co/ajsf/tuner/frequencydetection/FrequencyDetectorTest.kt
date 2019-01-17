package co.ajsf.tuner.frequencydetection

import co.ajsf.tuner.model.DetectionResult
import co.ajsf.tuner.test.data.TestDataFactory
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Flowable
import io.reactivex.rxkotlin.toFlowable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

internal class FrequencyDetectorTest {

    @Mock
    lateinit var mockDetectionEngine: DetectionEngine

    private lateinit var detector: FrequencyDetector

    private fun createPitchedNoise() = DetectionResult(TestDataFactory.randomFloat(), false, true, 1f, 50f)

    private fun createSilence() = DetectionResult(TestDataFactory.randomFloat(), true, true, 1f, 50f)

    private fun createRandomResults() = (0..TestDataFactory.randomInt(10)).map {
        if (TestDataFactory.randomBoolean()) createPitchedNoise()
        else createSilence()
    }

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)
        detector = FrequencyDetector(mockDetectionEngine)
    }

    @Test
    fun `calling listen calls listen on the DetectionEngine`() {
        whenever(mockDetectionEngine.listen()).thenReturn(createRandomResults().toFlowable())
        detector.listen()
        verify(mockDetectionEngine).listen()
    }

    @Test
    fun `calling listen multiple times calls listen on the detection engine the same amount of time`() {
        whenever(mockDetectionEngine.listen()).thenReturn(createRandomResults().toFlowable())
        val times = TestDataFactory.randomInt(10)
        repeat(times) { detector.listen() }
        verify(mockDetectionEngine, times(times)).listen()
    }

    @Test
    fun `when the detectionEngine sends pitched, non-silent result, it sends the frequency`() {
        val pitchedNoise = createPitchedNoise()
        whenever(mockDetectionEngine.listen()).thenReturn(Flowable.just(pitchedNoise))
        val testSubscriber = detector.listen().test()
        testSubscriber.assertValue(pitchedNoise.pitch)
    }

    @Test
    fun `when the detectionEngine sends silence, it sends -1`() {
        whenever(mockDetectionEngine.listen()).thenReturn(Flowable.just(createSilence()))
        val testSubscriber = detector.listen().test()
        testSubscriber.assertValue(-1f)
    }

    @Test
    fun `when the detection engine sends silence multiple times, it only sends -1 once`() {
        whenever(mockDetectionEngine.listen())
            .thenReturn(listOf(createSilence(), createSilence(), createSilence(), createSilence()).toFlowable())
        val testSubscriber = detector.listen().test()
        testSubscriber.assertValue(-1f)
        testSubscriber.assertValueSequence(listOf(-1f))
    }

    @Test
    fun `when the detection engine sends silence, followed by pitched noise, followed by silence twice, it calls the frequencyListener three times`() {
        val results = listOf(createSilence(), createPitchedNoise(), createSilence(), createSilence())
        whenever(mockDetectionEngine.listen())
            .thenReturn(results.toFlowable())
        val testSubscriber = detector.listen().test()
        testSubscriber.assertValueSequence(listOf(-1f, results[1].pitch, -1f))
    }
}