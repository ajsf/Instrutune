package co.ajsf.tuner.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import co.ajsf.tuner.frequencyDetection.FrequencyDetector
import co.ajsf.tuner.model.Instrument
import co.ajsf.tuner.model.InstrumentFactory
import co.ajsf.tuner.model.findClosestString

typealias SelectedStringInfo = Pair<Int, Int>
typealias SelectedInstrumentInfo = Pair<String, List<Char>>

class TunerViewModel : ViewModel() {

    val selectedInstrumentInfo: LiveData<SelectedInstrumentInfo>
        get() = _selectedInstrumentInfo

    val selectedStringInfo: LiveData<SelectedStringInfo>
        get() = _selectedStringInfo

    private val selectedInstrument = MutableLiveData<Instrument>()
    private val _selectedInstrumentInfo = MutableLiveData<SelectedInstrumentInfo>()
    private val _selectedStringInfo = MutableLiveData<SelectedStringInfo>()

    private val frequencyDetector = FrequencyDetector { FrequencyDetector.engineBuilder() }

    init {
        selectedInstrument.observeForever { instrument ->
            val info = instrument.name to instrument.strings.map { it.name.first() }
            _selectedInstrumentInfo.postValue(info)
        }
        _selectedStringInfo.postValue(-1 to 0)
        selectInstrument(InstrumentFactory.GUITAR)
    }

    fun listenForFrequencies() {
        frequencyDetector.listen { freq ->
            val detectedString = selectedInstrument.value?.findClosestString(freq)
            detectedString?.let {
                _selectedStringInfo.postValue(it.number to it.delta)
            }
        }
    }

    fun stopListening() = frequencyDetector.stopListening()

    private fun selectInstrument(instrument: Instrument) = selectedInstrument
        .postValue(instrument)
}