package co.ajsf.tuner.features.common.view

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.widget.LinearLayout
import co.ajsf.tuner.R

open class StringsView
@JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        orientation = LinearLayout.HORIZONTAL
        gravity = Gravity.CENTER
        clipChildren = false
    }

    protected var stringViews = listOf<TunerStringView>()

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setPadding(16, 8, 16, 0)
        setBackgroundResource(R.color.backgroundColor)
    }

    open fun setStrings(numberedNames: List<String>) {
        removeAllViews()
        stringViews = numberedNames.map { createStringView(it) }
    }

    private fun createStringView(stringName: String): TunerStringView =
        TunerStringView(context)
            .apply { numberedName = stringName }
            .also { addView(it) }

    fun setSelectedString(selectedStringName: String): Unit = stringViews
        .forEach { it.updateSelectedString(selectedStringName) }
}