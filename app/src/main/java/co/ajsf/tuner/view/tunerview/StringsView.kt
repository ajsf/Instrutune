package co.ajsf.tuner.view.tunerview

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import co.ajsf.tuner.R

class StringsView
@JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var stringViews: List<TunerStringView> = listOf()
    private var selectedString = ""

    init {
        orientation = LinearLayout.HORIZONTAL
        setPadding(16, 8, 16, 0)
        setBackgroundResource(R.color.backgroundColor)
    }

    fun setStrings(numberedNames: List<String>) {
        removeAllViews()
        stringViews = numberedNames.map {
            TunerStringView(context).apply { numberedName = it }
        }
        stringViews.onEach { addView(it) }
    }

    fun selectString(numberedName: String) {
        unselectStrings()
        selectedString = numberedName
        stringViews.onEach {
            if (it.numberedName == numberedName) {
                it.setSelected()
            }
        }
    }

    fun unselectStrings() {
        if (selectedString.isNotBlank()) {
            stringViews.firstOrNull { it.numberedName == selectedString }?.unselect()
            selectedString = ""
        }
    }
}