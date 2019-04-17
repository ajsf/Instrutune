package tech.ajsf.instrutune.features.tuner

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.toLiveData
import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import tech.ajsf.instrutune.common.data.InstrumentRepository
import tech.ajsf.instrutune.common.model.toInstrumentInfo
import tech.ajsf.instrutune.common.tuner.SelectedStringInfo
import tech.ajsf.instrutune.common.tuner.Tuner
import tech.ajsf.instrutune.common.view.InstrumentDialogHelper

data class TunerViewState(
    val tuningName: String = "",
    val category: String = "",
    val noteNames: List<String> = listOf(),
    val middleA: String = ""
)

data class ChromaticViewState(
    val freq: String = "0 Hz",
    val noteName: String = "",
    val delta: Int = 0
)

class TunerViewModel(
    private val tuner: Tuner,
    private val instrumentRepository: InstrumentRepository,
    private val dialogHelper: InstrumentDialogHelper,
    private val uiScheduler: Scheduler = AndroidSchedulers.mainThread()
) :
    ViewModel() {

    val tunerViewState: LiveData<TunerViewState>
        get() = _tunerViewState

    val chromaticViewState: LiveData<ChromaticViewState> = tuner.mostRecentNoteInfo
        .map { ChromaticViewState(it.freq, it.name, it.delta) }
        .toUiThreadLiveData()

    val selectedNoteViewState: LiveData<SelectedStringInfo> =
        tuner.instrumentTuning.toUiThreadLiveData()

    private val _tunerViewState: MutableLiveData<TunerViewState> = MutableLiveData()

    private val disposable = CompositeDisposable()

    init {
        setupTuner()
    }

    fun onActivityDestroyed() {
        dialogHelper.clear()
    }

    override fun onCleared() {
        disposable.clear()
        super.onCleared()
    }

    fun showSelectCategory() {
        disposable.add(
            instrumentRepository
                .getCategories()
                .observeOn(uiScheduler)
                .subscribeBy(
                    onSuccess = { instruments ->
                        dialogHelper.showSelectInstrumentDialog(instruments) { showSelectTuning(it) }
                    })
        )
    }

    fun showSelectTuning() {
        showSelectTuning(getViewState().category)
    }

    fun showSelectMiddleA() {
        dialogHelper.showSetMiddleADialog(getOffset()) { saveOffset(it) }
    }

    fun saveSelectedTuning(tuningId: Int) {
        instrumentRepository.saveSelectedTuning(tuningId)
        setupTuner()
    }

    fun saveOffset(offset: Int) {
        instrumentRepository.saveOffset(offset)
        setupTuner()
    }

    fun getOffset() = instrumentRepository.getOffset()

    private fun setupTuner() {
        val offset = getOffset()
        tuner.setOffset(offset)
        disposable.addAll(instrumentRepository
            .getSelectedTuning()
            .doOnSuccess { tuner.setInstrument(it) }
            .map { it.toInstrumentInfo(getMiddleAFreq(offset)) }
            .observeOn(uiScheduler)
            .subscribeBy {
                _tunerViewState.postValue(
                    TunerViewState(
                        tuningName = it.name,
                        category = it.category,
                        noteNames = it.noteNames,
                        middleA = it.middleA
                    )
                )
            })
    }

    private fun showSelectTuning(category: String) {
        disposable.add(instrumentRepository.getTuningsForCategory(category)
            .observeOn(uiScheduler)
            .subscribeBy { tunings ->
                dialogHelper.showSelectTuningDialog(tunings.map { it.tuningName }) {
                    val id = tunings[it].id
                    if (id != null) saveSelectedTuning(id)
                }
            })
    }

    private fun getMiddleAFreq(offset: Int): Int = 440 + offset

    private fun <T> Flowable<T>.toUiThreadLiveData() = this.observeOn(uiScheduler).toLiveData()

    private fun getViewState(): TunerViewState = _tunerViewState.value ?: TunerViewState()
}