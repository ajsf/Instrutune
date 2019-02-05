package co.ajsf.tuner.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.toLiveData
import co.ajsf.tuner.data.InstrumentRepository
import co.ajsf.tuner.model.Instrument
import co.ajsf.tuner.model.toInstrumentInfo
import co.ajsf.tuner.tuner.SelectedStringInfo
import co.ajsf.tuner.tuner.Tuner

data class SelectedInstrumentInfo(val name: String, val noteNames: List<String>, val middleA: String)

class TunerViewModel(private val tuner: Tuner, private val instrumentRepository: InstrumentRepository) :
    ViewModel() {

    val selectedInstrumentInfo: LiveData<SelectedInstrumentInfo>
        get() = _selectedInstrumentInfo

    val selectedStringInfo: LiveData<SelectedStringInfo> = tuner.instrumentTuning.toLiveData()

    val mostRecentFrequency: LiveData<String> = tuner.mostRecentFrequency.toLiveData()

    val mostRecentNoteName: LiveData<String> = tuner.mostRecentNoteInfo.map { it.name }.toLiveData()

    val mostRecentNoteDelta: LiveData<Int> = tuner.mostRecentNoteInfo.map { it.delta }.toLiveData()

    private val selectedInstrument = MutableLiveData<Instrument>()
    private val _selectedInstrumentInfo = MutableLiveData<SelectedInstrumentInfo>()

    init {
        selectedInstrument.observeForever {
            it?.let { instrument ->
                tuner.setInstrument(instrument)
                _selectedInstrumentInfo.postValue(instrument.toInstrumentInfo(getMiddleAFreq()))
            }
        }
        setupTuner()
    }

    fun getInstruments(): List<String> {
        return instrumentRepository.getInstrumentList()
    }

    fun getTunings(): List<Instrument> {
        return instrumentRepository.getTunings()
    }

    fun saveSelectedCategory(categoryName: String) {
        instrumentRepository.saveSelectedCategory(categoryName)
    }

    fun saveSelectedTuning(tuningName: String) {
        instrumentRepository.saveSelectedTuning(tuningName)
        getSelectedInstrument()
    }

    fun saveOffset(offset: Int) {
        instrumentRepository.saveOffset(offset)
        setupTuner()
    }

    fun getOffset() = instrumentRepository.getOffset()

    private fun setupTuner() {
        tuner.setOffset(instrumentRepository.getOffset())
        getSelectedInstrument()
    }

    private fun getSelectedInstrument() {
        val instrument = instrumentRepository.getSelectedTuning()
        selectedInstrument.postValue(instrument)
    }

    private fun getMiddleAFreq(): Int = 440 + getOffset()
}