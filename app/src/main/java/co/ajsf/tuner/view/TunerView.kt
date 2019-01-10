package co.ajsf.tuner.view

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import co.ajsf.tuner.R
import kotlinx.android.synthetic.main.tuner_view.view.*

class TunerView
@JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        LinearLayout.inflate(context, R.layout.tuner_view, this)
    }

    fun selectInstrument(name: String, stringNames: List<Char>) {
        strings_view.setStringNames(stringNames)
        selected_instrument_text.text = name
    }

    fun selectString(stringNumber: Int) = strings_view.selectString(stringNumber)
}