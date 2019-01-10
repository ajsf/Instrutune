package co.ajsf.tuner.mapper

import be.tarsos.dsp.AudioEvent
import be.tarsos.dsp.pitch.PitchDetectionResult
import co.ajsf.tuner.test.data.DetectionDataFactory
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

internal class TarsosResponseToModelMapperTest {

    @Mock
    lateinit var pitchDetectionResult: PitchDetectionResult

    @Mock
    lateinit var audioEvent: AudioEvent

    @Test
    fun `it maps correctly`() {
        MockitoAnnotations.initMocks(this)
        val expectedResult = DetectionDataFactory.makeDetectionResult()

        whenever(pitchDetectionResult.pitch).thenReturn(expectedResult.pitch)
        whenever(pitchDetectionResult.probability).thenReturn(expectedResult.probability)
        whenever(pitchDetectionResult.isPitched).thenReturn(expectedResult.isPitched)
        whenever(audioEvent.getdBSPL()).thenReturn(expectedResult.dBSPL.toDouble())
        whenever(audioEvent.isSilence(any())).thenReturn(expectedResult.isSilence)

        val mapped = mapTarsosResponseToDetectionResult(pitchDetectionResult to audioEvent)
        assertEquals(expectedResult, mapped)
    }
}