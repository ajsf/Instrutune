package tech.ajsf.instrutune.common.tuner.notefinder.model

import kotlin.math.pow

data class ChromaticOctave(private val number: Int = 4, private val centerA: Int = 440) {

    private val aFreq: Float = centerA * 2.0.pow(number - 4).toFloat()
    private val aNote = MusicalNote.fromFloatAndName(
        aFreq,
        "A",
        (number * 12) + 1
    )

    init {
        if ((number in (0..8)).not()) throw IllegalArgumentException("Invalid octave number: $number. It must be in the range of 0 to 8.")
    }

    val notes = listOf(
        MusicalNote.fromName(
            aNote.relativeFreq(-9),
            "C",
            (number * 12) - 8
        ),
        MusicalNote.fromName(
            aNote.relativeFreq(-8),
            "C#",
            (number * 12) - 7
        ),
        MusicalNote.fromName(
            aNote.relativeFreq(-7),
            "D",
            (number * 12) - 6
        ),
        MusicalNote.fromName(
            aNote.relativeFreq(-6),
            "D#",
            (number * 12) - 5
        ),
        MusicalNote.fromName(
            aNote.relativeFreq(-5),
            "E",
            (number * 12) - 4
        ),
        MusicalNote.fromName(
            aNote.relativeFreq(-4),
            "F",
            (number * 12) - 3
        ),
        MusicalNote.fromName(
            aNote.relativeFreq(-3),
            "F#",
            (number * 12) - 2
        ),
        MusicalNote.fromName(
            aNote.relativeFreq(-2),
            "G",
            (number * 12) - 1
        ),
        MusicalNote.fromName(
            aNote.relativeFreq(-1),
            "G#",
            number * 12
        ),
        aNote,
        MusicalNote.fromName(
            aNote.relativeFreq(1),
            "A#",
            (number * 12) + 2
        ),
        MusicalNote.fromName(
            aNote.relativeFreq(2),
            "B",
            (number * 12) + 3
        )
    )

    override fun toString(): String {
        return notes.joinToString {
            "${it.numberedName}-${String.format("%.3f", it.floatFreq)} Hz"
        }
    }

    companion object {
        fun createFullRange(offset: Int = 0): List<MusicalNote> = (0..8)
            .map { ChromaticOctave(it, 440 + offset) }
            .flatMap { it.notes }

        fun noteNames() = ChromaticOctave().notes.map {
            MusicalNote.nameFromNumberedName(
                it.numberedName
            )
        }
    }
}