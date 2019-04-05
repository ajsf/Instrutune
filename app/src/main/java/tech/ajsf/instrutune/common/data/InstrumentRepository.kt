package tech.ajsf.instrutune.common.data

import android.content.SharedPreferences
import androidx.core.content.edit
import tech.ajsf.instrutune.common.data.db.InstrumentDao
import tech.ajsf.instrutune.common.model.Instrument
import tech.ajsf.instrutune.common.model.InstrumentCategory
import io.reactivex.Scheduler

private const val SELECTED_CATEGORY = "SELECTED_CATEGORY"
private const val SELECTED_TUNING = "SELECTED_TUNING"
private const val OFFSET = "OFFSET"

interface InstrumentRepository {
    fun getTunings(): List<Instrument>
    fun getInstrumentList(): List<String> = InstrumentCategory.values().map { it.toString() }
    fun saveSelectedTuning(tuningName: String)
    fun saveSelectedCategory(categoryName: String)
    fun getSelectedTuning(): Instrument
    fun getSelectedCategory(): String
    fun saveOffset(offset: Int)
    fun getOffset(): Int
}

class InstrumentRepositoryImpl(
    private val prefs: SharedPreferences,
    private val instrumentDao: InstrumentDao,
    private val scheduler: Scheduler
) : InstrumentRepository {

    private var tuningOffset: Int = prefs.getInt(OFFSET, 0)

    override fun getTunings(): List<Instrument> {
        val category = getSelectedCategory()
        return fetchAndMapInstruments().filter { it.category.toString() == category }
    }

    private fun fetchAndMapInstruments(): List<Instrument> = instrumentDao
        .getAllInstruments()
        .map {
            println("--------------Entity: $it------------")
            InstrumentFactory.buildInstrumentsFromEntities(it, tuningOffset)
        }
        .subscribeOn(scheduler)
        .blockingGet()

    override fun saveSelectedTuning(tuningName: String) = prefs.edit {
        putString(SELECTED_TUNING, tuningName)
    }

    override fun saveSelectedCategory(categoryName: String) = prefs.edit {
        putString(SELECTED_CATEGORY, categoryName)
    }

    override fun getSelectedTuning(): Instrument {
        val category = getSelectedCategory()
        val tuningName = prefs.getString(SELECTED_TUNING, "Standard")
        println("------------_Getting: $category, $tuningName")
        return getInstrument(category, tuningName)
            ?: throw RuntimeException("Tuning Not Available. Category: $category, Name: $tuningName")
    }

    override fun getSelectedCategory(): String = prefs.getString(SELECTED_CATEGORY, "Guitar") ?: "Guitar"

    override fun saveOffset(offset: Int) {
        prefs.edit { putInt(OFFSET, offset) }
        tuningOffset = offset
    }

    override fun getOffset() = tuningOffset

    private fun getInstrument(category: String, tuningName: String?): Instrument? = fetchAndMapInstruments()
        .onEach { println(it) }
        .find { it.category.toString() == category && it.tuningName == tuningName }
}