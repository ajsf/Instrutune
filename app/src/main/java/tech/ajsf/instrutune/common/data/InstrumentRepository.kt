package tech.ajsf.instrutune.common.data

import android.content.SharedPreferences
import androidx.core.content.edit
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
    fun getAllTunings(): Map<InstrumentCategory, List<Instrument>>
    fun getTuningsForCategory(category: String): List<Instrument>
    fun getTuningById(id: Int): Instrument
    fun saveTuning(tuningName: String, numberedNotes: List<String>, id: Int?): Int
    fun deleteTuning(id: Int)
    fun getInstrumentList(): List<String>
    fun saveSelectedTuning(tuningId: Int)
    fun getSelectedTuning(): Instrument
    fun saveOffset(offset: Int)
    fun getOffset(): Int
}

class InstrumentRepositoryImpl(
    private val prefs: SharedPreferences,
    private val instrumentDao: InstrumentDao,
    private val scheduler: Scheduler
) : InstrumentRepository {

    private var tuningOffset: Int = prefs.getInt(OFFSET, 0)

    override fun getAllTunings(): Map<InstrumentCategory, List<Instrument>> {
        val instruments = instrumentDao.getAllInstruments().mapAndGet()
        return instruments.groupBy { it.category }
    }

    override fun getTuningsForCategory(category: String): List<Instrument> {
        return instrumentDao.getInstrumentsForCategory(category).mapAndGet()
    }

    override fun getTuningById(id: Int): Instrument {
        return getInstrument(id)
    }

    override fun saveTuning(tuningName: String, numberedNotes: List<String>, id: Int?): Int {
        return instrumentDao
            .insert(
                InstrumentEntity(
                    tuningName,
                    InstrumentCategory.Custom.toString(),
                    numberedNotes,
                    id
                )
            )
            .subscribeOn(scheduler)
            .blockingGet().toInt()
    }

    override fun deleteTuning(id: Int) {
        val instrument = instrumentDao.getInstrumentById(id).subscribeOn(scheduler).blockingGet()
        if (instrument.category == InstrumentCategory.Custom.toString()) {
            instrumentDao.deleteInstrumentById(id).subscribeOn(scheduler).subscribe()
        } else {
            throw RuntimeException("Only custom tunings can be deleted.")
        }
    }

    override fun getInstrumentList(): List<String> {
        val list = InstrumentCategory.values().map { it.toString() }
        val customTunings = instrumentDao
            .getInstrumentsForCategory(InstrumentCategory.Custom.toString())
            .subscribeOn(scheduler)
            .blockingGet()
        return if (customTunings.isNullOrEmpty()) list.filter { it != InstrumentCategory.Custom.toString() } else list
    }

    override fun saveSelectedTuning(tuningId: Int) = prefs.edit {
        putInt(SELECTED_TUNING, tuningId)
    }

    override fun getSelectedTuning(): Instrument {
        val tuningId = prefs.getInt(SELECTED_TUNING, -1)
        return if (tuningId > -1) {
            getInstrument(tuningId)
        } else {
            instrumentDao.getAllInstruments().mapAndGet().first()
        }
    }

    override fun saveOffset(offset: Int) {
        prefs.edit { putInt(OFFSET, offset) }
        tuningOffset = offset
    }

    override fun getOffset() = tuningOffset

    private fun getInstrument(id: Int): Instrument = instrumentDao.getInstrumentById(id)
        .map { it.buildInstrument(tuningOffset) }
        .subscribeOn(scheduler)
        .blockingGet()

    private fun Single<List<InstrumentEntity>>.mapAndGet(): List<Instrument> = map {
        InstrumentFactory.buildInstrumentsFromEntities(it, tuningOffset)
    }.subscribeOn(scheduler).blockingGet()
}