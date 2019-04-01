package co.ajsf.tuner.features.tuner.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import co.ajsf.tuner.common.data.InstrumentRepository
import co.ajsf.tuner.common.data.InstrumentRepositoryImpl
import co.ajsf.tuner.common.di.dbModule
import co.ajsf.tuner.common.di.frequencyDetectionModule
import co.ajsf.tuner.common.viewmodel.ViewModelFactory
import co.ajsf.tuner.features.tuner.TunerViewModel
import io.reactivex.schedulers.Schedulers
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

fun tunerActivityModule() = Kodein
    .Module("tunerActivityModule") {
        bind<ViewModel>(tag = TunerViewModel::class.java.simpleName) with provider {
            TunerViewModel(instance(), instance())
        }
        bind<ViewModelProvider.Factory>() with singleton {
            ViewModelFactory(kodein.direct)
        }
        bind<InstrumentRepository>() with singleton {
            InstrumentRepositoryImpl(
                instance(),
                instance(),
                Schedulers.io()
            )
        }

        import(frequencyDetectionModule())
        import(dbModule())
    }