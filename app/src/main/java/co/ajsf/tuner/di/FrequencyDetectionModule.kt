package co.ajsf.tuner.di

import co.ajsf.tuner.frequencydetection.DetectionEngine
import co.ajsf.tuner.frequencydetection.FrequencyDetector
import co.ajsf.tuner.tarsos.TarsosEngineBuilder
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

fun frequencyDetectionModule() = Kodein
    .Module("frequencyDetectionModule") {
        bind<FrequencyDetector>() with provider {
            FrequencyDetector { instance() }
        }
        bind<DetectionEngine>() with provider {
            TarsosEngineBuilder.build(44100, 2048) as DetectionEngine
        }
    }