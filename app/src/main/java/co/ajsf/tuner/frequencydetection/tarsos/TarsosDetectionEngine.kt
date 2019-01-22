package co.ajsf.tuner.frequencydetection.tarsos

import co.ajsf.tuner.frequencydetection.DetectionEngine
import co.ajsf.tuner.mapper.TarsosResponseToModelMapper
import co.ajsf.tuner.model.DetectionResult
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