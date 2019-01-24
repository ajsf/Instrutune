package co.ajsf.tuner.view.tunerview

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

    fun selectInstrument(stringNames: List<Char>): Unit = strings_view.setStringNames(stringNames)

    fun selectString(stringNumber: Int) {
        if (stringNumber != -1) {
            strings_view.selectString(stringNumber)
            tuner_vu_view.setIndicatorVisibility(true)
        } else {
            tuner_vu_view.setIndicatorVisibility(false)
            strings_view.unselectStrings()
        }
    }

    fun setDelta(delta: Float) = tuner_vu_view.setIndicatorDelta(delta)

    fun setFreq(freq: String) {
        recent_freq_text.text = freq
    }

    fun setNoteName(noteName: String) {
        note_name_text.text = noteName
    }
}
