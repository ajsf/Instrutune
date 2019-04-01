package co.ajsf.tuner.common.data

import android.content.SharedPreferences
import androidx.core.content.edit
import co.ajsf.tuner.common.model.Instrument
import co.ajsf.tuner.common.model.InstrumentCategory

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

class InstrumentRepositoryImpl(private val prefs: SharedPreferences) : InstrumentRepository {

    private var tuningOffset: Int = prefs.getInt(OFFSET, 0)
    private var instruments: List<Instrument> = InstrumentFactory.getAllInstruments(tuningOffset)

    override fun getTunings(): List<Instrument> {
        val category = getSelectedCategory()
        return instruments.filter { it.category.toString() == category }
    }

    override fun saveSelectedTuning(tuningName: String) = prefs.edit {
        putString(SELECTED_TUNING, tuningName)
    }

    override fun saveSelectedCategory(categoryName: String) = prefs.edit {
        putString(SELECTED_CATEGORY, categoryName)
    }

    override fun getSelectedTuning(): Instrument {
        val category = getSelectedCategory()
        val tuningName = prefs.getString(SELECTED_TUNING, "")
        return getInstrument(category, tuningName) ?: InstrumentFactory
            .getDefaultInstrumentForCategory(category, tuningOffset)
    }

    override fun getSelectedCategory(): String = prefs.getString(SELECTED_CATEGORY, "Guitar") ?: "Guitar"

    override fun saveOffset(offset: Int) {
        prefs.edit {
            putInt(OFFSET, offset)
        }
        tuningOffset = offset
        instruments = InstrumentFactory.getAllInstruments(tuningOffset)
    }

    override fun getOffset() = tuningOffset

    private fun getInstrument(category: String, tuningName: String?): Instrument? = instruments
        .find { it.category.toString() == category && it.tuningName == tuningName }
}