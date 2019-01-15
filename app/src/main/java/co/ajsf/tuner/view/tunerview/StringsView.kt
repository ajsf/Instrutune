package co.ajsf.tuner.view.tunerview

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.LinearLayout
import co.ajsf.tuner.R

class StringsView
@JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var stringViews: List<TunerStringView> = listOf()
    private var selectedString = -1

    init {
        orientation = LinearLayout.HORIZONTAL
        setPadding(16, 8, 16, 0)
        setBackgroundResource(R.color.backgroundColor)
    }

    fun setStringNames(strings: List<Char>) {
        removeAllViews()
        stringViews = strings.map {
            TunerStringView(context).apply {
                setStringName(it)
            }
        }
        stringViews.onEach { addView(it) }
    }

    fun selectString(stringNumber: Int) {
        if (stringNumber in stringViews.indices) {
            Log.d("DETECT", "Selecting string: $stringNumber")
            unselectStrings()
            stringViews[stringNumber].setSelected()
            selectedString = stringNumber
        }
    }

    fun unselectStrings() {
        if (selectedString in stringViews.indices) {
            stringViews[selectedString].unselect()
            selectedString = -1
        }
    }
}