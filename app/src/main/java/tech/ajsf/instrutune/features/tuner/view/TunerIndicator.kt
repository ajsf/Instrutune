package tech.ajsf.instrutune.features.tuner.view

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.core.animation.doOnCancel
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import kotlinx.android.synthetic.main.indicator_view.view.*
import tech.ajsf.instrutune.R

abstract class TunerIndicator @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val inactiveColor: Int = ContextCompat.getColor(context, R.color.colorDisabled)
    private val inTuneColor: Int = ContextCompat.getColor(context, R.color.indicator_in)
    private var currentColor = inactiveColor

    private var animator: ValueAnimator? = null
    private var activeColor: Int = 0

    init {
        attrs?.let {
            val a = context.obtainStyledAttributes(it, R.styleable.TunerIndicator)
            val color = a.getColor(R.styleable.TunerIndicator_activeColor, R.color.colorDisabled)
            activeColor = color
            a.recycle()
        }
    }

    fun setActive() {
        createAnimation(activeColor, 200)
    }

    fun setInactive() {
        createAnimation(inactiveColor, 400, 100)
    }

    fun setInTune() {
        createAnimation(inTuneColor, 20)
    }

    private fun createAnimation(toColor: Int, time: Long, delay: Long = 0L) {
        animator?.cancel()
        animator = null
        val drawable = DrawableCompat.wrap(img_indicator.drawable).mutate()
        animator = ValueAnimator.ofObject(ArgbEvaluator(), currentColor, toColor).apply {
            duration = time
            startDelay = delay
            addUpdateListener {
                DrawableCompat.setTint(drawable, it.animatedValue as Int)
            }
            doOnCancel { DrawableCompat.setTint(drawable, toColor) }
            start()
        }
        currentColor = toColor
    }
}

class TunerIndicatorSmall
@JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : TunerIndicator(context, attrs, defStyleAttr) {

    init {
        FrameLayout.inflate(context, R.layout.indicator_view, this)
    }
}

class TunerIndicatorMain
@JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : TunerIndicator(context, attrs, defStyleAttr) {

    init {
        FrameLayout.inflate(context, R.layout.indicator_main_view, this)
    }
}