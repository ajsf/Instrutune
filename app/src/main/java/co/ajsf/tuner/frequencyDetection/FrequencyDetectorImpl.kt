package co.ajsf.tuner.frequencyDetection

import be.tarsos.dsp.AudioDispatcher
import be.tarsos.dsp.io.android.AudioDispatcherFactory
import be.tarsos.dsp.pitch.PitchDetectionHandler
import be.tarsos.dsp.pitch.PitchProcessor

class FrequencyDetectorImpl : FrequencyDetector {

    private var freqListener: ((Float) -> Unit)? = null
    private var audioThread: Thread? = null
    private var dispatcher: AudioDispatcher? = null
    private var lastPitch = -1f

    private val pdh = PitchDetectionHandler { res, event ->
        if (event.isSilence(-80.0).not() && res.isPitched) {
            lastPitch = res.pitch
            freqListener?.invoke(lastPitch)
        } else if (lastPitch != -1f) {
            lastPitch = -1f
            freqListener?.invoke(lastPitch)
        }
    }

    private val pitchProcessor = PitchProcessor(
        PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, 22050f, 1024, pdh
    )

    override fun listen(listener: FrequencyListener) {
        freqListener = listener
        dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(22050, 1024, 0)
        dispatcher?.addAudioProcessor(pitchProcessor)
        audioThread = Thread(dispatcher, "Audio Thread")
        audioThread?.start()
    }

    override fun stopListening() {
        freqListener = null
        dispatcher?.removeAudioProcessor(pitchProcessor)
        dispatcher?.stop()
        dispatcher = null
        audioThread?.interrupt()
        audioThread = null
    }
}