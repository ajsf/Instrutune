package tech.ajsf.instrutune.common.tuner.frequencydetection.tarsos

import tech.ajsf.instrutune.common.tuner.frequencydetection.detector.DetectionEngine
import tech.ajsf.instrutune.common.tuner.frequencydetection.model.DetectionResult
import tech.ajsf.instrutune.common.tuner.frequencydetection.model.TarsosResponseToModelMapper
import io.reactivex.Flowable

class TarsosDetectionEngine(
    private val tarsosFlowable: TarsosResponseStream,
    private val mapper: TarsosResponseToModelMapper
) : DetectionEngine {

    override fun listen(): Flowable<DetectionResult> {
        val tarsosStream = tarsosFlowable.getResponseStream()
        return tarsosStream.map {
            mapper.invoke(it)
        }
    }
}