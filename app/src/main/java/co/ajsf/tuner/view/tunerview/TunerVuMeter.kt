package co.ajsf.tuner.view.tunerview

import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import co.ajsf.tuner.R
import kotlinx.android.synthetic.main.tuner_vu_meter.view.*

class TunerVuMeter
@JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        inflate(context, R.layout.tuner_vu_meter, this)
    }

    fun setIndicatorDelta(delta: Float) {
        val xTranslation = calculateVuMeterXTranslation(width, delta)
        ObjectAnimator.ofFloat(vu_indicator, "translationX", xTranslation).apply {
            duration = 400
            start()
        }
    }

    fun setIndicatorVisibility(isVisible: Boolean) {
        if (isVisible) {
            vu_indicator.imageAlpha = 255
        } else {
            ObjectAnimator.ofInt(vu_indicator, "imageAlpha", 60).apply {
                duration = 400
                start()
            }
        }
    }
}