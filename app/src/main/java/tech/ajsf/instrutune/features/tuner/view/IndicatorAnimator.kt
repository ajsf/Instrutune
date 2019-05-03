package tech.ajsf.instrutune.features.tuner.view

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.drawable.Drawable
import androidx.core.animation.doOnCancel
import androidx.core.graphics.drawable.DrawableCompat

class IndicatorAnimator(private val view: Drawable, private val activeColor: Int, private val inactiveColor: Int, private val intuneColor: Int) {

    private var currentColor = inactiveColor

    private var animator: ValueAnimator? = null

    fun setActive(delay: Long = 0) {
        createAnimation(activeColor, 400, delay)
    }

    fun setInactive(time: Long, delay: Long) {
        createAnimation(inactiveColor, time, delay + 250)
    }

    fun setInTune(delay: Long = 0) {
        createAnimation(intuneColor, 100, delay)
    }

    private fun endAnimation() {
        animator?.cancel()
        animator = null
    }

    private fun createAnimation(toColor: Int, time: Long, delay: Long = 0L) {
        endAnimation()
        val drawable = DrawableCompat.wrap(view).mutate()
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