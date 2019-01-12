package co.ajsf.tuner.frequencydetection

import co.ajsf.tuner.model.DetectionResult

typealias DetectionHandler = (DetectionResult) -> Unit
typealias EngineBuilder = () -> DetectionEngine

interface DetectionEngine {
    fun listen(detectionHandler: DetectionHandler)
    fun stopListening()
}