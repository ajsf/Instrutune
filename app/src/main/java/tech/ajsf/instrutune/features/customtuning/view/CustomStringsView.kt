package tech.ajsf.instrutune.features.customtuning.view

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.MotionEvent.*
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.animation.doOnEnd
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import tech.ajsf.instrutune.common.view.StringsView
import tech.ajsf.instrutune.common.view.TunerStringView
import tech.ajsf.instrutune.features.customtuning.moveItem
import kotlinx.android.synthetic.main.tuner_string.view.*

class CustomStringsView
@JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : StringsView(context, attrs, defStyleAttr) {

    var moveStringCallback: (Int, Int) -> Unit = { _, _ -> }

    var stringClickListener: (Int) -> Boolean = { true }

    var noteClickListener: (Int) -> Unit = {}

    private val scaleFactor = 1.05f

    override fun setStrings(numberedNames: List<String>) {
        if (numberedNames.size != stringViews.size) {
            super.setStrings(numberedNames)
            stringViews.forEachIndexed { index, view -> setupStringView(index, view) }
        } else {
            stringViews.forEachIndexed { index, view -> view.numberedName = numberedNames[index] }
        }
    }

    private fun setupStringView(index: Int, view: TunerStringView) = with(view) {
        string_name_outline.setOnLongClickListener { stringClickListener(index) }
        string_name_outline.setOnClickListener { noteClickListener(index) }
        setOnTouchListener(StringDragListener())
    }

    inner class StringDragListener : View.OnTouchListener {

        private var startX: Int = 0

        private var index = 0

        private var placeholder: FrameLayout? = null

        override fun onTouch(view: View?, event: MotionEvent?): Boolean {
            if (view == null || event == null) return false

            view as TunerStringView
            val x = event.rawX.toInt()

            when (event.action and ACTION_MASK) {
                ACTION_DOWN -> handleActionDown(x, view)
                ACTION_UP -> handleActionUp(x, view)
                ACTION_MOVE -> handleActionMove(x, view)
            }
            return true
        }

        private fun handleActionDown(eventX: Int, view: TunerStringView) {
            startX = eventX
            index = stringViews.indexOf(view)
            selectString(view)
        }


        private fun handleActionUp(eventX: Int, view: TunerStringView) {
            moveSelectedString(eventX, view.width / 2)
            refreshLayout(view)
        }

        private fun handleActionMove(eventX: Int, view: View) {
            if (eventX in (0..width)) {
                val bias = eventX / this@CustomStringsView.width.toFloat()
                with(ConstraintSet()) {
                    clone(this@CustomStringsView)
                    setHorizontalBias(view.id, bias)
                    applyTo(this@CustomStringsView)
                }
            }
        }

        private fun selectString(view: TunerStringView) {
            view.updateSelectedString(view.numberedName, false)
            scaleViewUp(view)
            val placeholder = createPlaceHolder()
            updateConstraints(view, placeholder.id)
        }

        private fun scaleViewUp(view: TunerStringView) = with(view) {
            translationZ = 100f
            scaleX = scaleFactor
            scaleY = scaleFactor
        }

        private fun updateConstraints(view: TunerStringView, placeholderId: Int) {
            val bias = startX / this@CustomStringsView.width.toFloat()
            val set = createMovementConstraints(view.id, bias)
            set.addPlaceholderConstraints(placeholderId, view.width)
            set.applyTo(this@CustomStringsView)
        }

        private fun createMovementConstraints(viewId: Int, bias: Float): ConstraintSet = ConstraintSet().apply {
            clone(this@CustomStringsView)
            removeLeftAndRightConstraints(viewId)
            connectToParentLeftAndRight(viewId)
            setHorizontalBias(viewId, bias)
        }

        private fun ConstraintSet.removeLeftAndRightConstraints(viewId: Int) {
            clear(viewId, ConstraintSet.LEFT)
            clear(viewId, ConstraintSet.RIGHT)
        }

        private fun ConstraintSet.connectToParentLeftAndRight(viewId: Int) {
            connect(viewId, ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT)
            connect(viewId, ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT)
        }

        private fun createPlaceHolder(): FrameLayout {
            val placeholderLayout = FrameLayout(context)
            placeholderLayout.id = View.generateViewId()
            placeholder = placeholderLayout
            addView(placeholderLayout)
            return placeholderLayout
        }

        private fun ConstraintSet.addPlaceholderConstraints(id: Int, width: Int) {
            setConstraints(index, id, this)
            constrainWidth(id, width)

            if (stringViews.size > 1) {
                if (index < stringViews.lastIndex) {
                    val nextViewId = stringViews[index + 1].id
                    clear(nextViewId, ConstraintSet.LEFT)
                    connect(id, ConstraintSet.RIGHT, nextViewId, ConstraintSet.LEFT)
                    connect(nextViewId, ConstraintSet.LEFT, id, ConstraintSet.RIGHT)
                }

                if (index > 0) {
                    val previousViewId = stringViews[index - 1].id
                    clear(previousViewId, ConstraintSet.RIGHT)
                    connect(id, ConstraintSet.LEFT, previousViewId, ConstraintSet.RIGHT)
                    connect(previousViewId, ConstraintSet.RIGHT, id, ConstraintSet.LEFT)
                }
            }
        }

        private fun moveSelectedString(eventX: Int, offset: Int) {
            val newIndex = findNewIndex(eventX, offset)
            moveStringCallback(index, newIndex)
            updateStringViews(newIndex)
        }

        private fun findNewIndex(x: Int, offset: Int): Int {
            var newIndex = index

            if (stringViews.size > 1) {
                if (index > 0 && x <= stringViews[0].x.toInt() + offset) {
                    println(stringViews[0].x)
                    newIndex = 0
                } else if (index != stringViews.lastIndex && x >= stringViews.last().x.toInt() + offset) {
                    newIndex = stringViews.lastIndex
                } else {
                    if (x < startX) {
                        for (i in 1..stringViews.lastIndex) {
                            if (i == index) continue
                            if (x in stringViews[i - 1].x.toInt() + offset..stringViews[i].x.toInt() + offset) {
                                newIndex = i
                                break
                            }
                        }
                    } else if (x > startX) {
                        for (i in 0 until stringViews.lastIndex) {
                            if (i == index) continue
                            if (x in stringViews[i].x.toInt() + offset..stringViews[i + 1].x.toInt() + offset) {
                                newIndex = i
                                break
                            }
                        }
                    }
                }
            }
            return newIndex
        }

        private fun updateStringViews(newIndex: Int) {
            val newViews = stringViews.toMutableList().also { it.moveItem(index, newIndex) }
            stringViews = newViews
        }

        private fun refreshLayout(view: TunerStringView) {
            createAnimations(view)

            createConstraints().apply {
                removePlaceholder()
                applyTo(this@CustomStringsView)
            }
        }

        private fun createAnimations(view: TunerStringView) {
            val decelerator = DecelerateInterpolator()
            val transitionTime = 600L
            val propertyValues = arrayOf(
                PropertyValuesHolder.ofFloat(View.SCALE_X, scaleFactor, 1f),
                PropertyValuesHolder.ofFloat(View.SCALE_Y, scaleFactor, 1f),
                PropertyValuesHolder.ofFloat(View.TRANSLATION_Z, 100f, 1f)
            )

            ObjectAnimator.ofPropertyValuesHolder(view, *propertyValues).apply {
                interpolator = decelerator
                duration = transitionTime
                doOnEnd { view.updateSelectedString("") }
                start()
            }

            val transition = ChangeBounds().apply {
                duration = transitionTime
                interpolator = decelerator
            }

            TransitionManager.beginDelayedTransition(this@CustomStringsView, transition)
        }

        private fun ConstraintSet.removePlaceholder() = placeholder?.let {
            clear(it.id)
            removeViewInLayout(it)
            placeholder = null
        }
    }
}