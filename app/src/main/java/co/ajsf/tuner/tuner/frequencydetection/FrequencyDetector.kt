package co.ajsf.tuner.tuner.frequencydetection

import co.ajsf.tuner.tuner.frequencydetection.detector.DetectionEngine
import io.reactivex.Flowable
import java.util.concurrent.TimeUnit

class FrequencyDetector(private val engine: DetectionEngine) {

    fun listen(): Flowable<Float> {

        val micInput = engine.listen()
            .filter { it.pitch > -1.0 }

        val noise = micInput
            .buffer(200, TimeUnit.MILLISECONDS)
            .filter { it.isNotEmpty() }
            .map { it.groupBy { result -> result.pitch.roundToTen() } }
            .map { it.maxBy { result -> result.value.size } }
            .map { it.value }
            .filter { it.size > 2 }
            .flatMap { Flowable.just(it.maxBy { result -> result.probability }) }
            .map { it.pitch }

        val silence = noise
            .debounce(600, TimeUnit.MILLISECONDS)
            .map { -1f }

        return noise.mergeWith(silence).startWith(-1f)
    }

    private fun Float.roundToTen(): Int = Math.round(this / 10) * 10
}