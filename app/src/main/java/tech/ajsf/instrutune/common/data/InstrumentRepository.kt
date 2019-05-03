package tech.ajsf.instrutune.common.data

import android.content.SharedPreferences
import androidx.core.content.edit
import io.reactivex.Completable
import io.reactivex.Scheduler
import io.reactivex.Single
import tech.ajsf.instrutune.common.data.db.InstrumentDao
import tech.ajsf.instrutune.common.data.db.InstrumentEntity
import tech.ajsf.instrutune.common.data.mapper.InstrumentMapper
import tech.ajsf.instrutune.common.model.Instrument
import tech.ajsf.instrutune.common.model.InstrumentCategory

private const val SELECTED_TUNING = "SELECTED_TUNING"
private const val OFFSET = "OFFSET"

interface InstrumentRepository {
    fun getAllTunings(): Single<List<Instrument>>
    fun getTuningsForCategory(category: String): Single<List<Instrument>>
    fun getTuningById(id: Int): Single<Instrument>
    fun saveTuning(name: String, notes: List<String>, id: Int?): Single<Int>
    fun deleteTuning(id: Int): Completable
    fun getCategories(): Single<List<String>>
    fun getSelectedTuning(): Single<Instrument>
    fun saveSelectedTuning(tuningId: Int)
    fun getOffset(): Int
    fun saveOffset(offset: Int)
}

class InstrumentRepositoryImpl(
    private val prefs: SharedPreferences,
    private val instrumentDao: InstrumentDao,
    private val mapper: InstrumentMapper,
    private val scheduler: Scheduler
) : InstrumentRepository {

    override fun getAllTunings(): Single<List<Instrument>> = instrumentDao
        .getAllInstruments()
        .map { mapper.toInstrumentList(it) }
        .subscribeOn(scheduler)

    override fun getTuningsForCategory(category: String): Single<List<Instrument>> {
        return instrumentDao.getInstrumentsForCategory(category)
            .map { mapper.toInstrumentList(it) }
            .subscribeOn(scheduler)
    }

    override fun getTuningById(id: Int): Single<Instrument> = getInstrument(id)

    override fun saveTuning(name: String, notes: List<String>, id: Int?): Single<Int> {
        val custom = InstrumentCategory.Custom.toString()
        val entity = InstrumentEntity(name, custom, notes, id)

        return instrumentDao
            .insert(entity)
            .map { it.toInt() }
            .subscribeOn(scheduler)
    }

    override fun deleteTuning(id: Int): Completable = instrumentDao
        .getInstrumentById(id)
        .map {
            if (it.category == InstrumentCategory.Custom.toString()) {
                instrumentDao.deleteInstrumentById(id)
            } else {
                throw RuntimeException("Only custom tunings can be deleted.")
            }
        }.ignoreElement()
        .subscribeOn(scheduler)

    override fun getCategories(): Single<List<String>> = instrumentDao
        .getInstrumentsForCategory(InstrumentCategory.Custom.toString())
        .map { customTunings ->
            val list = InstrumentCategory.values().map { it.toString() }
            if (customTunings.isNullOrEmpty()) {
                list.filter { it != InstrumentCategory.Custom.toString() }
            } else list
        }
        .subscribeOn(scheduler)

    override fun getSelectedTuning(): Single<Instrument> {
        val tuningId = prefs.getInt(SELECTED_TUNING, -1)
        return if (tuningId > -1) {
            getInstrument(tuningId)
        } else {
            instrumentDao.getAllInstruments()
                .map { it.first() }
                .map { mapper.toInstrument(it) }
                .subscribeOn(scheduler)
        }
    }

    override fun saveSelectedTuning(tuningId: Int) = prefs.edit {
        putInt(SELECTED_TUNING, tuningId)
    }

    override fun getOffset() = prefs.getInt(OFFSET, 0)

    override fun saveOffset(offset: Int) {
        prefs.edit { putInt(OFFSET, offset) }
    }

    private fun getInstrument(id: Int): Single<Instrument> = instrumentDao
        .getInstrumentById(id)
        .map { mapper.toInstrument(it) }
        .subscribeOn(scheduler)
}