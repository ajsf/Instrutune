package co.ajsf.tuner.view.tunerview

fun calculateTunerXValue(viewWidth: Int, delta: Float): Float {
    val midPoint = viewWidth / 2
    val multiplier = midPoint / 100f
    val newX = multiplier * delta + midPoint
    return newX
}