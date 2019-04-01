package co.ajsf.tuner.common.di

import co.ajsf.tuner.common.tuner.Tuner
import co.ajsf.tuner.common.tuner.frequencydetection.FrequencyDetector
import co.ajsf.tuner.common.tuner.frequencydetection.FrequencyDetectorImpl
import co.ajsf.tuner.common.tuner.frequencydetection.detector.DetectionEngine
import co.ajsf.tuner.common.tuner.frequencydetection.tarsos.TarsosDetectionEngine
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

fun frequencyDetectionModule() = Kodein.Module("frequencyDetectionModule") {

    import(tarsosModule())

    bind<FrequencyDetector>() with provider {
        FrequencyDetectorImpl(
            instance()
        )
    }

    bind<DetectionEngine>() with provider {
        TarsosDetectionEngine(instance(), instance())
    }

    bind<Tuner>() with provider {
        Tuner(instance())
    }
}