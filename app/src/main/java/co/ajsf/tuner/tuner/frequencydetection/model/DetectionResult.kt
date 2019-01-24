package co.ajsf.tuner.tuner.frequencydetection.model

data class DetectionResult(
    val pitch: Float,
    val isSilence: Boolean,
    val isPitched: Boolean,
    val probability: Float,
    val dBSPL: Float
)