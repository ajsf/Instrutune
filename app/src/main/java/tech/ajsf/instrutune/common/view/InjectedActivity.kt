package tech.ajsf.instrutune.common.view

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.android.retainedKodein
import tech.ajsf.instrutune.InstrutuneApp
import tech.ajsf.instrutune.common.di.baseActivityModule

abstract class InjectedActivity : AppCompatActivity(), KodeinAware {

    private val appKodein: Kodein by closestKodein()

    override val kodein: Kodein by retainedKodein {
        extend(appKodein)
        import(baseActivityModule(this@InjectedActivity), allowOverride = true)
        import(activityModule())
        (app().overrideBindings)()
    }

    open fun activityModule() = Kodein.Module("activityModule") {}
}

fun Activity.app() = applicationContext as InstrutuneApp