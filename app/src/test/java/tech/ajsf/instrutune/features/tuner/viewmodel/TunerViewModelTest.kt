package tech.ajsf.instrutune.features.tuner.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Single
import io.reactivex.rxkotlin.toFlowable
import io.reactivex.schedulers.Schedulers
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import tech.ajsf.instrutune.common.data.InstrumentRepository
import tech.ajsf.instrutune.common.model.Instrument
import tech.ajsf.instrutune.common.tuner.ChromaticMode
import tech.ajsf.instrutune.common.tuner.InstrumentMode
import tech.ajsf.instrutune.common.tuner.NoteInfo
import tech.ajsf.instrutune.common.tuner.Tuner
import tech.ajsf.instrutune.common.view.InstrumentDialogHelper
import tech.ajsf.instrutune.features.tuner.TunerViewModel
import tech.ajsf.instrutune.test.data.InstrumentDataFactory
import tech.ajsf.instrutune.test.data.TestDataFactory.randomInt
import tech.ajsf.instrutune.test.data.TestDataFactory.randomStringList

class TunerViewModelTest {

    @Mock
    private lateinit var mockTuner: Tuner

    @Mock
    private lateinit var mockRepository: InstrumentRepository

    @Mock
    private lateinit var mockDialogHelper: InstrumentDialogHelper

    private lateinit var viewModel: TunerViewModel

    private lateinit var noteInfo: List<NoteInfo>
    private lateinit var instrument: Instrument

    private var offset: Int = 0

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        noteInfo = InstrumentDataFactory.randomNoteInfoList()
        instrument = InstrumentDataFactory.randomInstrument()
        offset = randomInt()

