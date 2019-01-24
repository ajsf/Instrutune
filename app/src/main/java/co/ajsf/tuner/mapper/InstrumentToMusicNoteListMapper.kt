package co.ajsf.tuner.mapper

import co.ajsf.tuner.model.Instrument
import co.ajsf.tuner.model.MusicalNote

fun Instrument.mapToNoteList() = strings
    .map { MusicalNote.fromFloat(it.freq, it.name) }