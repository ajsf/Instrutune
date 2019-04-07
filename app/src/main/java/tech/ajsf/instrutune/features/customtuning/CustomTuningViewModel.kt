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
    val originalName: String? = null
)

class CustomTuningViewModel(private val instrumentRepository: InstrumentRepository) : ViewModel() {

    val buildingLiveData: LiveData<Boolean>
        get() = _building

    val viewStateLiveData: LiveData<CustomTuningViewState>
        get() = _viewState

    private val _viewState = MutableLiveData<CustomTuningViewState>()

    private val _building = MutableLiveData<Boolean>()

    private val tunings = instrumentRepository.getAllTunings()

    init {
        _viewState.postValue(CustomTuningViewState())
        _building.postValue(false)
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

    fun saveTuningAndGetName(): String {
        val viewState = getViewState()
        instrumentRepository.saveTuning(viewState.tuningName, viewState.notes)
        return viewState.tuningName
    }

    fun getCategories(): List<String> = instrumentRepository.getInstrumentList()
        .filter { it != InstrumentCategory.Custom.toString() }

    fun getTuningsForCategory(category: String) =
        tunings[InstrumentCategory.valueOf(category)]?.map { it.tuningName } ?: listOf()

    fun startBuilder() = _building.postValue(true)

    fun startBuilder(name: String, category: String) {
        val tuningName = if (category == InstrumentCategory.Custom.toString()) name else "$category - $name"
        tunings[InstrumentCategory.valueOf(category)]
            ?.find { it.tuningName == name }
            ?.let { instrument ->
                val notes = instrument.notes.map { it.numberedName }
                val newViewState = getViewState().copy(
                    notes = notes,
                    originalName = tuningName
                )
                _viewState.postValue(newViewState)
            }
        startBuilder()
    }

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