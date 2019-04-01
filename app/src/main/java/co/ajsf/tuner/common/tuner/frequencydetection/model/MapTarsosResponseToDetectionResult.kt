package co.ajsf.tuner.common.tuner.frequencydetection.model

import co.ajsf.tuner.common.tuner.frequencydetection.tarsos.TarsosResponse

typealias TarsosResponseToModelMapper = (TarsosResponse) -> DetectionResult

fun mapTarsosResponseToDetectionResult(response: TarsosResponse): DetectionResult {
    val (res, event) = response
    return DetectionResult(
        res.pitch,
        event.isSilence(-78.0),
        res.isPitched,
        res.probability,
        event.getdBSPL().toFloat()
    )
}