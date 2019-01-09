package co.ajsf.tuner.view

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import co.ajsf.tuner.R

class StringsView
@JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val strings = listOf("E", "A", "D", "G", "B", "E")

    init {
        orientation = LinearLayout.HORIZONTAL
        setPadding(16, 8, 16, 0)
        setBackgroundResource(R.color.backgroundColor)
        strings.onEach {
            addView(TunerStringView(context).apply {
                setStringName(it.first())
            })
        }
    }
}