        whenever(mockRepository.getSelectedTuning()).thenReturn(Single.just(instrument))
        whenever(mockRepository.getOffset()).thenReturn(offset)
        whenever(mockTuner.getTunerFlow()).thenReturn(noteInfo.toFlowable())
    }

    @Test
    fun `when first created, the TunerViewState mode is InstrumentMode`() {
        viewModel = buildViewModel()
        val viewState = viewModel.tunerViewState.value!!
        assertEquals(viewState.mode, InstrumentMode)
    }

    @Test
    fun `calling toggleMode changes the TunerViewState mode from InstrumentMode to ChromaticMode and back`() {
        viewModel = buildViewModel()

        viewModel.toggleMode()
        assertEquals(viewModel.tunerViewState.value!!.mode, ChromaticMode)

        viewModel.toggleMode()
        assertEquals(viewModel.tunerViewState.value!!.mode, InstrumentMode)

        viewModel.toggleMode()
        assertEquals(viewModel.tunerViewState.value!!.mode, ChromaticMode)
    }

    @Test
    fun `it calls getOffset on the repository when created`() {
        viewModel = buildViewModel()
        verify(mockRepository).getOffset()
    }

    @Test
    fun `it calls getSelectedTuning on the repository when created`() {
        viewModel = buildViewModel()
        verify(mockRepository).getSelectedTuning()
    }

    @Test
    fun `it calls getTunerFlow with the instrument and offset returned by the repository`() {
        viewModel = buildViewModel()
        verify(mockTuner).getTunerFlow()
    }

    @Test
    fun `it maps the instrument to TunerViewState and sends it as LiveData, with a middleA of 440 plus the offset`() {
        viewModel = buildViewModel()
        val expectedMiddleA = "A4=${440 + offset}Hz"

        viewModel.tunerViewState.observeForever { viewState ->
            with(viewState) {
                assertEquals("${instrument.category} (${instrument.tuningName})", tuningName)
                assertEquals(instrument.category.toString(), category)
                assertEquals(instrument.notes.map { it.numberedName }, noteNames)
                assertEquals(expectedMiddleA, middleA)
            }
        }
    }

    @Test
    fun `when saveOffset is called, it calls the repository with the same offset`() {
        offset = randomInt()
        viewModel = buildViewModel()
        viewModel.saveOffset(offset)
        verify(mockRepository).saveOffset(offset)
    }

    @Test
    fun `when saveOffset is called, it calls the repository for the saved offset, and sets the tuner with that offset`() {
        val newOffset = randomInt()

        viewModel = buildViewModel()
        reset(mockRepository)

        whenever(mockRepository.getOffset()).thenReturn(newOffset)
        whenever(mockRepository.getSelectedTuning()).thenReturn(Single.just(instrument))

        viewModel.saveOffset(newOffset)

        verify(mockRepository).getOffset()
        verify(mockTuner).getTunerFlow()
    }

    @Test
    fun `when saveOffset is called, it calls getSelectedTuning on the instrument repository`() {
        viewModel = buildViewModel()
        reset(mockRepository)
        whenever(mockRepository.getSelectedTuning()).thenReturn(Single.just(instrument))

        viewModel.saveOffset(randomInt(20))

        verify(mockRepository).getSelectedTuning()
    }

    @Test
    fun `when saveOffset is called, it updates the TunerViewState with the new middleA`() {
        viewModel = buildViewModel()
        reset(mockRepository)

        val offset = randomInt(20)

        whenever(mockRepository.getSelectedTuning()).thenReturn(Single.just(instrument))
        whenever(mockRepository.getOffset()).thenReturn(offset)

        viewModel.saveOffset(offset)

        val expectedMiddleA = "A4=${440 + offset}Hz"

        viewModel.tunerViewState.observeForever {
            assertEquals(expectedMiddleA, it.middleA)
        }
    }

    @Test
    fun `when  getOffset is called it calls getOffset on the repository`() {
        viewModel = buildViewModel()
        reset(mockRepository)
        viewModel.getOffset()
        verify(mockRepository).getOffset()
    }

    @Test
    fun `when showSelectCategory is called, it calls getCategories on the repository`() {
        viewModel = buildViewModel()
        whenever(mockRepository.getCategories()).thenReturn(Single.just(randomStringList()))
        viewModel.showSelectCategory()
        verify(mockRepository).getCategories()
    }

    @Test
    fun `when showSelectCategory is called, showSelectInstrumentDialog on the dialogHelper`() {
        viewModel = buildViewModel()
        val categories = randomStringList()
        whenever(mockRepository.getCategories()).thenReturn(Single.just(categories))

        viewModel.showSelectCategory()
        verify(mockDialogHelper).showSelectInstrumentDialog(eq(categories), any())
    }

    @Test
    fun `when showSelectTuning is called, it calls getTuningsForCategory on the repository`() {
        viewModel = buildViewModel()
        val categories = randomStringList()
        val currentCategory = instrument.category.toString()
        whenever(mockRepository.getCategories()).thenReturn(Single.just(categories))

        val instruments = InstrumentDataFactory.randomInstrumentList()
        whenever(mockRepository.getTuningsForCategory(currentCategory)).thenReturn(
            Single.just(instruments)
        )

        viewModel.showSelectTuning()
        verify(mockRepository).getTuningsForCategory(currentCategory)
    }

    @Test
    fun `when showSelectTuning is called, it calls showSelectTuningDialog on the dialogHelper`() {
        viewModel = buildViewModel()
        val categories = randomStringList()
        val currentCategory = instrument.category.toString()
        whenever(mockRepository.getCategories()).thenReturn(Single.just(categories))

        val instruments = InstrumentDataFactory.randomInstrumentList()
        whenever(mockRepository.getTuningsForCategory(currentCategory)).thenReturn(
            Single.just(instruments)
        )

        viewModel.showSelectTuning()
        verify(mockDialogHelper).showSelectTuningDialog(
            eq(instruments.map { it.tuningName }),
            any()
        )
    }

    @Test
    fun `when showSelectMiddleA is called, it calls getOffset on the repository`() {
        viewModel = buildViewModel()
        reset(mockRepository)

        viewModel.showSelectMiddleA()

        verify(mockRepository).getOffset()
    }

    @Test
    fun `when showSelectMiddleA is called, it calls showSetMiddleADialog on the dialog helper with the offset returned by the repository`() {
        viewModel = buildViewModel()
        reset(mockRepository)

        val offset = randomInt()
        whenever(mockRepository.getOffset()).thenReturn(offset)

        viewModel.showSelectMiddleA()

        verify(mockDialogHelper).showSetMiddleADialog(eq(offset), any())
    }

    @Test
    fun `when the callback sent to showSetMiddleADialog is called, saveOffset is called on the repository`() {
        viewModel = buildViewModel()
        reset(mockRepository)

        val offset = randomInt()
        whenever(mockRepository.getOffset()).thenReturn(offset)
        whenever(mockRepository.getSelectedTuning()).thenReturn(Single.just(instrument))

        val captor = argumentCaptor<(Int) -> Unit>()

        viewModel.showSelectMiddleA()

        verify(mockDialogHelper).showSetMiddleADialog(eq(offset), captor.capture())

        val newOffset = randomInt()
        captor.firstValue.invoke(newOffset)

        verify(mockRepository).saveOffset(newOffset)
    }

    @Test
    fun `when saveSelectedTuning is called, it calls saveSelectedTuning on the repository`() {
        val tuningId = randomInt()
        viewModel = buildViewModel()
        viewModel.saveSelectedTuning(tuningId)
        verify(mockRepository).saveSelectedTuning(tuningId)
    }

    @Test
    fun `when saveSelectedTuning is called it calls getSelectedTuning on the repository`() {
        val tuningId = randomInt()
        viewModel = buildViewModel()
        reset(mockRepository)
        whenever(mockRepository.getSelectedTuning()).thenReturn(Single.just(instrument))

        viewModel.saveSelectedTuning(tuningId)
        verify(mockRepository).getSelectedTuning()
    }

    @Test
    fun `when saveSelectedTuning is called, it sends the new instrument info to tunerViewState`() {
        viewModel = buildViewModel()

        val newInstrument = InstrumentDataFactory.randomInstrument()

        whenever(mockRepository.getSelectedTuning()).thenReturn(Single.just(newInstrument))
        whenever(mockRepository.getOffset()).thenReturn(offset)
        whenever(mockTuner.getTunerFlow()).thenReturn(noteInfo.toFlowable())

        val tuningId = randomInt()

        viewModel.saveSelectedTuning(tuningId)

        viewModel.tunerViewState.observeForever { viewState ->
            with(viewState) {
                assertEquals("${newInstrument.category} (${newInstrument.tuningName})", tuningName)
                assertEquals(newInstrument.category.toString(), category)
                assertEquals(newInstrument.notes.map { it.numberedName }, noteNames)
            }
        }
    }

    @Test
    fun `when noteViewState is subscribed to it sends the NoteInfo sent by the tuner as NoteViewState`() {
        viewModel = buildViewModel()

        var count = 0

        viewModel.noteViewState.observeForever {
            val expectedInfo = noteInfo[count]
            assertEquals(expectedInfo.name, it.noteName)
            assertEquals(expectedInfo.delta, it.delta)
            assertEquals("${String.format("%.2f", expectedInfo.freq)} Hz", it.freqString)
            count++
        }

        assertEquals(noteInfo.size, count)
    }

    @Test
    fun `when onActivityDestroyed is called, is calls clear on the DialogHelper`() {
        viewModel = buildViewModel()

        viewModel.onActivityDestroyed()

        verify(mockDialogHelper).clear()
    }

    private fun buildViewModel() =
        TunerViewModel(mockTuner, mockRepository, mockDialogHelper, Schedulers.trampoline())
}