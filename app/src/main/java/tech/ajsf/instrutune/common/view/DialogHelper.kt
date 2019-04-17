package tech.ajsf.instrutune.common.view

import androidx.appcompat.app.AlertDialog

abstract class DialogHelper {

    private var currentDialog: AlertDialog? = null

    fun clear() {
        currentDialog?.let {
            it.dismiss()
            currentDialog = null
        }
    }

    protected fun AlertDialog.Builder.finishBuilding(): AlertDialog {
        val dialog = create()
        currentDialog = dialog
        return dialog
    }
}