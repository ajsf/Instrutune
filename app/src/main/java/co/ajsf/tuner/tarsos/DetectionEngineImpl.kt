package co.ajsf.tuner.tarsos

import be.tarsos.dsp.AudioDispatcher
import be.tarsos.dsp.AudioEvent
import be.tarsos.dsp.pitch.PitchDetectionHandler
import be.tarsos.dsp.pitch.PitchDetectionResult
import be.tarsos.dsp.pitch.PitchProcessor
import co.ajsf.tuner.frequencydetection.DetectionEngine
import co.ajsf.tuner.frequencydetection.DetectionHandler
import co.ajsf.tuner.mapper.TarsosResponseToModelMapper

typealias TarsosResponse = Pair<PitchDetectionResult, AudioEvent>

class DetectionEngineImpl(
    private val dispatcher: AudioDispatcher,
    private val mapper: TarsosResponseToModelMapper,
    sampleRate: Int,
    bufferSize: Int
) : DetectionEngine {

    private var audioThread: Thread? = null
    private var detectionHandler: DetectionHandler? = null

    private val pdh = PitchDetectionHandler { res, event ->
        handleDetection(res, event)
    }

    private val pitchProcessor = PitchProcessor(
        PitchProcessor.PitchEstimationAlgorithm.MPM, sampleRate.toFloat(), bufferSize, pdh
    )

    override fun listen(detectionHandler: DetectionHandler) {
        this.detectionHandler = detectionHandler
        dispatcher.addAudioProcessor(pitchProcessor)
        audioThread = Thread(dispatcher, "Audio Thread")
        audioThread?.start()
    }

    override fun stopListening() {
        dispatcher.removeAudioProcessor(pitchProcessor)
        dispatcher.stop()
        audioThread?.interrupt()
        audioThread = null
        detectionHandler = null
    }

    fun handleDetection(pitchDetectionResult: PitchDetectionResult, audioEvent: AudioEvent) {
        val detectionResult = mapper.invoke(pitchDetectionResult to audioEvent)
        detectionHandler?.invoke(detectionResult)
    }
}