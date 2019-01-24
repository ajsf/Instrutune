package co.ajsf.tuner.tuner.frequencydetection

import co.ajsf.tuner.model.DetectionResult
import io.reactivex.Flowable

interface DetectionEngine {
    fun listen(): Flowable<DetectionResult>
}