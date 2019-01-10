package co.ajsf.tuner.tarsos

import be.tarsos.dsp.AudioDispatcher
import be.tarsos.dsp.AudioEvent
import be.tarsos.dsp.pitch.PitchDetectionResult
import co.ajsf.tuner.frequencyDetection.DetectionHandler
import co.ajsf.tuner.mapper.TarsosResponseToModelMapper
import co.ajsf.tuner.model.DetectionResult
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

internal class DetectionEngineImplTest {

    @Mock
    lateinit var mockDispatcher: AudioDispatcher
    @Mock
    lateinit var mockDetectionHandler: DetectionHandler
    @Mock
    lateinit var mockMapper: TarsosResponseToModelMapper

    @Mock
    lateinit var pitchDetectionResult: PitchDetectionResult

    @Mock
    lateinit var audioEvent: AudioEvent

    private lateinit var detectionEngine: DetectionEngineImpl

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)
        detectionEngine = DetectionEngineImpl(mockDispatcher, mockMapper)
        detectionEngine.listen(mockDetectionHandler)
    }

    @Test
    fun `it adds an audio processor when listen is called`() {
        verify(mockDispatcher, times(1)).addAudioProcessor(any())
    }

    @Test
    fun `it removes the audio processor when stopListening is called`() {
        detectionEngine.stopListening()
        verify(mockDispatcher, times(1)).removeAudioProcessor(any())
    }

    @Test
    fun `it calls the mapper when a response is received`() {
        val tarsosResponse = pitchDetectionResult to audioEvent
        val detectionResult = DetectionResult(1f, false, true, 1f, 1f)

        whenever(mockMapper.invoke(tarsosResponse)).thenReturn(detectionResult)
        detectionEngine.handleDetection(pitchDetectionResult, audioEvent)
        verify(mockMapper, times(1)).invoke(tarsosResponse)
    }

    @Test
    fun `it invokes the detection handler  with the mapped detection result`() {
        val tarsosResponse = pitchDetectionResult to audioEvent
        val detectionResult = DetectionResult(1f, false, true, 1f, 1f)

        whenever(mockMapper(tarsosResponse)).thenReturn(detectionResult)
        detectionEngine.handleDetection(pitchDetectionResult, audioEvent)
        verify(mockDetectionHandler, times(1)).invoke(detectionResult)
    }
}