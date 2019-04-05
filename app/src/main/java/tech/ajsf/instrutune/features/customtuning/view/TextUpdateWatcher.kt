package tech.ajsf.instrutune.features.customtuning.view

import android.text.Editable
import android.text.TextWatcher

class TextUpdateWatcher(private val callback: (String) -> Unit) : TextWatcher {
    override fun afterTextChanged(s: Editable?) {}

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        callback.toString()
    }
}