package tech.ajsf.instrutune.features.tuner.view

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.tuner_vu_meter.view.*
import tech.ajsf.instrutune.R

class TunerMeter
@JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val leftIndicators: List<TunerIndicator>
    private val rightIndicators: List<TunerIndicator>

    init {
        inflate(context, R.layout.tuner_vu_meter, this)

        leftIndicators = listOf(
            indicator_left_1,
            indicator_left_2,
            indicator_left_3,
            indicator_left_4,
            indicator_left_5,
            indicator_left_6,
            indicator_left_7,
            indicator_left_8,
            indicator_left_9,
            indicator_left_10,
            indicator_left_11,
            indicator_left_12,
            indicator_left_13,
            indicator_left_14
        )

        rightIndicators = listOf(
            indicator_right_1,
            indicator_right_2,
            indicator_right_3,
            indicator_right_4,
            indicator_right_5,
            indicator_right_6,
            indicator_right_7,
            indicator_right_8,
            indicator_right_9,
            indicator_right_10,
            indicator_right_11,
            indicator_right_12,
            indicator_right_13,
            indicator_right_14
        )
    }

    fun setIndicatorDelta(delta: Float) {
        val deltaInt = delta.toInt()
        when {
            deltaInt in (-1..1) -> setInTune()
            deltaInt <= -2 -> setFlat(-deltaInt)
            else -> setSharp(deltaInt)
        }
    }

    private fun setInTune() {
        indicator_main.setInTune()
        leftIndicators.setInTune()
        rightIndicators.setInTune()
    }

    private fun setFlat(deltaInt: Int) {
        val smoothedDelta = smoothDelta(deltaInt)
        indicator_main.setActive()
        rightIndicators.setInactive()
        leftIndicators.setByDelta(smoothedDelta)
    }

    private fun setSharp(deltaInt: Int) {
        val smoothedDelta = smoothDelta(deltaInt)
        indicator_main.setActive()
        leftIndicators.setInactive()
        rightIndicators.setByDelta(smoothedDelta)
    }

    private fun List<TunerIndicator>.setInactive() = indices.reversed()
        .forEach { postFunction(get(it)::setInactive, size - it) }

    private fun List<TunerIndicator>.setInTune() {
        forEachIndexed { index, tunerIndicator ->
            postFunction(tunerIndicator::setInTune, index + 1)
        }
    }

    private fun List<TunerIndicator>.setByDelta(delta: Int) {
        forEachIndexed { index, tunerIndicator ->
            if (delta > index) postFunction(tunerIndicator::setActive, index)
            else postFunction(tunerIndicator::setInactive, index + 1)
        }
    }

    private fun postFunction(function: () -> Unit, delay: Int) {
        postDelayed({ function.invoke() }, delay * 28L)
    }

    fun setIndicatorVisibility(isVisible: Boolean) {
        if (isVisible.not()) {
            postFunction(indicator_main::setInactive, 15)
            leftIndicators.setInactive()
            rightIndicators.setInactive()
        }
    }

    private fun smoothDelta(deltaInt: Int) = when (Math.abs(deltaInt)) {
        in (2..4) -> 1
        in (5..7) -> 2
        in (7..9) -> 3
        in (10..12) -> 4
        in (13..16) -> 5
        in (17..20) -> 6
        in (21..24) -> 7
        in (25..29) -> 8
        in (30..35) -> 9
        in (34..41) -> 10
        in (42..50) -> 11
        in (51..60) -> 12
        in (61..70) -> 13
        in (71..80) -> 14
        in (80..90) -> 15
        else -> 16
    }
}