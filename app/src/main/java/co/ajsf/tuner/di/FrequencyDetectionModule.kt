package co.ajsf.tuner.di

import co.ajsf.tuner.frequencydetection.DetectionEngine
import co.ajsf.tuner.frequencydetection.FrequencyDetector
import co.ajsf.tuner.frequencydetection.tarsos.TarsosDetectionEngine
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

fun frequencyDetectionModule() = Kodein.Module("frequencyDetectionModule") {

    import(tarsosModule())

    bind<FrequencyDetector>() with provider { FrequencyDetector(instance()) }

    bind<DetectionEngine>() with provider {
        TarsosDetectionEngine(instance(), instance())
    }
}