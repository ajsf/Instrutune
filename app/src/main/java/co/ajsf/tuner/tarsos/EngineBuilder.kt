package co.ajsf.tuner.tarsos

import be.tarsos.dsp.io.android.AudioDispatcherFactory
import co.ajsf.tuner.mapper.mapTarsosResponseToDetectionResult

object EngineBuilder {

    fun build(): DetectionEngineImpl = DetectionEngineImpl(buildAudioDispatcher(), mapper)

    private fun buildAudioDispatcher() = AudioDispatcherFactory
        .fromDefaultMicrophone(22050, 1024, 0)

    private val mapper = ::mapTarsosResponseToDetectionResult
}