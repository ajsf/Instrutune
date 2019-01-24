package co.ajsf.tuner.tuner.frequencydetection.detector

import co.ajsf.tuner.tuner.frequencydetection.model.DetectionResult
import io.reactivex.Flowable

interface DetectionEngine {
    fun listen(): Flowable<DetectionResult>
}