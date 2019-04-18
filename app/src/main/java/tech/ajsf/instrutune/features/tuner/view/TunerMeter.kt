package tech.ajsf.instrutune.features.tuner.view

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.tuner_vu_meter.view.*
import tech.ajsf.instrutune.R

private sealed class Direction
private object Flat : Direction()
private object Sharp : Direction()
private object Off : Direction()

private data class TunerState(val direction: Direction = Off, val delta: Float = 0f)

class TunerMeter
@JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val leftIndicators: List<TunerIndicator>
    private val rightIndicators: List<TunerIndicator>

    private var tunerState = TunerState()

    private val lightDelay = 35L

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

    fun clearIndicators() {
        indicator_main.setInactive(250, 700)
        leftIndicators.setInactive(250, 50)
        rightIndicators.setInactive(250, 50)
    }

    fun setIndicatorDelta(delta: Float) {
        when (delta.toInt()) {
            in (-3..3) -> setInTune()
            in (Int.MIN_VALUE..-3) -> setFlat(delta + 3)
            else -> setSharp(delta - 3)
        }
    }

    private fun setInTune() {
        tunerState = TunerState()
        indicator_main.setInTune()
        leftIndicators.setInTune()
        rightIndicators.setInTune()
    }

    private fun setFlat(delta: Float) {
        val smoothedDelta = scaleAndCurveDelta(delta)

        indicator_main.setActive()
        if (tunerState.direction !is Flat) rightIndicators.setInactive()
        leftIndicators.setByDelta(smoothedDelta)

        tunerState = TunerState(Flat, delta)
    }

    private fun setSharp(delta: Float) {
        val smoothedDelta = scaleAndCurveDelta(delta)

        indicator_main.setActive()
        if (tunerState.direction !is Sharp) leftIndicators.setInactive()
        rightIndicators.setByDelta(smoothedDelta)

        tunerState = TunerState(Sharp, delta)
    }

    private fun List<TunerIndicator>.setInactive(time: Long= 15, delay: Long = 15) {
        indices.reversed()
            .forEach {
                get(it).setInactive(time, delay * ((lastIndex - it)))
            }
    }

    private fun List<TunerIndicator>.setInTune() {
        forEachIndexed { index, tunerIndicator -> tunerIndicator.setInTune(index * lightDelay) }
    }

    private fun List<TunerIndicator>.setByDelta(delta: Int) {
        forEachIndexed { index, tunerIndicator ->
            if (delta > index) tunerIndicator.setActive(index * lightDelay)
            else tunerIndicator.setInactive(50, 10)
        }
    }
}