package co.ajsf.tuner.tuner.notefinder.model

import kotlin.math.pow

data class ChromaticOctave(private val number: Int, private val centerA: Int = 440) {

    private val aFreq: Float = centerA * 2.0.pow(number).toFloat()
    private val A = MusicalNote.fromFloat(aFreq, "A", number)

    val notes = listOf(
        A,
        MusicalNote(A.relativeFreq(1), "A#", number + 1),
        MusicalNote(A.relativeFreq(2), "B", number + 2),
        MusicalNote(A.relativeFreq(3), "C", number + 3),
        MusicalNote(A.relativeFreq(4), "C#", number + 4),
        MusicalNote(A.relativeFreq(5), "D", number + 5),
        MusicalNote(A.relativeFreq(6), "D#", number + 6),
        MusicalNote(A.relativeFreq(7), "E", number + 7),
        MusicalNote(A.relativeFreq(8), "F", number + 8),
        MusicalNote(A.relativeFreq(9), "F#", number + 9),
        MusicalNote(A.relativeFreq(10), "G", number + 10),
        MusicalNote(A.relativeFreq(11), "G#", number + 11)
    )

    override fun toString(): String {
        return notes.joinToString {
            "${it.name}-${String.format("%.2f", it.freq / 100f)} Hz"
        }
    }
}