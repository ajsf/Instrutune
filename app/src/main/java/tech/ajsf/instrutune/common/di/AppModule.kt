package tech.ajsf.instrutune.common.di

import android.content.Context
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.provider

fun appModule(appContext: Context) = Kodein.Module("appModule") {
    bind<Context>() with provider { appContext }
}