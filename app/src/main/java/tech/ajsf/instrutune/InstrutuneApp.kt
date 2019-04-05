package tech.ajsf.instrutune

import android.app.Application
import androidx.annotation.VisibleForTesting
import io.reactivex.schedulers.Schedulers
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import tech.ajsf.instrutune.common.data.db.TunerDatabase
import tech.ajsf.instrutune.common.di.appModule

class InstrutuneApp : Application(), KodeinAware {

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
        import(appModule(this@InstrutuneApp))
    }
}