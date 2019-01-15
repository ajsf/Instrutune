package co.ajsf.tuner.tarsos

import be.tarsos.dsp.io.android.AudioDispatcherFactory
import co.ajsf.tuner.mapper.mapTarsosResponseToDetectionResult

object TarsosEngineBuilder {

    fun build(sampleRate: Int, bufferSize: Int): DetectionEngineImpl =
        DetectionEngineImpl(buildAudioDispatcher(sampleRate, bufferSize), mapper, sampleRate, bufferSize)

    private fun buildAudioDispatcher(sampleRate: Int, bufferSize: Int) = AudioDispatcherFactory
        .fromDefaultMicrophone(sampleRate, bufferSize, 1024)

    private val mapper = ::mapTarsosResponseToDetectionResult
}