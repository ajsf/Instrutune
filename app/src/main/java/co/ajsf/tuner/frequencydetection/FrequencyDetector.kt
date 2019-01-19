package co.ajsf.tuner.frequencydetection

import io.reactivex.Flowable
import java.util.concurrent.TimeUnit

class FrequencyDetector(private val engine: DetectionEngine) {

    fun listen(): Flowable<Float> {

        val micInput = engine.listen()
            .filter { it.pitch > -1.0 && it.probability > 0.85 }

        val noise = micInput
            .filter { it.isSilence.not() }
            .buffer(200, TimeUnit.MILLISECONDS)
            .filter { it.isNotEmpty() }
            .flatMap { detectionResults ->
                Flowable.just(detectionResults.maxBy { it.probability })
            }
            .map { it.pitch }

        val silence = noise
            .debounce(800, TimeUnit.MILLISECONDS)
            .map { -1f }

        return noise.mergeWith(silence).startWith(-1f)
    }
}