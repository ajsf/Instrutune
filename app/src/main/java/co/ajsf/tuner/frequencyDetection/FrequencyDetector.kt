package co.ajsf.tuner.frequencyDetection

typealias FrequencyListener = (Float) -> Unit

interface FrequencyDetector {
    fun listen(listener: FrequencyListener)
    fun stopListening()
}