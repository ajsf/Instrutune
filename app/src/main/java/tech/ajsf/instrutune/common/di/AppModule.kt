package tech.ajsf.instrutune.common.di

import android.content.Context
import android.content.SharedPreferences
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.provider

fun appModule(appContext: Context) = Kodein.Module("appModule") {
    bind<Context>() with provider { appContext }
    bind<SharedPreferences>() with provider {
        appContext
            .getSharedPreferences(appContext.packageName, Context.MODE_PRIVATE)
    }
    import(dbModule())
}