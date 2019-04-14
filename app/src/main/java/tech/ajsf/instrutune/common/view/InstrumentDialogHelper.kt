package tech.ajsf.instrutune.common.view

import android.content.Context
import android.view.LayoutInflater
import android.widget.NumberPicker
import androidx.appcompat.app.AlertDialog
import tech.ajsf.instrutune.R

class InstrumentDialogHelper(private val context: Context) {

    private var currentDialog: AlertDialog? = null

    fun showSelectInstrumentDialog(instruments: List<String>, onSelect: (String) -> Unit) {
        val title = context.getString(R.string.select_instrument_title)
        showSelectorDialog(title, instruments.toTypedArray()) {
            onSelect(instruments[it])
        }
    }

    fun showSelectTuningDialog(tunings: List<String>, onSelect: (Int) -> Unit) {
        val title = context.getString(R.string.select_tuning_title)
        showSelectorDialog(title, tunings.toTypedArray()) { onSelect(it) }
    }

    private fun showSelectorDialog(title: String, items: Array<String>, onClick: (Int) -> Unit) {
        with(AlertDialog.Builder(context)) {
            setTitle(title)
            setItems(items) { _, i -> onClick(i) }
            finishBuilding()
        }.show()
    }

    fun showSetMiddleADialog(offset: Int, onSave: (Int) -> Unit) {
        var newOffset = offset
        val maxVariance = 10

        val picker = LayoutInflater.from(context.applicationContext)
            .inflate(R.layout.freq_picker, null) as NumberPicker

        picker.apply {
            maxValue = maxVariance * 2
            minValue = 0
            wrapSelectorWheel = false
            displayedValues = (440 - maxVariance..440 + maxVariance)
                .map { it.toString() }.toTypedArray()

            setOnValueChangedListener { numPicker, _, _ ->
                newOffset = numPicker.value - maxVariance
            }

            value = newOffset + maxVariance
        }

        with(AlertDialog.Builder(context)) {
            setView(picker)
            setTitle(context.getString(R.string.select_center_freq))
            setPositiveButton(context.getString(R.string.btn_select_text)) { _, _ ->
                onSave(newOffset)
            }
            setNegativeButton(context.getString(R.string.btn_cancel_text)) { _, _ -> }
            setNeutralButton(context.getString(R.string.btn_reset_text)) { _, _ -> onSave(0) }
            setCancelable(true)
            finishBuilding()
        }.show()
    }

    private fun AlertDialog.Builder.finishBuilding(): AlertDialog {
        val dialog = create()
        currentDialog = dialog
        return dialog
    }

    fun clear() {
        currentDialog?.let {
            it.dismiss()
            currentDialog = null
        }
    }
}