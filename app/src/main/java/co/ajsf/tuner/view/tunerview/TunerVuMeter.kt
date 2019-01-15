package co.ajsf.tuner.view.tunerview

import android.content.Context
import android.util.AttributeSet
import android.view.View
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

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        setIndicatorDelta(0f)
    }

    fun setIndicatorDelta(delta: Float) {
        vu_indicator.x = calculateTunerXValue(width, delta)
    }

    fun setIndicatorVisibility(isVisible: Boolean) {
        vu_indicator.visibility = when {
            isVisible -> View.VISIBLE
            else -> View.GONE
        }
    }
}