package co.ajsf.tuner.tuner.frequencydetection

import co.ajsf.tuner.tuner.frequencydetection.detector.DetectionEngine
import io.reactivex.Flowable
import java.util.concurrent.TimeUnit

class FrequencyDetector(private val engine: DetectionEngine) {

    fun listen(): Flowable<Float> {

        val micInput = engine.listen()
            .filter { it.pitch > -1.0 }

        val noise = micInput
            .filter { it.isSilence.not() }
            .buffer(250, TimeUnit.MILLISECONDS)
            .filter { it.isNotEmpty() }
            .map { it.groupBy { result -> result.pitch.toInt() } }
            .map { it.maxBy { result -> result.value.size } }
            .map { it.value.maxBy { result -> result.probability } }
            .map { it.pitch }

        val silence = noise
            .debounce(500, TimeUnit.MILLISECONDS)
            .map { -1f }

        return noise.mergeWith(silence).startWith(-1f)
    }

}