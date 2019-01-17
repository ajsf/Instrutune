package co.ajsf.tuner.di

import be.tarsos.dsp.io.android.AudioDispatcherFactory
import be.tarsos.dsp.pitch.PitchDetectionHandler
import be.tarsos.dsp.pitch.PitchProcessor
import co.ajsf.tuner.frequencydetection.DetectionEngine
import co.ajsf.tuner.frequencydetection.FrequencyDetector
import co.ajsf.tuner.mapper.TarsosResponseToModelMapper
import co.ajsf.tuner.mapper.mapTarsosResponseToDetectionResult
import co.ajsf.tuner.tarsos.*
import io.reactivex.schedulers.Schedulers
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

fun frequencyDetectionModule() = Kodein
    .Module("frequencyDetectionModule") {
        val sampleRate = 44100
        val bufferSize = 2048

        bind<FrequencyDetector>() with provider { FrequencyDetector(instance()) }

        bind<DetectionEngine>() with provider {
            TarsosDetectionEngine(instance(), instance())
        }

        bind<PitchProcessorBuilder>() with provider {
            { pdh: PitchDetectionHandler ->
                println("Detecter: building pitch processor")
                PitchProcessor(
                    PitchProcessor.PitchEstimationAlgorithm.MPM, sampleRate.toFloat(),
                    bufferSize, pdh
                )
            }
        }

        bind<TarsosResponseToModelMapper>() with provider { ::mapTarsosResponseToDetectionResult }

        bind<AudioDispatcherBuilder>() with provider {
            {
                println("Detecter: Building audio dispatcher")
                AudioDispatcherFactory.fromDefaultMicrophone(
                    sampleRate, bufferSize, 1024
                )
            }
        }

        bind<TarsosDispatcher>() with provider {
            TarsosDispatcherImpl(instance(), instance())
        }

        bind<TarsosResponseStream>() with provider {
            TarsosFlowable(instance(), Schedulers.io())
        }
    }