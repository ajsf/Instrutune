package co.ajsf.tuner.frequencyDetection

import co.ajsf.tuner.model.DetectionResult
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

internal class FrequencyDetectorTest {

    @Mock
    lateinit var mockDetectionEngine: DetectionEngine
    @Mock
    lateinit var mockFrequencyListener: FrequencyListener

    private lateinit var detector: FrequencyDetector

    private val silence = DetectionResult(100f, true, true, 1f, 50f)
    private val pitchedNoise = DetectionResult(100f, false, true, 1f, 50f)
    private val nonPitchedNoise = DetectionResult(100f, false, false, 1f, 50f)
    private val lowProbabilityNoise = DetectionResult(100f, false, true, .1f, 50f)

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)
        detector = FrequencyDetector { mockDetectionEngine }
    }

    @Test
    fun `calling listen calls listen on the DetectionEngine`() {
        detector.listen(mockFrequencyListener)
        verify(mockDetectionEngine).listen(any())
    }

    @Test
    fun `calling stopListening calls stopListening on the DetectionEngine`() {
        detector.listen(mockFrequencyListener)
        detector.stopListening()
        verify(mockDetectionEngine).stopListening()
    }

    @Test
    fun `calling listen twice throws a CurrentlyListeningException`() {
        detector.listen(mockFrequencyListener)
        Assertions.assertThrows(CurrentlyListeningException::class.java) { detector.listen(mockFrequencyListener) }
    }

    @Test
    fun `calling stopListening throws a NotListeningException if called before listen is called`() {
        Assertions.assertThrows(NotListeningException::class.java) { detector.stopListening() }
    }

    @Test
    fun `calling listen, followed by stopListening, followed by listen works`() {
        detector.listen(mockFrequencyListener)
        detector.stopListening()
        detector.listen(mockFrequencyListener)
        verify(mockDetectionEngine, times(2)).listen(any())
        verify(mockDetectionEngine, times(1)).stopListening()
    }

    @Nested
    inner class DetectionHandlerTest {

        @BeforeEach
        fun setup() {
            detector.listen(mockFrequencyListener)
        }

        @Test
        fun `when called with a pitched non-silent result, it calls the frequencyListener with the frequency of the result`() {
            detector.detectionHandler(pitchedNoise)
            verify(mockFrequencyListener).invoke(pitchedNoise.pitch)
        }

        @Test
        fun `when called with a non-pitched non-silent result, it doesn't call the frequencyListener`() {
            detector.detectionHandler(nonPitchedNoise)
            verify(mockFrequencyListener, times(0)).invoke(any())
        }

        @Test
        fun `when called with a pitched non-silent result with low probability, it doesn't call the frequencyListener`() {
            detector.detectionHandler(lowProbabilityNoise)
            verify(mockFrequencyListener, times(0)).invoke(any())
        }

        @Test
        fun `when called with a silent result, it calls the frequencyListener with -1`() {
            detector.detectionHandler(silence)
            verify(mockFrequencyListener).invoke(-1f)
        }

        @Test
        fun `when called with a silent result multiple times, it only calls the frequencyListener once`() {
            detector.detectionHandler(silence)
            detector.detectionHandler(silence)
            detector.detectionHandler(silence)
            verify(mockFrequencyListener, times(1)).invoke(-1f)
        }

        @Test
        fun `when called with silence, followed by pitched noise, followed by silence, it calls the frequencyListener three times`() {
            detector.detectionHandler(silence)
            detector.detectionHandler(pitchedNoise)
            detector.detectionHandler(silence)
            verify(mockFrequencyListener, times(3)).invoke(any())
            verify(mockFrequencyListener, times(2)).invoke(-1f)
            verify(mockFrequencyListener, times(1)).invoke(pitchedNoise.pitch)
        }
    }
}