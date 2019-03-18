package co.ajsf.tuner.features.tuner.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import co.ajsf.tuner.data.InstrumentRepository
import co.ajsf.tuner.features.tuner.TunerViewModel
import co.ajsf.tuner.model.toInstrumentInfo
import co.ajsf.tuner.test.data.InstrumentDataFactory
import co.ajsf.tuner.test.data.TestDataFactory
import co.ajsf.tuner.tuner.SelectedNoteInfo
import co.ajsf.tuner.tuner.SelectedStringInfo
import co.ajsf.tuner.tuner.Tuner
import com.nhaarman.mockitokotlin2.reset
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.rxkotlin.toFlowable
import io.reactivex.schedulers.Schedulers
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class TunerViewModelTest {

    @Mock
    private lateinit var mockTuner: Tuner

    @Mock
    private lateinit var mockRepository: InstrumentRepository

    private lateinit var viewModel: TunerViewModel

    private lateinit var stringInfo: List<SelectedStringInfo>
    private lateinit var noteInfo: List<SelectedNoteInfo>
    private lateinit var freqs: List<String>

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        stringInfo = InstrumentDataFactory.randomStringInfoList()
        noteInfo = InstrumentDataFactory.randomNoteInfoList()
        freqs = InstrumentDataFactory.randomFreqList()
        whenever(mockTuner.instrumentTuning).thenReturn(stringInfo.toFlowable())
        whenever(mockTuner.mostRecentNoteInfo).thenReturn(noteInfo.toFlowable())
        whenever(mockTuner.mostRecentFrequency).thenReturn(freqs.toFlowable())
    }

    @Test
    fun `it calls getOffset on the repository when created`() {
        viewModel = buildViewModel()
        verify(mockRepository).getOffset()
    }

    @Test
    fun `it calls setOffset on the tuner with a default of 0 when created`() {
        viewModel = buildViewModel()
        verify(mockTuner).setOffset(0)
    }

    @Test
    fun `if the repository returns a different offset, it calls setOffset on the tuner with that number when created`() {
        val offset = TestDataFactory.randomInt(20)
        whenever(mockRepository.getOffset()).thenReturn(offset)
        viewModel = buildViewModel()
        verify(mockTuner).setOffset(offset)
    }

    @Test
    fun `it calls getSelectedTuning on the repository when created`() {
        viewModel = buildViewModel()
        verify(mockRepository).getSelectedTuning()
    }

    @Test
    fun `it calls set instrument on the tuner with the instrument returned from getSelectedTuning`() {
        val instrument = InstrumentDataFactory.randomInstrument()
        whenever(mockRepository.getSelectedTuning()).thenReturn(instrument)
        viewModel = buildViewModel()
        verify(mockTuner).setInstrument(instrument)
    }

    @Test
    fun `it maps the instrument to SelectedInstrumentInfo and sends it to selectedInstrumentInfo LiveData, with a default middleA of 440Hz`() {
        val instrument = InstrumentDataFactory.randomInstrument()

        whenever(mockRepository.getSelectedTuning()).thenReturn(instrument)

        viewModel = buildViewModel()
        viewModel.selectedInstrumentInfo.observeForever {
            assertEquals(instrument.toInstrumentInfo(), it)
        }
    }

    @Test
    fun `when there is an offset, the instrument info is created with a middleA of 440 plus the offset`() {
        val instrument = InstrumentDataFactory.randomInstrument()
        val offset = TestDataFactory.randomInt(20)

        whenever(mockRepository.getSelectedTuning()).thenReturn(instrument)
        whenever(mockRepository.getOffset()).thenReturn(offset)

        viewModel = buildViewModel()

        viewModel.selectedInstrumentInfo.observeForever {
            assertEquals(instrument.toInstrumentInfo(440 + offset), it)
        }
    }

    @Test
    fun `when saveOffset is called, it calls the repository with the same offset`() {
        val offset = TestDataFactory.randomInt()
        viewModel = buildViewModel()
        viewModel.saveOffset(offset)
        verify(mockRepository).saveOffset(offset)
    }

    @Test
    fun `when saveOffset is called, it calls the repository for the saved offset, and sets the tuner with that offset`() {
        val offset = TestDataFactory.randomInt()

        viewModel = buildViewModel()
        reset(mockRepository)

        whenever(mockRepository.getOffset()).thenReturn(offset)

        viewModel.saveOffset(offset)

        verify(mockRepository).getOffset()
        verify(mockTuner).setOffset(offset)
    }

    @Test
    fun `when saveOffset is called, it calls getSelectedTuning on the instrument repository`() {
        viewModel = buildViewModel()
        reset(mockRepository)

        viewModel.saveOffset(TestDataFactory.randomInt(20))

        verify(mockRepository).getSelectedTuning()
    }

    @Test
    fun `when saveOffset is called, it sends instrument info with the new middle a to selectedInstrumentInfo`() {
        viewModel = buildViewModel()
        reset(mockRepository)

        val instrument = InstrumentDataFactory.randomInstrument()
        val offset = TestDataFactory.randomInt(20)

        whenever(mockRepository.getSelectedTuning()).thenReturn(instrument)
        whenever(mockRepository.getOffset()).thenReturn(offset)

        viewModel.saveOffset(offset)

        viewModel.selectedInstrumentInfo.observeForever {
            assertEquals(instrument.toInstrumentInfo(440 + offset), it)
        }
    }

    @Test
    fun `when  getOffset is called it calls getOffset on the tuner`() {
        viewModel = buildViewModel()
        reset(mockRepository)
        viewModel.getOffset()
        verify(mockRepository).getOffset()
    }

    @Test
    fun `when getInstruments is called, it calls getInstrumentList on the repository`() {
        viewModel = buildViewModel()
        viewModel.getInstruments()
        verify(mockRepository).getInstrumentList()
    }

    @Test
    fun `when getTunings is called, it calls getTunings on the repository`() {
        viewModel = buildViewModel()
        viewModel.getTunings()
        verify(mockRepository).getTunings()
    }

    @Test
    fun `when saveSelectedCategory is called it calls saveSelectedCategory on the repository`() {
        val category = TestDataFactory.randomString()
        viewModel = buildViewModel()
        viewModel.saveSelectedCategory(category)
        verify(mockRepository).saveSelectedCategory(category)
    }

    @Test
    fun `when saveSelectedTuning is called it calls saveSelectedTuning on the repository`() {
        val tuningName = TestDataFactory.randomString()
        viewModel = buildViewModel()
        viewModel.saveSelectedTuning(tuningName)
        verify(mockRepository).saveSelectedTuning(tuningName)
    }

    @Test
    fun `when saveSelectedTuning is called it calls getSelectedTuning on the repository`() {
        val tuningName = TestDataFactory.randomString()
        viewModel = buildViewModel()
        reset(mockRepository)
        viewModel.saveSelectedTuning(tuningName)
        verify(mockRepository).getSelectedTuning()
    }

    @Test
    fun `when saveSelectedTuning is called it updates the tuner with the new instrument`() {
        viewModel = buildViewModel()
        reset(mockRepository)

        val tuningName = TestDataFactory.randomString()
        val instrument = InstrumentDataFactory.randomInstrument()

        whenever(mockRepository.getSelectedTuning()).thenReturn(instrument)

        viewModel.saveSelectedTuning(tuningName)

        verify(mockTuner).setInstrument(instrument)
    }

    @Test
    fun `when saveSelectedTuning is called, it sends the new instrument info to selectedInstrumentInfo`() {
        viewModel = buildViewModel()
        reset(mockRepository)

        val instrument = InstrumentDataFactory.randomInstrument()
        val tuningName = TestDataFactory.randomString()

        whenever(mockRepository.getSelectedTuning()).thenReturn(instrument)

        viewModel.saveSelectedTuning(tuningName)

        viewModel.selectedInstrumentInfo.observeForever {
            assertEquals(instrument.toInstrumentInfo(), it)
        }
    }

    @Test
    fun `selectedStringInfo converts the tuner's instrumentTuning Flowable to LiveData`() {
        val info = InstrumentDataFactory.randomStringInfoList()
        whenever(mockTuner.instrumentTuning).thenReturn(info.toFlowable())

        viewModel = buildViewModel()
        var count = 0

        viewModel.selectedStringInfo.observeForever {
            assertEquals(info[count], it)
            count++
        }
        assertEquals(info.size, count)
    }

    @Test
    fun `mostRecentFrequency converts the tuner's mostRecentFrequency Flowable to LiveData`() {
        viewModel = buildViewModel()
        var count = 0

        viewModel.mostRecentFrequency.observeForever {
            assertEquals(freqs[count], it)
            count++
        }
        assertEquals(freqs.size, count)
    }

    @Test
    fun `mostRecentNoteName sends the name from the tuner's mostRecentNoteInfo as LiveData`() {
        val info = InstrumentDataFactory.randomNoteInfoList()
        whenever(mockTuner.mostRecentNoteInfo).thenReturn(info.toFlowable())

        viewModel = buildViewModel()
        var count = 0

        viewModel.mostRecentNoteName.observeForever {
            assertEquals(info[count].name, it)
            count++
        }
        assertEquals(info.size, count)
    }

    @Test
    fun `mostRecentNoteDelta sends the delta from the tuner's mostRecentNoteInfo as LiveData`() {
        val info = InstrumentDataFactory.randomNoteInfoList()
        whenever(mockTuner.mostRecentNoteInfo).thenReturn(info.toFlowable())

        viewModel = buildViewModel()
        var count = 0

        viewModel.mostRecentNoteDelta.observeForever {
            assertEquals(info[count].delta, it)
            count++
        }
        assertEquals(info.size, count)
    }

    private fun buildViewModel() =
        TunerViewModel(mockTuner, mockRepository, Schedulers.trampoline())
}