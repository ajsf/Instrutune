package tech.ajsf.instrutune.common.tuner

import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import tech.ajsf.instrutune.common.model.Instrument
import tech.ajsf.instrutune.common.tuner.frequencydetection.FrequencyDetector
import tech.ajsf.instrutune.common.tuner.notefinder.NoteFinder
import tech.ajsf.instrutune.common.tuner.notefinder.model.MusicalNote

data class SelectedStringInfo(val numberedName: String, val delta: Float)
data class SelectedNoteInfo(val name: String, val delta: Int, val freq: String)

class Tuner(frequencyDetector: FrequencyDetector, scheduler: Scheduler = Schedulers.computation()) {

    private val audioFeed: Flowable<Float> = frequencyDetector
        .listen().observeOn(scheduler)

    private var chromaticNoteFinder: NoteFinder? = null
    private var instrumentNoteFinder: NoteFinder? = null

    val instrumentTuning: Flowable<SelectedStringInfo> = audioFeed
        .filter { instrumentNoteFinder != null }
        .map { instrumentNoteFinder?.findNote(it) }
        .map { SelectedStringInfo(it.numberedName, it.delta.toFloat()) }

    val mostRecentNoteInfo: Flowable<SelectedNoteInfo> = audioFeed
        .filter { chromaticNoteFinder != null }
        .filter { it != -1f }
        .map { it to chromaticNoteFinder!!.findNote(it) }
        .map {
            val name = MusicalNote.nameFromNumberedName(it.second.numberedName)
            val freq = String.format("%.2f", it.first)
            SelectedNoteInfo(name, it.second.delta, freq)
        }

    fun setInstrument(instrument: Instrument) {
        instrumentNoteFinder = NoteFinder.instrumentNoteFinder(instrument)
    }

    fun setOffset(offset: Int) {
        chromaticNoteFinder = NoteFinder.chromaticNoteFinder(offset)
    }
}