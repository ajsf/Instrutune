package tech.ajsf.instrutune.common.data

import android.content.SharedPreferences
import androidx.core.content.edit
import io.reactivex.Completable
import io.reactivex.Scheduler
import io.reactivex.Single
import tech.ajsf.instrutune.common.data.db.InstrumentDao
import tech.ajsf.instrutune.common.data.db.InstrumentEntity
import tech.ajsf.instrutune.common.data.db.buildInstrument
import tech.ajsf.instrutune.common.model.Instrument
import tech.ajsf.instrutune.common.model.InstrumentCategory

private const val SELECTED_TUNING = "SELECTED_TUNING"
private const val OFFSET = "OFFSET"

interface InstrumentRepository {
    fun getAllTunings(): Single<List<Instrument>>
    fun getTuningsForCategory(category: String): Single<List<Instrument>>
    fun getTuningById(id: Int): Single<Instrument>
    fun saveTuning(tuningName: String, numberedNotes: List<String>, id: Int?): Single<Int>
    fun deleteTuning(id: Int): Completable
    fun getCategories(): Single<List<String>>
    fun saveSelectedTuning(tuningId: Int)
    fun getSelectedTuning(): Single<Instrument>
    fun saveOffset(offset: Int)
    fun getOffset(): Int
}

class InstrumentRepositoryImpl(
    private val prefs: SharedPreferences,
    private val instrumentDao: InstrumentDao,
    private val scheduler: Scheduler
) : InstrumentRepository {

    private var tuningOffset: Int = prefs.getInt(OFFSET, 0)

    override fun getAllTunings(): Single<List<Instrument>> = instrumentDao
        .getAllInstruments()
        .map { InstrumentFactory.buildInstrumentsFromEntities(it, tuningOffset) }
        .subscribeOn(scheduler)

    override fun getTuningsForCategory(category: String): Single<List<Instrument>> {
        return instrumentDao.getInstrumentsForCategory(category)
            .map { InstrumentFactory.buildInstrumentsFromEntities(it, tuningOffset) }
            .subscribeOn(scheduler)
    }

    override fun getTuningById(id: Int): Single<Instrument> {
        return getInstrument(id).subscribeOn(scheduler)
    }

    override fun saveTuning(
        tuningName: String,
        numberedNotes: List<String>,
        id: Int?
    ): Single<Int> {
        val custom = InstrumentCategory.Custom.toString()

        return instrumentDao
            .insert(InstrumentEntity(tuningName, custom, numberedNotes, id))
            .map { it.toInt() }
            .subscribeOn(scheduler)
    }

    override fun deleteTuning(id: Int): Completable {
        return instrumentDao
            .getInstrumentById(id)
            .map {
                if (it.category == InstrumentCategory.Custom.toString()) {
                    instrumentDao.deleteInstrumentById(id)
                } else {
                    throw RuntimeException("Only custom tunings can be deleted.")
                }
            }.ignoreElement()
            .subscribeOn(scheduler)
    }

    override fun getCategories(): Single<List<String>> = instrumentDao
        .getInstrumentsForCategory(InstrumentCategory.Custom.toString())
        .map { customTunings ->
            val list = InstrumentCategory.values().map { it.toString() }
            if (customTunings.isNullOrEmpty()) {
                list.filter { it != InstrumentCategory.Custom.toString() }
            } else list
        }
        .subscribeOn(scheduler)

    override fun saveSelectedTuning(tuningId: Int) = prefs.edit {
        putInt(SELECTED_TUNING, tuningId)
    }

    override fun getSelectedTuning(): Single<Instrument> {
        val tuningId = prefs.getInt(SELECTED_TUNING, -1)
        return if (tuningId > -1) {
            getInstrument(tuningId).subscribeOn(scheduler)
        } else {
            instrumentDao.getAllInstruments()
                .map { InstrumentFactory.buildInstrumentsFromEntities(it, tuningOffset) }
                .map { it.first() }
                .subscribeOn(scheduler)
        }
    }

    override fun saveOffset(offset: Int) {
        prefs.edit { putInt(OFFSET, offset) }
        tuningOffset = offset
    }

    override fun getOffset() = tuningOffset

    private fun getInstrument(id: Int): Single<Instrument> = instrumentDao
        .getInstrumentById(id)
        .map { it.buildInstrument(tuningOffset) }
        .subscribeOn(scheduler)
}