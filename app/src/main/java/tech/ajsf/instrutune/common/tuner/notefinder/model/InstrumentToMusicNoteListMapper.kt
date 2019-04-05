package tech.ajsf.instrutune.common.tuner.notefinder.model

import tech.ajsf.instrutune.common.model.Instrument

fun Instrument.mapToMusicalNoteList(): List<MusicalNote> = notes
    .map { MusicalNote(it.freq, it.numberedName) }
    .sortedBy { it.freq }