package co.ajsf.tuner.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import co.ajsf.tuner.R
import kotlinx.android.synthetic.main.tuner_string.view.*

class TunerStringView
@JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    fun setStringName(name: Char) {
        string_name_text.text = name.toString()
    }

    init {
        layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 1f)
        LayoutInflater.from(context).inflate(R.layout.tuner_string, this)
    }
}