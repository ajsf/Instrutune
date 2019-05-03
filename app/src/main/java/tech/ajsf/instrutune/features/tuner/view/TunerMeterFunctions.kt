package tech.ajsf.instrutune.features.tuner.view

fun scaleAndCurveDelta(delta: Int): Int {
    val absDelta = Math.abs(delta.toDouble())
    val intDelta = Math.pow(absDelta, .63).toInt()
    return Math.min(intDelta, 16)
}