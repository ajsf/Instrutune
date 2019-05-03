package tech.ajsf.instrutune.features.tuner.view

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.indicator_view.view.*
import tech.ajsf.instrutune.R

abstract class TunerIndicator @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var activeColor: Int = 0
    private val inactiveColor: Int = ContextCompat.getColor(context, R.color.colorDisabled)
    private val intuneColor: Int = ContextCompat.getColor(context, R.color.indicator_in)

    private val indicatorAnimator: IndicatorAnimator by lazy {
        IndicatorAnimator(img_indicator.drawable, activeColor, inactiveColor, intuneColor)
    }

    init {
        attrs?.let {
            val a = context.obtainStyledAttributes(it, R.styleable.TunerIndicator)
            val color = a.getColor(R.styleable.TunerIndicator_activeColor, inactiveColor)
            activeColor = color
            a.recycle()
        }
    }

    fun setActive(delay: Long = 0) {
        indicatorAnimator.setActive(delay)
    }

    fun setInactive(time: Long, delay: Long) {
        indicatorAnimator.setInactive(time, delay)
    }

    fun setInTune(delay: Long = 0) {
        indicatorAnimator.setInTune(delay)
    }
}

class TunerIndicatorSmall
@JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : TunerIndicator(context, attrs, defStyleAttr) {

    init {
        inflate(context, R.layout.indicator_view, this)
    }
}

class TunerIndicatorMain
@JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : TunerIndicator(context, attrs, defStyleAttr) {

    init {
        inflate(context, R.layout.indicator_main_view, this)
    }
}