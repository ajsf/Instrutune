package tech.ajsf.instrutune.common.tuner

import com.nhaarman.mockitokotlin2.*
import io.reactivex.rxkotlin.toFlowable
import io.reactivex.schedulers.TestScheduler
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import tech.ajsf.instrutune.common.model.Instrument
import tech.ajsf.instrutune.common.tuner.frequencydetection.FrequencyDetector
import tech.ajsf.instrutune.common.tuner.notefinder.NoteFinder
import tech.ajsf.instrutune.test.data.InstrumentDataFactory
import tech.ajsf.instrutune.test.data.TestDataFactory

internal class TunerTest {

    @Mock
    lateinit var mockDetector: FrequencyDetector

    @Mock
    private lateinit var mockMapper: DetectionToNoteMapper

    private lateinit var tuner: Tuner

    private lateinit var freqList: List<Float>
    private lateinit var infoList: List<NoteInfo>

    private lateinit var instrument: Instrument

    private var offset: Int = 0

    private val scheduler = TestScheduler()

    private fun randomFloatList(): List<Float> =
        (0..TestDataFactory.randomInt(10))
            .map { TestDataFactory.randomFloat() }

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)
        instrument = InstrumentDataFactory.randomInstrument()
        freqList = randomFloatList()
        tuner = Tuner(mockDetector, mockMapper, scheduler)
    }

    private fun stubResponse(freqs: List<Float> = freqList) {
        whenever(mockDetector.listen())
            .thenReturn(freqs.toFlowable())

        infoList = InstrumentDataFactory.randomNoteInfoList(freqs.size)

        freqs.forEachIndexed { index, freq ->
            whenever(mockMapper.map(eq(freq), any())).thenReturn(infoList[index])
        }
    }

    @Test
    fun `there are no interactions with the detector when the Tuner is first created`() {
        stubResponse()
        verify(mockDetector, never()).listen()
    }

    @Test
    fun `calling getTunerFlow calls listen on the detector`() {
        stubResponse()
        tuner.getTunerFlow()
        verify(mockDetector).listen()
    }

    @Test
    fun `getTunerFlow sends the same number of items it receives from the detector when subscribed to`() {
        stubResponse()
        val testSubscriber = tuner.getTunerFlow().test()
        scheduler.triggerActions()
        testSubscriber.assertValueCount(freqList.size)
    }

    @Test
    fun `getTunerFlow calls map on the mapper for each frequency sent by the detector when subscribed to`() {
        stubResponse()
        tuner.getTunerFlow().test()
        scheduler.triggerActions()
        freqList.forEach {
            verify(mockMapper).map(eq(it), any())
        }
    }

    @Test
    fun `it returns the result from the mapper for each frequency sent by the detector`() {
        stubResponse()
        val testSubscriber = tuner.getTunerFlow().test()
        scheduler.triggerActions()
        testSubscriber.assertValueSequence(infoList)
    }

    @Test
    fun `if not configured, it calls the mapper with results from the chromatic note finder`() {
        val freqs = instrument.notes.map { it.freq / 1000f }

        stubResponse(freqs)

        tuner.getTunerFlow().test()
        scheduler.triggerActions()

        val finder = NoteFinder.chromaticNoteFinder(offset)
        val expectedNotes = freqs.map { finder.findNote(it) }

        freqs.forEachIndexed { index, freq ->
            verify(mockMapper).map(freq, expectedNotes[index])
        }
    }

    @Test
    fun `when configured it calls the mapper with results from the instrumentNoteFinder`() {
        val freqs = instrument.notes.map { it.freq / 1000f }

        stubResponse(freqs)

        tuner.configTuner(instrument, offset)
        tuner.getTunerFlow().test()
        scheduler.triggerActions()

        val finder = NoteFinder.instrumentNoteFinder(instrument, offset)
        val expectedNotes = freqs.map { finder.findNote(it) }

        freqs.forEachIndexed { index, freq ->
            verify(mockMapper).map(freq, expectedNotes[index])
        }
    }

    @Test
    fun `when configured and then set to chromatic mode, it uses the chromatic note finder`() {
        val freqs = instrument.notes.map { it.freq / 1000f }

        stubResponse(freqs)

        tuner.configTuner(instrument, offset)
        tuner.mode = ChromaticMode
        tuner.getTunerFlow().test()
        scheduler.triggerActions()

        val finder = NoteFinder.chromaticNoteFinder(offset)
        val expectedNotes = freqs.map { finder.findNote(it) }

        freqs.forEachIndexed { index, freq ->
            verify(mockMapper).map(freq, expectedNotes[index])
        }
    }

    @Test
    fun `when configured, set to chromatic mode, and back to instrument mode, it uses the instrument note finder`() {
        val freqs = instrument.notes.map { it.freq / 1000f }

        stubResponse(freqs)

        tuner.configTuner(instrument, offset)
        tuner.mode = ChromaticMode
        tuner.mode = InstrumentMode
        tuner.getTunerFlow().test()
        scheduler.triggerActions()

        val finder = NoteFinder.instrumentNoteFinder(instrument, offset)
        val expectedNotes = freqs.map { finder.findNote(it) }

        freqs.forEachIndexed { index, freq ->
            verify(mockMapper).map(freq, expectedNotes[index])
        }
    }
}