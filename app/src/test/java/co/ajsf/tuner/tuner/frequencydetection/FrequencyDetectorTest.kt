package co.ajsf.tuner.tuner.frequencydetection

import co.ajsf.tuner.test.data.TestDataFactory
import co.ajsf.tuner.tuner.frequencydetection.detector.DetectionEngine
import co.ajsf.tuner.tuner.frequencydetection.model.DetectionResult
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.rxkotlin.toFlowable
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

internal class FrequencyDetectorTest {

    @Mock
    lateinit var mockDetectionEngine: DetectionEngine

    private lateinit var detector: FrequencyDetector

    private fun createPitchedNoise() =
        DetectionResult(
            TestDataFactory.randomFloat(),
            false,
            true,
            TestDataFactory.randomFloat(),
            50f
        )

    private fun createRandomResults(length: Int = TestDataFactory.randomInt(10)) = (1..length)
        .map { createPitchedNoise() }

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)
        detector = FrequencyDetector(mockDetectionEngine)
    }

    @Test
    fun `calling listen calls listen on the DetectionEngine`() {
        whenever(mockDetectionEngine.listen())
            .thenReturn(createRandomResults().toFlowable())

        detector.listen()

        verify(mockDetectionEngine).listen()
    }

    @Test
    fun `it sends -1f when first subscribed to`() {
        whenever(mockDetectionEngine.listen())
            .thenReturn(emptyList<DetectionResult>().toFlowable())
        val testSubscriber = detector.listen().test()

        testSubscriber.assertValue(-1f)
    }

    @Test
    fun `calling listen multiple times calls listen on the detection engine the same amount of time`() {
        whenever(mockDetectionEngine.listen())
            .thenReturn(createRandomResults().toFlowable())

        val times = TestDataFactory.randomInt(10)
        repeat(times) { detector.listen() }

        verify(mockDetectionEngine, times(times)).listen()
    }

    @Test
    fun `multiple subscribers all receive the same values`() {
        whenever(mockDetectionEngine.listen())
            .thenReturn(createRandomResults().toFlowable())

        val times = TestDataFactory.randomInt(10)
        val testSubscribers = (0..times).map { detector.listen().test() }

        (1..testSubscribers.lastIndex).onEach {
            assertEquals(testSubscribers.first().values(), testSubscribers[it].values())
        }
    }

    @Test
    fun `when the detectionEngine sends three sounds, three sounds are sent`() {
        val results = createRandomResults(3)
        whenever(mockDetectionEngine.listen())
            .thenReturn(results.toFlowable())

        val testSubscriber = detector.listen().test()

        testSubscriber.assertValueCount(3)
    }

    @Test
    fun `when the detectionEngine sends ten sounds, three sounds are sent`() {
        val results = createRandomResults(10)
        whenever(mockDetectionEngine.listen())
            .thenReturn(results.toFlowable())

        val testSubscriber = detector.listen().test()

        testSubscriber.assertValueCount(3)
    }
}