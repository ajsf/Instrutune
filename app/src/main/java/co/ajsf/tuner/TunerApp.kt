package co.ajsf.tuner

import android.app.Application
import co.ajsf.tuner.di.appModule
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware

class TunerApp : Application(), KodeinAware {

    override val kodein = Kodein.lazy {
        import(appModule(this@TunerApp))
    }
}