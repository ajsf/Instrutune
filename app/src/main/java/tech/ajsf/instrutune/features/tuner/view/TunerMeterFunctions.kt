package tech.ajsf.instrutune.features.tuner.view

import kotlin.math.roundToInt

fun scaleAndCurveDelta(delta: Float): Int {
    val absDelta = Math.abs(delta.toDouble())
    val intDelta = Math.pow(absDelta, .61).roundToInt()
    return Math.min(intDelta, 16)
}