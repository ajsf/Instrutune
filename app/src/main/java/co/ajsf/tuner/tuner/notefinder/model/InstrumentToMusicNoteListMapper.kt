package co.ajsf.tuner.tuner.notefinder.model

import co.ajsf.tuner.model.Instrument

fun Instrument.mapToMusicalNoteList(): List<MusicalNote> = notes
    .map { MusicalNote(it.freq, it.numberedName) }
    .sortedBy { it.freq }