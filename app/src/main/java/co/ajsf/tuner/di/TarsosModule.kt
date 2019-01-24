package co.ajsf.tuner.di

import be.tarsos.dsp.io.android.AudioDispatcherFactory
import be.tarsos.dsp.pitch.PitchDetectionHandler
import be.tarsos.dsp.pitch.PitchProcessor
import co.ajsf.tuner.tuner.frequencydetection.tarsos.*
import co.ajsf.tuner.mapper.TarsosResponseToModelMapper
import co.ajsf.tuner.mapper.mapTarsosResponseToDetectionResult
import io.reactivex.schedulers.Schedulers
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

fun tarsosModule() = Kodein
    .Module("tarsosModule") {
        val sampleRate = 44100
        val bufferSize = 4096

        bind<TarsosResponseStream>() with provider {
            TarsosFlowable(instance(), Schedulers.io())
        }

        bind<TarsosDispatcher>() with provider {
            TarsosDispatcherImpl(instance(), instance())
        }

        bind<DispatcherFactory>() with provider {
            {
                AudioDispatcherFactory
                    .fromDefaultMicrophone(
                        sampleRate, bufferSize, bufferSize / 2
                    )
            }
        }

        bind<PitchProcessorFactory>() with provider {
            { pdh: PitchDetectionHandler ->
                PitchProcessor(
                    PitchProcessor.PitchEstimationAlgorithm.FFT_YIN,
                    sampleRate.toFloat(),
                    bufferSize,
                    pdh
                )
            }
        }

        bind<TarsosResponseToModelMapper>() with provider { ::mapTarsosResponseToDetectionResult }


    }