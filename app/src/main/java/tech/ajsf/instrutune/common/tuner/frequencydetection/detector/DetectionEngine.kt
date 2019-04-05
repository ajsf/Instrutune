package tech.ajsf.instrutune.common.tuner.frequencydetection.detector

import tech.ajsf.instrutune.common.tuner.frequencydetection.model.DetectionResult
import io.reactivex.Flowable

interface DetectionEngine {
    fun listen(): Flowable<DetectionResult>
}