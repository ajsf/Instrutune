package co.ajsf.tuner.tuner.notefinder.model

import kotlin.math.pow

data class ChromaticOctave(private val number: Int = 4, private val centerA: Int = 440) {

    private val aFreq: Float = centerA * 2.0.pow(number - 4).toFloat()
    private val A = MusicalNote.fromFloat(aFreq, "A", (number * 12) + 1)

    init {
        if ((number in (0..8)).not()) throw IllegalArgumentException("Invalid number: $number. It must be in the range of 0 to 8.")
    }

    val notes = listOf(
        MusicalNote(A.relativeFreq(-9), "C", (number * 12) - 8),
        MusicalNote(A.relativeFreq(-8), "C#", (number * 12) - 7),
        MusicalNote(A.relativeFreq(-7), "D", (number * 12) - 6),
        MusicalNote(A.relativeFreq(-6), "D#", (number * 12) - 5),
        MusicalNote(A.relativeFreq(-5), "E", (number * 12) - 4),
        MusicalNote(A.relativeFreq(-4), "F", (number * 12) - 3),
        MusicalNote(A.relativeFreq(-3), "F#", (number * 12) - 2),
        MusicalNote(A.relativeFreq(-2), "G", (number * 12) - 1),
        MusicalNote(A.relativeFreq(-1), "G#", number * 12),
        A,
        MusicalNote(A.relativeFreq(1), "A#", (number * 12) + 2),
        MusicalNote(A.relativeFreq(2), "B", (number * 12) + 3)
    )

    override fun toString(): String {
        return notes.joinToString {
            "${it.name}-${String.format("%.3f", it.floatFreq)} Hz"
        }
    }

    companion object {
        fun createFullRange(): List<MusicalNote> = (0..8).map { ChromaticOctave(it) }.flatMap { it.notes }
    }
}