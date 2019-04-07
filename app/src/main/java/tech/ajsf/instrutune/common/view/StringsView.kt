package tech.ajsf.instrutune.common.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import tech.ajsf.instrutune.R

open class StringsView
@JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    protected var stringViews = listOf<TunerStringView>()

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setPadding(16, 8, 16, 0)
        setBackgroundResource(R.color.backgroundColor)
    }

    open fun setStrings(numberedNames: List<String>) {
        removeAllViews()
        stringViews = numberedNames.mapIndexed { index, s -> createStringView(s, index) }
        val constraintSet = createConstraints()
        constraintSet.applyTo(this)
    }

    protected fun createConstraints(): ConstraintSet {
        val constraintSet = ConstraintSet()
        for (i in stringViews.indices) {
            setConstraints(i, stringViews[i].id, constraintSet)
            constraintSet.constrainWidth(stringViews[i].id, ConstraintSet.WRAP_CONTENT)
        }
        return constraintSet
    }

    protected fun setConstraints(index: Int, viewId: Int, constraintSet: ConstraintSet) {
        if (index == 0) {
            constraintSet.connect(
                viewId,
                ConstraintSet.LEFT,
                ConstraintSet.PARENT_ID,
                ConstraintSet.LEFT
            )
        } else {
            constraintSet.connect(
                viewId,
                ConstraintSet.LEFT,
                stringViews[index - 1].id,
                ConstraintSet.RIGHT
            )
        }

        if (index == stringViews.lastIndex) {
            constraintSet.connect(
                viewId,
                ConstraintSet.RIGHT,
                ConstraintSet.PARENT_ID,
                ConstraintSet.RIGHT
            )
        } else {
            constraintSet.connect(
                viewId,
                ConstraintSet.RIGHT,
                stringViews[index + 1].id,
                ConstraintSet.LEFT
            )
        }

        constraintSet.connect(
            viewId, ConstraintSet.TOP,
            ConstraintSet.PARENT_ID, ConstraintSet.TOP
        )
        constraintSet.connect(
            viewId, ConstraintSet.BOTTOM,
            ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM
        )
    }

    private fun createStringView(stringName: String, index: Int): TunerStringView =
        TunerStringView(context)
            .apply {
                id = View.generateViewId()
                numberedName = stringName
                noteNumber = index
                transitionName = "$stringName$index"
            }
            .also { addView(it) }

    fun setSelectedString(selectedStringName: String): Unit = stringViews
        .forEach { it.updateSelectedString(selectedStringName) }
}