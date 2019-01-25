package co.ajsf.tuner.tuner

import co.ajsf.tuner.model.Instrument
import co.ajsf.tuner.tuner.frequencydetection.FrequencyDetector
import co.ajsf.tuner.tuner.notefinder.NoteFinder
import co.ajsf.tuner.tuner.notefinder.model.mapToMusicalNoteList
import io.reactivex.Flowable

data class SelectedStringInfo(val number: Int, val delta: Float)

class Tuner(frequencyDetector: FrequencyDetector) {

    private val audioFeed: Flowable<Float> = frequencyDetector.listen()
    private val chromaticNoteFinder: NoteFinder = NoteFinder.chromaticNoteFinder()
    private var instrumentNoteFinder: NoteFinder? = null

    val instrumentTuning: Flowable<SelectedStringInfo> = audioFeed
        .filter { instrumentNoteFinder != null }
        .map { instrumentNoteFinder?.findNote(it) }
        .map { SelectedStringInfo(it.number, it.delta.toFloat()) }

    val mostRecentFrequency: Flowable<String> = audioFeed
        .filter { it != -1f }
        .map { String.format("%.2f", it) }

    val mostRecentNoteName: Flowable<String> = audioFeed
        .filter { it != -1f }
        .map { chromaticNoteFinder.findNote(it) }
        .map { it.name }

    fun setInstrument(instrument: Instrument) {
        instrumentNoteFinder = NoteFinder.instrumentNoteFinder(instrument.mapToMusicalNoteList())
    }
}