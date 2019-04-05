package tech.ajsf.instrutune.features.customtuning

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_custom_tuning.*
import org.kodein.di.Kodein
import tech.ajsf.instrutune.R
import tech.ajsf.instrutune.common.view.InjectedActivity
import tech.ajsf.instrutune.common.viewmodel.buildViewModel
import tech.ajsf.instrutune.features.customtuning.di.customTuningActivityModule
import tech.ajsf.instrutune.features.customtuning.view.ConfirmDeleteDialog
import tech.ajsf.instrutune.features.customtuning.view.SelectNoteDialog
import tech.ajsf.instrutune.features.customtuning.view.TextUpdateWatcher

const val CUSTOM_TUNING_EXTRA = "CUSTOM_TUNING_NAME"

class CustomTuningActivity : InjectedActivity() {

    override fun activityModule() = Kodein.Module("custom") {
        import(customTuningActivityModule())
    }

    private val viewModel: CustomTuningViewModel by buildViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_tuning)
        initUi()
    }

    private fun initUi() {
        initViewModel()
        initStringsView()

        val textWatcher = TextUpdateWatcher { viewModel.updateTitle(it) }
        tuning_name_edit_text.addTextChangedListener(textWatcher)
        add_string_fab.setOnClickListener { showSelectNoteDialog() }
        btn_save.setOnClickListener {
            val name = viewModel.saveTuningAndGetName()
            val data = Intent()
            data.putExtra(CUSTOM_TUNING_EXTRA, name)
            setResult(Activity.RESULT_OK, data)
            finish()
        }
    }

    private fun initViewModel() {
        viewModel.viewStateLiveDate.observe(this, Observer {
            strings_view.setStrings(it.notes)
            btn_save?.isEnabled = it.tuningName.isNotBlank() && it.notes.isNotEmpty()
        })
    }

    private fun initStringsView() = with(strings_view) {
        stringClickListener = { showConfirmDeleteDialog(it) }
        noteClickListener = { showSelectNoteDialog(it) }
        moveStringCallback = { oldIndex, newIndex -> viewModel.moveNote(oldIndex, newIndex) }
    }

    private fun showConfirmDeleteDialog(noteIndex: Int): Boolean {
        ConfirmDeleteDialog(this) { viewModel.removeNote(noteIndex) }.show()
        return true
    }

    private fun showSelectNoteDialog(noteIndex: Int = -1, numberedName: String = "C4") {
        val notePicker =
            SelectNoteDialog(this) { s -> onNoteSelected(noteIndex, s) }
        notePicker.show(numberedName, viewModel.getNoteRange())
    }

    private fun onNoteSelected(noteIndex: Int, noteName: String) {
        if (noteIndex < 0) createNewString(noteName) else updateString(noteIndex, noteName)
    }

    private fun createNewString(noteName: String) {
        viewModel.addNote(noteName)
    }

    private fun updateString(noteIndex: Int, noteName: String) {
        viewModel.updateNote(noteIndex, noteName)
    }
}