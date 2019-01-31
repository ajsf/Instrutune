package co.ajsf.tuner.data

import android.content.SharedPreferences
import androidx.core.content.edit
import co.ajsf.tuner.model.Instrument
import co.ajsf.tuner.model.InstrumentCategory
import co.ajsf.tuner.model.InstrumentFactory

private const val SELECTED_CATEGORY = "SELECTED_CATEGORY"
private const val SELECTED_TUNING = "SELECTED_TUNING"
private const val OFFSET = "OFFSET"

class InstrumentRepository(private val prefs: SharedPreferences) {

    private var tuningOffset: Int = prefs.getInt(OFFSET, 0)
    private var instruments: List<Instrument> = InstrumentFactory.getAllInstruments(tuningOffset)

    fun getTunings(instrument: String): List<Instrument> = instruments.filter { it.category.toString() == instrument }

    fun getInstrumentList(): List<String> = InstrumentCategory.values().map { it.toString() }

    fun saveSelectedTuning(tuningName: String) = prefs.edit {
        putString(SELECTED_TUNING, tuningName)
    }

    fun saveSelectedCategory(categoryName: String) = prefs.edit {
        putString(SELECTED_CATEGORY, categoryName)
    }

    fun getSelectedTuning(): Instrument {
        val category = getSelectedCategory()
        val tuningName = prefs.getString(SELECTED_TUNING, "")
        return getInstrument(category, tuningName) ?: InstrumentFactory
            .getDefaultInstrumentForCategory(category, tuningOffset)
    }

    fun getSelectedCategory(): String = prefs.getString(SELECTED_CATEGORY, "Guitar") ?: "Guitar"

    fun saveOffset(offset: Int) {
        prefs.edit {
            putInt(OFFSET, offset)
        }
        tuningOffset = offset
        instruments = InstrumentFactory.getAllInstruments(tuningOffset)
    }

    fun getOffset() = tuningOffset

    private fun getInstrument(category: String, tuningName: String?): Instrument? = instruments
        .find { it.category.toString() == category && it.tuningName == tuningName }
}