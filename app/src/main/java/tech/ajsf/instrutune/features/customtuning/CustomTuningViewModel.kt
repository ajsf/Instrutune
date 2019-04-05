package tech.ajsf.instrutune.features.customtuning

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tech.ajsf.instrutune.common.tuner.notefinder.model.ChromaticOctave

data class CustomTuningViewState(
    val notes: List<String> = listOf(),
    val tuningName: String = ""
)

class CustomTuningViewModel : ViewModel() {

    val viewStateLiveDate: LiveData<CustomTuningViewState>
        get() = _viewState

    private val _viewState = MutableLiveData<CustomTuningViewState>()

    init {
        _viewState.postValue(CustomTuningViewState())
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