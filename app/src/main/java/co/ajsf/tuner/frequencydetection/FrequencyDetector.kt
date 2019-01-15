package co.ajsf.tuner.frequencydetection

typealias FrequencyListener = (Float) -> Unit

class FrequencyDetector(private val engineBuilder: EngineBuilder) {

    private var freqListener: ((Float) -> Unit)? = null
    private var lastPitch = Float.MIN_VALUE
    private lateinit var engine: DetectionEngine

    val detectionHandler: DetectionHandler = { result ->
        result.apply {
            println("DETECTED: Freq: $pitch Probability: $probability, Dbspl: $dBSPL")
            if (isSilence.not() && isPitched && probability > 0.88) {
                lastPitch = pitch
                freqListener?.invoke(lastPitch)
            } else if (lastPitch != -1f) {
                lastPitch = -1f
                freqListener?.invoke(lastPitch)
            }
        }
    }

    fun listen(listener: FrequencyListener) {
        if (freqListener != null) throw CurrentlyListeningException()
        freqListener = listener
        engine = engineBuilder()
        engine.listen(detectionHandler)
    }

    fun stopListening() {
        if (freqListener == null) throw NotListeningException()
        freqListener = null
        engine.stopListening()
    }
}

class CurrentlyListeningException :
    Exception("The frequency detector is currently listening. Call stopListening before calling listen again.")

class NotListeningException :
    Exception("The frequency detector is not listening listening. Call listen before calling stopListening again.")