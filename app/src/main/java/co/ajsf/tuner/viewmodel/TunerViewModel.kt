package co.ajsf.tuner.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.toLiveData
import co.ajsf.tuner.data.InstrumentRepository
import co.ajsf.tuner.frequencydetection.FrequencyDetector
import co.ajsf.tuner.frequencydetection.notefinder.ChromaticNoteFinder
import co.ajsf.tuner.model.Instrument
import co.ajsf.tuner.model.NO_STRING
import co.ajsf.tuner.model.findClosestString

typealias SelectedStringInfo = Pair<Int, Float>
typealias SelectedInstrumentInfo = Pair<String, List<Char>>

class TunerViewModel(frequencyDetector: FrequencyDetector, private val instrumentRepository: InstrumentRepository) :
    ViewModel() {

    private val noteFinder = ChromaticNoteFinder()

    val selectedInstrumentInfo: LiveData<SelectedInstrumentInfo>
        get() = _selectedInstrumentInfo

    val selectedStringInfo: LiveData<SelectedStringInfo> = frequencyDetector.listen()
        .map { selectedInstrument.value?.findClosestString(it) ?: NO_STRING }
        .map { it.number to it.delta }.toLiveData()

    val mostRecentFrequency: LiveData<String> = frequencyDetector
        .listen()
        .filter { it != -1f }
        .map { String.format("%.2f", it) }
        .toLiveData()

    val mostRecentNoteName: LiveData<String> = frequencyDetector.listen()
        .filter { it != -1f }
        .map { noteFinder.findNote(it) }
        .toLiveData()

    private val selectedInstrument = MutableLiveData<Instrument>()
    private val _selectedInstrumentInfo = MutableLiveData<SelectedInstrumentInfo>()

    init {
        selectedInstrument.observeForever { instrument ->
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