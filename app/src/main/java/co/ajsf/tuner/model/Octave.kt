package co.ajsf.tuner.model

import kotlin.math.pow
import kotlin.math.roundToInt

data class MusicalNote(val freq: Int, val name: String)

class Octave(number: Int, centerA: Int = 440) {

    // Multiply frequencies by 100 to store as Int. Makes determining the closest note easier.
    private val aFreq = (centerA * 2.0.pow(number) * 100).toInt()

    val notes = listOf(
        MusicalNote(aFreq, "A"),
        MusicalNote(aFreq.nextNote(1), "A#"),
        MusicalNote(aFreq.nextNote(2), "B"),
        MusicalNote(aFreq.nextNote(3), "C"),
        MusicalNote(aFreq.nextNote(4), "C#"),
        MusicalNote(aFreq.nextNote(5), "D"),
        MusicalNote(aFreq.nextNote(6), "D#"),
        MusicalNote(aFreq.nextNote(7), "E"),
        MusicalNote(aFreq.nextNote(8), "F"),
        MusicalNote(aFreq.nextNote(9), "F#"),
        MusicalNote(aFreq.nextNote(10), "G"),
        MusicalNote(aFreq.nextNote(11), "G#")
    )

    val frequencyRange = (calculateLowRange()..calculateHighRange())

    private fun calculateLowRange(): Int {
        val lowNote = notes.first().freq
        val lowerNote = lowNote.nextNote(-1)
        val midPoint = (lowNote - lowerNote) / 2
        return lowNote - midPoint
    }

    private fun calculateHighRange(): Int {
        val highNote = notes.last().freq
        val higherNote = highNote.nextNote(1)
        val midPoint = (higherNote - highNote) / 2
        return highNote + midPoint
    }

    private fun Int.nextNote(steps: Int): Int = (this * 2.0.pow(steps / 12.0)).roundToInt()

    override fun toString(): String {
        return notes.joinToString {
            "${it.name}-${String.format("%.2f", it.freq / 100f)} Hz"
        }
    }
}