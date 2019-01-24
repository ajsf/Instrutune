package co.ajsf.tuner.tuner.frequencydetection.model

import co.ajsf.tuner.tuner.frequencydetection.tarsos.TarsosResponse

typealias TarsosResponseToModelMapper = (TarsosResponse) -> DetectionResult

fun mapTarsosResponseToDetectionResult(response: TarsosResponse): DetectionResult {
    val (res, event) = response
    return DetectionResult(
        res.pitch,
        event.isSilence(-50.0),
        res.isPitched,
        res.probability,
        event.getdBSPL().toFloat()
    )
}