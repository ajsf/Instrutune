package tech.ajsf.instrutune.features.customtuning.view

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import tech.ajsf.instrutune.R
import tech.ajsf.instrutune.common.tuner.notefinder.model.MusicalNote
import kotlinx.android.synthetic.main.note_picker.view.*

class SelectNoteDialog(private val context: Context, private val onNoteSelected: (String) -> Unit) {

    private val title = "Select Note"
    private val inflater = LayoutInflater.from(context.applicationContext)

    fun show(numberedName: String, noteRange: List<String>) {
        val notePicker = inflater.inflate(R.layout.note_picker, null)

        var (noteName, octave) = MusicalNote.nameAndNumberFromNumberedName(numberedName)

        notePicker.note_picker.apply {
            minValue = 0
            maxValue = noteRange.lastIndex
            displayedValues = noteRange.toTypedArray()
            setOnValueChangedListener { _, _, newVal ->
                noteName = noteRange[newVal]
            }
            value = noteRange.indexOf(noteName)
        }

        notePicker.octave_picker.apply {
            minValue = 0
            maxValue = 8
            setOnValueChangedListener { _, _, newVal ->
                octave = newVal
            }
            value = octave
        }

        with(AlertDialog.Builder(context)) {
            setTitle(title)
            setView(notePicker)
            setPositiveButton("Select") { _, _ -> onNoteSelected("$noteName$octave") }
            setNegativeButton("Cancel") { _, _ -> }
            setCancelable(true)
            create()
        }.show()
    }
}