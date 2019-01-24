package co.ajsf.tuner.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.toLiveData
import co.ajsf.tuner.data.InstrumentRepository
import co.ajsf.tuner.frequencydetection.FrequencyDetector
import co.ajsf.tuner.frequencydetection.notefinder.NO_NOTE
import co.ajsf.tuner.frequencydetection.notefinder.NoteFinder
import co.ajsf.tuner.mapper.mapToNoteList
import co.ajsf.tuner.model.Instrument

typealias SelectedStringInfo = Pair<Int, Float>
typealias SelectedInstrumentInfo = Pair<String, List<Char>>

class TunerViewModel(frequencyDetector: FrequencyDetector, private val instrumentRepository: InstrumentRepository) :
    ViewModel() {

    private val audioFeed = frequencyDetector.listen()
    private val chromaticNoteFinder = NoteFinder.chromaticNoteFinder()

    private var instrumentNoteFinder: NoteFinder? = null

    val selectedInstrumentInfo: LiveData<SelectedInstrumentInfo>
        get() = _selectedInstrumentInfo

    val selectedStringInfo: LiveData<SelectedStringInfo> = audioFeed
        .map { instrumentNoteFinder?.findNote(it) ?: NO_NOTE }
        .map { it.number to it.delta.toFloat() }.toLiveData()

    val mostRecentFrequency: LiveData<String> = audioFeed
        .filter { it != -1f }
        .map { String.format("%.2f", it) }
        .toLiveData()

    val mostRecentNoteName: LiveData<String> = audioFeed
        .filter { it != -1f }
        .map { chromaticNoteFinder.findNote(it) }
        .map { it.name }
        .toLiveData()

    private val selectedInstrument = MutableLiveData<Instrument>()
    private val _selectedInstrumentInfo = MutableLiveData<SelectedInstrumentInfo>()

    init {
        selectedInstrument.observeForever { instrument ->
            instrumentNoteFinder = NoteFinder.instrumentNoteFinder(instrument.mapToNoteList())
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