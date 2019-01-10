package co.ajsf.tuner.frequencyDetection

import co.ajsf.tuner.model.DetectionResult

typealias DetectionHandler = (DetectionResult) -> Unit

interface DetectionEngine {
    fun listen(detectionHandler: DetectionHandler)
    fun stopListening()
}