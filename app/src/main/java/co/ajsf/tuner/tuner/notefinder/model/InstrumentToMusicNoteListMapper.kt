package co.ajsf.tuner.tuner.notefinder.model

import co.ajsf.tuner.model.Instrument

fun Instrument.mapToMusicalNoteList(): List<MusicalNote> = strings
    .mapIndexed { index, instrument ->
        MusicalNote.fromFloat(instrument.freq, instrument.name, index)
    }.sortedBy { it.freq }