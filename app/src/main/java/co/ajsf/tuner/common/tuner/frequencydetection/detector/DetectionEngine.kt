package co.ajsf.tuner.common.tuner.frequencydetection.detector

import co.ajsf.tuner.common.tuner.frequencydetection.model.DetectionResult
import io.reactivex.Flowable

interface DetectionEngine {
    fun listen(): Flowable<DetectionResult>
}