package co.ajsf.tuner.tuner.notefinder.model

import co.ajsf.tuner.model.Instrument

fun Instrument.mapToMusicalNoteList(): List<MusicalNote> = strings
    .map { MusicalNote.fromFloat(it.freq, it.name, it.noteNumber) }
    .sortedBy { it.freq }