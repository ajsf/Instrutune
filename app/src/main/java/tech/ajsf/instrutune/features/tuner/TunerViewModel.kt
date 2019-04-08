package tech.ajsf.instrutune.features.tuner

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.toLiveData
import tech.ajsf.instrutune.common.data.InstrumentRepository
import tech.ajsf.instrutune.common.model.Instrument
import tech.ajsf.instrutune.common.model.toInstrumentInfo
import tech.ajsf.instrutune.common.tuner.SelectedStringInfo
import tech.ajsf.instrutune.common.tuner.Tuner
import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers

data class SelectedInstrumentInfo(val name: String, val noteNames: List<String>, val middleA: String, val category: String)

class TunerViewModel(
    private val tuner: Tuner,
    private val instrumentRepository: InstrumentRepository,
    private val uiScheduler: Scheduler = AndroidSchedulers.mainThread()
) :
    ViewModel() {

    val selectedInstrumentInfo: LiveData<SelectedInstrumentInfo>
        get() = _selectedInstrumentInfo

    val selectedStringInfo: LiveData<SelectedStringInfo> =
        tuner.instrumentTuning.toUiThreadLiveData()

    val mostRecentFrequency: LiveData<String> =
        tuner.mostRecentFrequency.toUiThreadLiveData()

    val mostRecentNoteName: LiveData<String> =
        tuner.mostRecentNoteInfo.map { it.name }.toUiThreadLiveData()

    val mostRecentNoteDelta: LiveData<Int> =
        tuner.mostRecentNoteInfo.map { it.delta }.toUiThreadLiveData()

    private fun <T> Flowable<T>.toUiThreadLiveData() =
        this.observeOn(uiScheduler).toLiveData()

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

    fun getTuningsForCategory(category: String): List<Instrument> {
        return instrumentRepository.getTuningsForCategory(category)
    }

    fun saveSelectedTuning(tuningId: Int) {
        instrumentRepository.saveSelectedTuning(tuningId)
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