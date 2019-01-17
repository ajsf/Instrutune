package co.ajsf.tuner.frequencydetection

import io.reactivex.Flowable

class FrequencyDetector(private val engine: DetectionEngine) {

    fun listen(): Flowable<Float> {

        val micInput = engine.listen().filter { it.pitch > -1.0 }.distinctUntilChanged()

        return micInput.map {
            if (it.isSilence) -1f else it.pitch
        }.distinctUntilChanged()
    }
}