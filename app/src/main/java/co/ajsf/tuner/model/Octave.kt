package co.ajsf.tuner.model

import kotlin.math.pow
import kotlin.math.roundToInt

data class Note(val freq: Int, val name: String)

class Octave(number: Int, centerA: Int = 440) {

    // Multiply frequencies by 100 to store as Int. Makes determining the closest note easier.
    private val aFreq = (centerA * 2.0.pow(number) * 100).toInt()

    val notes = listOf(
        Note(aFreq, "A"),
        Note(aFreq.nextNote(1), "A#"),
        Note(aFreq.nextNote(2), "B"),
        Note(aFreq.nextNote(3), "C"),
        Note(aFreq.nextNote(4), "C#"),
        Note(aFreq.nextNote(5), "D"),
        Note(aFreq.nextNote(6), "D#"),
        Note(aFreq.nextNote(7), "E"),
        Note(aFreq.nextNote(8), "F"),
        Note(aFreq.nextNote(9), "F#"),
        Note(aFreq.nextNote(10), "G"),
        Note(aFreq.nextNote(11), "G#")
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