package tech.ajsf.instrutune.common.di

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.provider

fun baseActivityModule(activity: AppCompatActivity) = Kodein
    .Module("baseActivityModule") {
        bind<Context>(overrides = true) with provider { activity }
        import(dataModule())
    }