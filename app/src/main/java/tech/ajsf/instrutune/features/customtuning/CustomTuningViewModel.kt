package tech.ajsf.instrutune.features.customtuning

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import tech.ajsf.instrutune.common.data.InstrumentRepository
import tech.ajsf.instrutune.common.model.InstrumentCategory
import tech.ajsf.instrutune.common.tuner.notefinder.model.ChromaticOctave
import tech.ajsf.instrutune.common.view.InstrumentDialogHelper
import tech.ajsf.instrutune.common.viewmodel.BaseViewModel

data class CustomTuningViewState(
    val notes: List<String> = listOf(),
    val tuningName: String = "",
    val id: Int? = null,
    val requestOnboarding: Boolean
)

data class BuilderAction(
    val building: Boolean = false,
    val tuningName: String = ""
)

class CustomTuningViewModel(
    override val instrumentRepository: InstrumentRepository,
    override val dialogHelper: InstrumentDialogHelper,
    override val uiScheduler: Scheduler = AndroidSchedulers.mainThread()
) : BaseViewModel() {

    val buildingLiveData: LiveData<BuilderAction>
        get() = _building

    val viewStateLiveData: LiveData<CustomTuningViewState>
        get() = _viewState

    private val _viewState = MutableLiveData<CustomTuningViewState>()

    private val _building = MutableLiveData<BuilderAction>()

    init {
        disposable.add(
            getTunings(InstrumentCategory.Custom.toString())
                .subscribeBy {
                    _viewState.postValue(CustomTuningViewState(requestOnboarding = it.isEmpty()))
                }
        )
        _building.postValue(BuilderAction())
    }

    fun addNote(noteName: String) {
        val newNotes = getNotes() + noteName
        updateNotes(newNotes)
    }

    fun updateNote(noteIndex: Int, noteName: String) {
        updateNotes(getNotes().also { it[noteIndex] = noteName })
    }

    fun moveNote(oldIndex: Int, newIndex: Int) {
        val newNotes = getNotes().also { it.moveItem(oldIndex, newIndex) }
        updateNotes(newNotes)
    }

    fun removeNote(noteIndex: Int) = updateNotes(
        getNotes().also { it.removeAt(noteIndex) }
    )

    fun updateTitle(title: String) {
        _viewState.postValue(getViewState().copy(tuningName = title))
    }

    fun getNoteRange(): List<String> = ChromaticOctave.noteNames()

    fun enableEdit(): Boolean =
        instrumentRepository.getCategories().blockingGet().contains(
            InstrumentCategory.Custom.toString()
        )

    fun saveTuningAndExecuteAction(action: (Int) -> Unit) {
        val (notes, tuningName, id) = getViewState()
        disposable.add(
            instrumentRepository
                .saveTuning(tuningName, notes, id)
                .subscribeBy {
                    action(it)
                })
    }

    fun deleteTuning() = getViewState().id?.let {
        instrumentRepository.deleteTuning(it).subscribe()
    }

    fun startBuilder(name: String = "") {
        _building.postValue(BuilderAction(true, name))
    }

    private fun startBuilder(id: Int) {
        instrumentRepository
            .getTuningById(id)
            .map { instrument ->
                with(instrument) {
                    val notes = notes.map { it.numberedName }
                    val tuningId = if (category == InstrumentCategory.Custom) id else null
                    val newViewState = getViewState().copy(notes = notes, id = tuningId)
                    _viewState.postValue(newViewState)
                    val name =
                        if (category == InstrumentCategory.Custom) tuningName else "$category - $tuningName"
                    startBuilder(name)
                }
            }.subscribe()
    }

    fun onboardingChecked() = _viewState.postValue(getViewState().copy(requestOnboarding = false))

    private fun updateNotes(newNotes: List<String>) {
        val viewState = getViewState()
        val newViewState = viewState.copy(notes = newNotes)
        _viewState.postValue(newViewState)
    }

    private fun getNotes() = getViewState().notes.toMutableList()

    private fun getViewState() = _viewState.value!!

    override fun onTuningsSelected(id: Int) {
        startBuilder(id)
    }
}

fun <T> MutableList<T>.moveItem(oldIndex: Int, newIndex: Int) {
    val item = get(oldIndex)
    removeAt(oldIndex)
    add(newIndex, item)
}