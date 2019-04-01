package co.ajsf.tuner.common.tuner.notefinder.model

import co.ajsf.tuner.common.model.Instrument

fun Instrument.mapToMusicalNoteList(): List<MusicalNote> = notes
    .map { MusicalNote(it.freq, it.numberedName) }
    .sortedBy { it.freq }