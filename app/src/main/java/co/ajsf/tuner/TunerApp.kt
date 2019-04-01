package co.ajsf.tuner

import android.app.Application
import androidx.annotation.VisibleForTesting
import co.ajsf.tuner.common.di.appModule
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware

class TunerApp : Application(), KodeinAware {

    @VisibleForTesting
    var overrideBindings: Kodein.MainBuilder.() -> Unit = {}

    override val kodein = Kodein.lazy {
        import(appModule(this@TunerApp))
    }
}