package tech.ajsf.instrutune.common.di

import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import tech.ajsf.instrutune.common.tuner.Tuner
import tech.ajsf.instrutune.common.tuner.frequencydetection.FrequencyDetector
import tech.ajsf.instrutune.common.tuner.frequencydetection.FrequencyDetectorImpl
import tech.ajsf.instrutune.common.tuner.frequencydetection.detector.DetectionEngine
import tech.ajsf.instrutune.common.tuner.frequencydetection.tarsos.TarsosDetectionEngine

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