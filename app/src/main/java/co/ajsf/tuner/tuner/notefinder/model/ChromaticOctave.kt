package co.ajsf.tuner.tuner.notefinder.model

import kotlin.math.pow

data class ChromaticOctave(private val number: Int, private val centerA: Int = 440) {

    private val aFreq: Float = centerA * 2.0.pow(number).toFloat()
    private val A = MusicalNote.fromFloat(aFreq, "A")

    val notes = listOf(
        A,
        MusicalNote(A.relativeFreq(1), "A#"),
        MusicalNote(A.relativeFreq(2), "B"),
        MusicalNote(A.relativeFreq(3), "C"),
        MusicalNote(A.relativeFreq(4), "C#"),
        MusicalNote(A.relativeFreq(5), "D"),
        MusicalNote(A.relativeFreq(6), "D#"),
        MusicalNote(A.relativeFreq(7), "E"),
        MusicalNote(A.relativeFreq(8), "F"),
        MusicalNote(A.relativeFreq(9), "F#"),
        MusicalNote(A.relativeFreq(10), "G"),
        MusicalNote(A.relativeFreq(11), "G#")
    )

    override fun toString(): String {
        return notes.joinToString {
            "${it.name}-${String.format("%.2f", it.freq / 100f)} Hz"
        }
    }
}