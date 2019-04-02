package co.ajsf.tuner

import android.app.Application
import androidx.annotation.VisibleForTesting
import co.ajsf.tuner.common.data.db.TunerDatabase
import co.ajsf.tuner.common.di.appModule
import io.reactivex.schedulers.Schedulers
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware

class TunerApp : Application(), KodeinAware {

    @VisibleForTesting
    var overrideBindings: Kodein.MainBuilder.() -> Unit = {}

    override fun onCreate() {
        super.onCreate()
        TunerDatabase.getInstance(this)
            .instrumentDao()
            .getAllInstruments()
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    override val kodein = Kodein.lazy {
        import(appModule(this@TunerApp))
    }
}