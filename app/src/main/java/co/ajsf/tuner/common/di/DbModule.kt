package co.ajsf.tuner.common.di

import co.ajsf.tuner.common.data.db.InstrumentDao
import co.ajsf.tuner.common.data.db.TunerDatabase
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

fun dbModule() = Kodein.Module("dbModule") {
    bind<InstrumentDao>() with provider { TunerDatabase.getInstance(instance()).instrumentDao() }
}