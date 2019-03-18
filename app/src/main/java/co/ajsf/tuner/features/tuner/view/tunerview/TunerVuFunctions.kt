package co.ajsf.tuner.features.tuner.view.tunerview

fun calculateVuMeterXTranslation(viewWidth: Int, delta: Float): Float {
    val midPoint = viewWidth / 2
    val multiplier = midPoint / 100f
    val newX = multiplier * delta
    return newX
}