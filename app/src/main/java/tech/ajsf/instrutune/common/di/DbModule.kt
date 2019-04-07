package tech.ajsf.instrutune.common.di

import android.content.Context
import android.content.SharedPreferences
import io.reactivex.schedulers.Schedulers
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import tech.ajsf.instrutune.common.data.InstrumentRepository
import tech.ajsf.instrutune.common.data.InstrumentRepositoryImpl
import tech.ajsf.instrutune.common.data.db.InstrumentDao
import tech.ajsf.instrutune.common.data.db.TunerDatabase

fun dataModule() = Kodein.Module("dataModule") {
    bind<InstrumentDao>() with provider { TunerDatabase.getInstance(instance()).instrumentDao() }
    bind<InstrumentRepository>() with singleton {
        InstrumentRepositoryImpl(
            instance(),
            instance(),
            Schedulers.io()
        )
    }
    bind<SharedPreferences>() with provider {
        val context: Context = instance()
        context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
    }
}