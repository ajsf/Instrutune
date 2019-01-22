package co.ajsf.tuner.frequencydetection.tarsos

import be.tarsos.dsp.AudioDispatcher
import be.tarsos.dsp.AudioEvent
import be.tarsos.dsp.pitch.PitchDetectionHandler
import be.tarsos.dsp.pitch.PitchDetectionResult
import be.tarsos.dsp.pitch.PitchProcessor
import co.ajsf.tuner.frequencydetection.DetectorDispatcher

typealias AudioDispatcherBuilder = () -> AudioDispatcher
typealias PitchProcessorBuilder = (PitchDetectionHandler) -> PitchProcessor

typealias TarsosResponse = Pair<PitchDetectionResult, AudioEvent>
typealias TarsosListener = (TarsosResponse) -> Unit
typealias TarsosDispatcher = DetectorDispatcher<TarsosResponse>

class TarsosDispatcherImpl(
    private val audioDispatcherBuilder: AudioDispatcherBuilder,
    private val processorBuilder: PitchProcessorBuilder
) :
    TarsosDispatcher {

    private lateinit var dispatcher: AudioDispatcher
    private var listening: Boolean = false

    override fun listen(listener: TarsosListener) {
        if (listening) throw CurrentlyListeningException()
        dispatcher = audioDispatcherBuilder()
        val pdh = PitchDetectionHandler { res, event ->
            listener(res to event)
        }
        val processor = processorBuilder(pdh)
        dispatcher.addAudioProcessor(processor)
        listening = true
        dispatcher.run()
    }

    override fun stop() {
        if (!listening) throw NotListeningException()
        dispatcher.stop()
        listening = false
    }
}

class NotListeningException : Exception("Listen must be called before calling stop.")
class CurrentlyListeningException :
    Exception("The dispatcher is currently listening. Stop must be called before calling listen again.")