package tech.ajsf.instrutune.common.view

import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import tech.ajsf.instrutune.R

class InstrumentDialogHelper(private val activity: AppCompatActivity) {

    fun showSelectInstrumentDialog(instruments: List<String>, onSelect: (String) -> Unit) {
        val title = activity.getString(R.string.select_instrument_title)
        showSelectorDialog(title, instruments.toTypedArray()) {
            onSelect(instruments[it])
        }
    }

    fun showSelectTuningDialog(tunings: List<String>, onSelect: (String) -> Unit) {
        val title = activity.getString(R.string.select_tuning_title)
        showSelectorDialog(title, tunings.toTypedArray()) { onSelect(tunings[it]) }
    }

    private fun showSelectorDialog(title: String, items: Array<String>, onClick: (Int) -> Unit) {
        with(AlertDialog.Builder(activity)) {
            setTitle(title)
            setItems(items) { _, i -> onClick(i) }
            create()
        }.show()
    }
}