package tech.ajsf.instrutune.tuner.frequencydetection

import tech.ajsf.instrutune.common.tuner.frequencydetection.FrequencyDetector
import tech.ajsf.instrutune.common.tuner.frequencydetection.FrequencyDetectorImpl
import tech.ajsf.instrutune.test.data.TestDataFactory
import tech.ajsf.instrutune.common.tuner.frequencydetection.detector.DetectionEngine
import tech.ajsf.instrutune.common.tuner.frequencydetection.model.DetectionResult
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.rxkotlin.toFlowable
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
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
        detector = FrequencyDetectorImpl(mockDetectionEngine)
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
    fun `when the detectionEngine sends three sounds, it sends -1, a sound, and -1`() {
        val results = createRandomResults(3)
        whenever(mockDetectionEngine.listen())
            .thenReturn(results.toFlowable())

        val testSubscriber = detector.listen().test()

        testSubscriber.assertValueCount(3)
        testSubscriber.assertValueAt(0, -1f)
        assertNotEquals(testSubscriber.values()[1], -1f)
        testSubscriber.assertValueAt(2, -1f)
    }

    @Test
    fun `when the detectionEngine sends ten sounds, it sends -1, a sound, and -1`() {
        val results = createRandomResults(10)
        whenever(mockDetectionEngine.listen())
            .thenReturn(results.toFlowable())

        val testSubscriber = detector.listen().test()

        testSubscriber.assertValueAt(0, -1f)
        assertNotEquals(testSubscriber.values()[1], -1f)
        testSubscriber.assertValueAt(2, -1f)
    }

    @Test
    fun `when the detectionEngine sends silence, it only sends the first -1f`() {
        val silence = listOf(
            DetectionResult(
                TestDataFactory.randomFloat(),
                true,
                true,
                TestDataFactory.randomFloat(),
                50f
            )
        )

        whenever(mockDetectionEngine.listen())
            .thenReturn(silence.toFlowable())

        val testSubscriber = detector.listen().test()
        testSubscriber.assertValueCount(1)
    }
}