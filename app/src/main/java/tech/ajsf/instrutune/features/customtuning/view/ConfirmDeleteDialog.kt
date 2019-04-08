package tech.ajsf.instrutune.features.customtuning.view

import android.content.Context
import androidx.appcompat.app.AlertDialog

class ConfirmDeleteDialog(private val context: Context, private val callback: () -> Unit) {

    private val title = "Are You Sure?"

    fun show(message: String) {
        with(AlertDialog.Builder(context)) {
            setTitle(title)
            setMessage(message)
            setPositiveButton("YES") { _, _ -> callback() }
            setNegativeButton("Cancel") { _, _ -> }
        }.show()
    }
}