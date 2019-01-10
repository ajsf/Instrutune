package co.ajsf.tuner.mapper

import co.ajsf.tuner.model.DetectionResult
import co.ajsf.tuner.tarsos.TarsosResponse

typealias TarsosResponseToModelMapper = (TarsosResponse) -> DetectionResult

fun mapTarsosResponseToDetectionResult(response: TarsosResponse): DetectionResult {
    val (res, event) = response
    return DetectionResult(
        res.pitch,
        event.isSilence(-45.0),
        res.isPitched,
        res.probability,
        event.getdBSPL().toFloat()
    )
}