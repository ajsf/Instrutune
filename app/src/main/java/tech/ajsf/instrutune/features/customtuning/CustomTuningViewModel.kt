package tech.ajsf.instrutune.features.customtuning

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tech.ajsf.instrutune.common.data.InstrumentRepository
import tech.ajsf.instrutune.common.model.InstrumentCategory
import tech.ajsf.instrutune.common.tuner.notefinder.model.ChromaticOctave

data class CustomTuningViewState(
    val notes: List<String> = listOf(),
    val tuningName: String = "",
    val id: Int? = null,
    val checkOnboarding: Boolean = true
)

data class BuilderAction(
    val building: Boolean = false,
    val tuningName: String = ""
)

class CustomTuningViewModel(
    private val instrumentRepository: InstrumentRepository
) : ViewModel() {

    val buildingLiveData: LiveData<BuilderAction>
        get() = _building

    val viewStateLiveData: LiveData<CustomTuningViewState>
        get() = _viewState

    private val _viewState = MutableLiveData<CustomTuningViewState>()

    private val _building = MutableLiveData<BuilderAction>()

    private val tunings = instrumentRepository
        .getAllTunings()
        .blockingGet()
        .groupBy { it.category }

    init {
        _viewState.postValue(CustomTuningViewState())
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

    fun saveTuningAndGetId(): Int {
        val (notes, tuningName, id) = getViewState()
        return instrumentRepository.saveTuning(tuningName, notes, id)
            .blockingGet()
    }

    fun deleteTuning() = getViewState().id?.let { instrumentRepository.deleteTuning(it) }

    fun getCategories(): List<String> = instrumentRepository.getCategories()
        .blockingGet()
        .filter { it != InstrumentCategory.Custom.toString() }

    fun getTuningsForCategory(category: String) =
        tunings[InstrumentCategory.valueOf(category)] ?: listOf()

    fun startBuilder(name: String = "") {
        _building.postValue(BuilderAction(true, name))
    }

    fun startBuilder(id: Int) {
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

    fun onboardingChecked() = _viewState.postValue(getViewState().copy(checkOnboarding = false))

    private fun updateNotes(newNotes: List<String>) {
        val viewState = getViewState()
        val newViewState = viewState.copy(notes = newNotes)
        _viewState.postValue(newViewState)
    }

    private fun getNotes() = getViewState().notes.toMutableList()

    private fun getViewState() = _viewState.value!!
}

fun <T> MutableList<T>.moveItem(oldIndex: Int, newIndex: Int) {
    val item = get(oldIndex)
    removeAt(oldIndex)
    add(newIndex, item)
}