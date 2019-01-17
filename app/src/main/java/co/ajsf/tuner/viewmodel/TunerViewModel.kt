package co.ajsf.tuner.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.toLiveData
import co.ajsf.tuner.frequencydetection.FrequencyDetector
import co.ajsf.tuner.model.Instrument
import co.ajsf.tuner.model.InstrumentFactory
import co.ajsf.tuner.model.findClosestString

typealias SelectedStringInfo = Pair<Int, Float>
typealias SelectedInstrumentInfo = Pair<String, List<Char>>

class TunerViewModel(frequencyDetector: FrequencyDetector) : ViewModel() {

    val selectedInstrumentInfo: LiveData<SelectedInstrumentInfo>
        get() = _selectedInstrumentInfo

    val selectedStringInfo: LiveData<SelectedStringInfo> = frequencyDetector.listen()
        .map {
            selectedInstrument.value?.findClosestString(it)
        }.map { it.number to it.delta }.toLiveData()

    private val selectedInstrument = MutableLiveData<Instrument>()
    private val _selectedInstrumentInfo = MutableLiveData<SelectedInstrumentInfo>()

    init {
        selectedInstrument.observeForever { instrument ->
            val info = instrument.name to instrument.strings.map { it.name.first() }
            _selectedInstrumentInfo.postValue(info)
        }
        //_selectedStringInfo.postValue(-1 to 0f)
        selectInstrument(InstrumentFactory.GUITAR)
    }

    private fun selectInstrument(instrument: Instrument) = selectedInstrument
        .postValue(instrument)
}