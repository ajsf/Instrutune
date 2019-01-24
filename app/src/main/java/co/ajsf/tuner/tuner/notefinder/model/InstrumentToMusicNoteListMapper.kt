package co.ajsf.tuner.tuner.notefinder.model

import co.ajsf.tuner.model.Instrument

fun Instrument.mapToNoteList() = strings
    .map { MusicalNote.fromFloat(it.freq, it.name) }