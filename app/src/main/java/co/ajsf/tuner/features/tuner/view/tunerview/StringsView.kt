package co.ajsf.tuner.features.tuner.view.tunerview

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.widget.LinearLayout
import co.ajsf.tuner.R

class StringsView
@JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        orientation = LinearLayout.HORIZONTAL
        gravity = Gravity.CENTER
        setPadding(16, 8, 16, 0)
        setBackgroundResource(R.color.backgroundColor)
    }

    private var stringViews = listOf<TunerStringView>()

    fun setStrings(numberedNames: List<String>) {
        removeAllViews()
        stringViews = numberedNames.map {
            TunerStringView(context).apply {
                numberedName = it
            }
        }.onEach { addView(it) }
    }

    fun setSelectedString(selectedStringName: String): Unit = stringViews
        .forEach { it.updateSelectedString(selectedStringName) }
}