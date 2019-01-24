package co.ajsf.tuner.tuner

import co.ajsf.tuner.mapper.mapToNoteList
import co.ajsf.tuner.model.Instrument
import co.ajsf.tuner.tuner.frequencydetection.FrequencyDetector
import co.ajsf.tuner.tuner.frequencydetection.notefinder.NO_NOTE
import co.ajsf.tuner.tuner.frequencydetection.notefinder.NoteFinder
import io.reactivex.Flowable

class Tuner(frequencyDetector: FrequencyDetector) {

    private val audioFeed = frequencyDetector.listen()
    private val chromaticNoteFinder = NoteFinder.chromaticNoteFinder()
    private var instrumentNoteFinder: NoteFinder? = null

    val instrumentTuning: Flowable<Pair<Int, Float>> = audioFeed
        .map { instrumentNoteFinder?.findNote(it) ?: NO_NOTE }
        .map { it.number to it.delta.toFloat() }

    val mostRecentFrequency: Flowable<String> = audioFeed
        .filter { it != -1f }
        .map { String.format("%.2f", it) }

    val mostRecentNoteName: Flowable<String> = audioFeed
        .filter { it != -1f }
        .map { chromaticNoteFinder.findNote(it) }
        .map { it.name }

    fun setInstrument(instrument: Instrument) {
        instrumentNoteFinder = NoteFinder.instrumentNoteFinder(instrument.mapToNoteList())
    }
}