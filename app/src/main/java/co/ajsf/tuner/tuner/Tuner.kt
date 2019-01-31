package co.ajsf.tuner.tuner

import co.ajsf.tuner.model.Instrument
import co.ajsf.tuner.tuner.frequencydetection.FrequencyDetector
import co.ajsf.tuner.tuner.notefinder.NoteFinder
import co.ajsf.tuner.tuner.notefinder.model.MusicalNote
import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

data class SelectedStringInfo(val numberedName: String, val delta: Float)
data class SelectedNoteInfo(val name: String, val delta: Int)

class Tuner(frequencyDetector: FrequencyDetector, scheduler: Scheduler = Schedulers.computation()) {

    private val audioFeed: Flowable<Float> = frequencyDetector
        .listen().observeOn(scheduler)

    private var chromaticNoteFinder: NoteFinder? = null
    private var instrumentNoteFinder: NoteFinder? = null

    val instrumentTuning: Flowable<SelectedStringInfo> = audioFeed
        .filter { instrumentNoteFinder != null }
        .map { instrumentNoteFinder?.findNote(it) }
        .map { SelectedStringInfo(it.numberedName, it.delta.toFloat()) }

    val mostRecentFrequency: Flowable<String> = audioFeed
        .filter { chromaticNoteFinder != null }
        .filter { it != -1f }
        .map { String.format("%.2f", it) }

    val mostRecentNoteInfo: Flowable<SelectedNoteInfo> = audioFeed
        .filter { chromaticNoteFinder != null }
        .filter { it != -1f }
        .map { chromaticNoteFinder?.findNote(it) }
        .map { SelectedNoteInfo(MusicalNote.nameFromNumberedName(it.numberedName), it.delta) }

    fun setInstrument(instrument: Instrument) {
        instrumentNoteFinder = NoteFinder.instrumentNoteFinder(instrument)
    }

    fun setOffset(offset: Int) {
        chromaticNoteFinder = NoteFinder.chromaticNoteFinder(offset)
    }
}