package co.ajsf.tuner.di

import co.ajsf.tuner.tuner.Tuner
import co.ajsf.tuner.tuner.frequencydetection.FrequencyDetector
import co.ajsf.tuner.tuner.frequencydetection.FrequencyDetectorImpl
import co.ajsf.tuner.tuner.frequencydetection.detector.DetectionEngine
import co.ajsf.tuner.tuner.frequencydetection.tarsos.TarsosDetectionEngine
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

fun frequencyDetectionModule() = Kodein.Module("frequencyDetectionModule") {

    import(tarsosModule())

    bind<FrequencyDetector>() with provider { FrequencyDetectorImpl(instance()) }

    bind<DetectionEngine>() with provider {
        TarsosDetectionEngine(instance(), instance())
    }

    bind<Tuner>() with provider {
        Tuner(instance())
    }
}