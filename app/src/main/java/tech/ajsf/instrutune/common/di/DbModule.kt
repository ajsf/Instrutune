package tech.ajsf.instrutune.common.di

import tech.ajsf.instrutune.common.data.db.InstrumentDao
import tech.ajsf.instrutune.common.data.db.TunerDatabase
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

fun dbModule() = Kodein.Module("dbModule") {
    bind<InstrumentDao>() with provider { TunerDatabase.getInstance(instance()).instrumentDao() }
}