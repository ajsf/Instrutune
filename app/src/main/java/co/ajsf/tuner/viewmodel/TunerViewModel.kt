package co.ajsf.tuner.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.toLiveData
import co.ajsf.tuner.data.InstrumentRepository
import co.ajsf.tuner.model.Instrument
import co.ajsf.tuner.tuner.Tuner

typealias SelectedStringInfo = Pair<Int, Float>
typealias SelectedInstrumentInfo = Pair<String, List<Char>>

class TunerViewModel(tuner: Tuner, private val instrumentRepository: InstrumentRepository) :
    ViewModel() {

    val selectedInstrumentInfo: LiveData<SelectedInstrumentInfo>
        get() = _selectedInstrumentInfo

    val selectedStringInfo: LiveData<SelectedStringInfo> = tuner.instrumentTuning.toLiveData()

    val mostRecentFrequency: LiveData<String> = tuner.mostRecentFrequency.toLiveData()

    val mostRecentNoteName: LiveData<String> = tuner.mostRecentNoteName.toLiveData()

    private val selectedInstrument = MutableLiveData<Instrument>()
    private val _selectedInstrumentInfo = MutableLiveData<SelectedInstrumentInfo>()

    init {
        selectedInstrument.observeForever { instrument ->
            tuner.setInstrument(instrument)
            val info = instrument.name to instrument.strings.map { it.name.first() }
            _selectedInstrumentInfo.postValue(info)
        }
        getSelectedInstrument()
    }

    fun getInstruments(): List<Instrument> {
        return instrumentRepository.getInstruments()
    }

    fun saveSelectedInstrument(instrumentName: String) {
        instrumentRepository.saveSelectedInstrument(instrumentName)
        getSelectedInstrument()
    }

    private fun getSelectedInstrument() {
        val instrument = instrumentRepository.getSelectedInstrument()
        selectedInstrument.postValue(instrument)
    }
}