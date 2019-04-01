package co.ajsf.tuner.tuner

import co.ajsf.tuner.common.tuner.SelectedStringInfo
import co.ajsf.tuner.common.tuner.Tuner
import co.ajsf.tuner.test.data.InstrumentDataFactory
import co.ajsf.tuner.test.data.TestDataFactory
import co.ajsf.tuner.common.tuner.frequencydetection.FrequencyDetector
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.rxkotlin.toFlowable
import io.reactivex.schedulers.TestScheduler
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

internal class TunerTest {

    @Mock
    lateinit var mockDetector: FrequencyDetector

    private lateinit var tuner: Tuner
    private lateinit var floatList: List<Float>

    private val scheduler = TestScheduler()

    private fun randomFloatList(): List<Float> =
        (0..TestDataFactory.randomInt(10))
            .map { TestDataFactory.randomFloat() }

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    private fun stubRandomResponse() {
        floatList = randomFloatList()
        whenever(mockDetector.listen())
            .thenReturn(floatList.toFlowable())
        tuner = Tuner(mockDetector, scheduler)
    }

    @Test
    fun `listen is called on the detector when it is created`() {
        stubRandomResponse()
        verify(mockDetector).listen()
    }

    @Test
    fun `mostRecentFrequency sends no items if an offset hasn't been sent`() {
        stubRandomResponse()
        val testSubscriber = tuner.mostRecentFrequency.test()
        scheduler.triggerActions()
        testSubscriber.assertValueCount(0)
    }

    @Test
    fun `mostRecentFrequency sends the same number of items as it receives once an offset has been set`() {
        stubRandomResponse()
        tuner.setOffset(0)
        val testSubscriber = tuner.mostRecentFrequency.test()
        scheduler.triggerActions()
        testSubscriber.assertValueCount(floatList.size)
    }

    @Test
    fun `mostRecentNoteName sends no items if no offset has been sent`() {
        stubRandomResponse()
        val testSubscriber = tuner.mostRecentNoteInfo.test()
        scheduler.triggerActions()
        testSubscriber.assertValueCount(0)
    }

    @Test
    fun `mostRecentNoteName sends the same number of items as it receives once an offset has been set`() {
        stubRandomResponse()
        tuner.setOffset(0)
        val testSubscriber = tuner.mostRecentNoteInfo.test()
        scheduler.triggerActions()
        testSubscriber.assertValueCount(floatList.size)
    }

    @Test
    fun `instrumentTuning sends nothing if no instrument has been set`() {
        stubRandomResponse()
        val testSubscriber = tuner.instrumentTuning.test()
        scheduler.triggerActions()
        testSubscriber.assertValueCount(0)
    }

    @Test
    fun `instrumentTuning sends the same number as it receives once an instrument has been set`() {
        stubRandomResponse()
        tuner.setInstrument(InstrumentDataFactory.randomInstrument())
        val testSubscriber = tuner.instrumentTuning.test()
        scheduler.triggerActions()
        testSubscriber.assertValueCount(floatList.size)
    }

    @Test
    fun `it returns each numberedName with a delta of 0 when the freq for each string of an instrument is sent`() {

        val instrument = InstrumentDataFactory.randomInstrument()

        whenever(mockDetector.listen())
            .thenReturn(instrument.notes.map { it.freq / 1000f }.toFlowable())

        tuner = Tuner(mockDetector, scheduler)
        tuner.setInstrument(instrument)

        val testSubscriber = tuner.instrumentTuning.test()
        scheduler.triggerActions()

        val expectedResults = instrument.notes
            .map { SelectedStringInfo(it.numberedName, 0f) }

        testSubscriber.assertValueSequence(expectedResults)
    }
}