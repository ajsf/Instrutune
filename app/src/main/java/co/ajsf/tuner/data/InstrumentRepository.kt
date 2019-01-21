package co.ajsf.tuner.data

import android.content.SharedPreferences
import androidx.core.content.edit
import co.ajsf.tuner.model.Instrument
import co.ajsf.tuner.model.InstrumentFactory

private const val SELECTED_INSTRUMENT = "SELECTED_INSTRUMENT "

class InstrumentRepository(private val prefs: SharedPreferences) {

    private val instruments = listOf(InstrumentFactory.GUITAR, InstrumentFactory.BASS)

    fun getInstruments() = instruments

    fun saveSelectedInstrument(instrumentName: String) = prefs.edit {
        putString(SELECTED_INSTRUMENT, instrumentName)
    }

    fun getSelectedInstrument(): Instrument {
        val instrumentName = prefs.getString(SELECTED_INSTRUMENT, "")
        return getInstrumentByName(instrumentName) ?: InstrumentFactory.GUITAR
    }

    private fun getInstrumentByName(instrumentName: String?): Instrument? = instruments
        .find { it.name == instrumentName }

}