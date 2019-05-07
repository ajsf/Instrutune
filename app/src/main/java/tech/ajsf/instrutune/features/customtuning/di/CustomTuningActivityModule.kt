package tech.ajsf.instrutune.features.customtuning.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import tech.ajsf.instrutune.common.viewmodel.ViewModelFactory
import tech.ajsf.instrutune.features.customtuning.CustomTuningViewModel

fun customTuningActivityModule() = Kodein.Module("customTuningActivityModule") {
    bind<ViewModel>(tag = CustomTuningViewModel::class.java.simpleName) with provider {
        CustomTuningViewModel(instance(), instance())
    }
    bind<ViewModelProvider.Factory>() with singleton {
        ViewModelFactory(kodein.direct)
    }
}