package co.ajsf.tuner.common.tuner.frequencydetection.tarsos

import co.ajsf.tuner.common.tuner.frequencydetection.detector.DetectionEngine
import co.ajsf.tuner.common.tuner.frequencydetection.model.DetectionResult
import co.ajsf.tuner.common.tuner.frequencydetection.model.TarsosResponseToModelMapper
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