package co.ajsf.tuner.frequencydetection.notefinder

import co.ajsf.tuner.model.Note
import co.ajsf.tuner.model.Octave

class NoteFinder {

    private val octaves = (-4..2).map { Octave(it) }

    fun findNote(freq: Float): String {
        val freqInt = (freq * 100).toInt()
        octaves.forEach {
            if (freqInt in it.frequencyRange) {
                return when (freqInt) {
                    in (it.frequencyRange.start..it.notes.first().freq) -> it.notes.first().name
                    in (it.frequencyRange.last..it.notes.last().freq) -> it.notes.last().name
                    else -> findNote(freqInt, it)
                }
            }
        }
        return ""
    }

    private fun findNote(freq: Int, octave: Octave): String = with(octave) {
        (1 until notes.size).forEach { i ->
            val lowNote = notes[i - 1]
            val highNote = notes[i]
            if (freq in lowNote.freq..highNote.freq) return findNearestNote(freq, lowNote, highNote)
        }
        return ""
    }

    private fun findNearestNote(freq: Int, lowNote: Note, highNote: Note): String {
        val midPoint = lowNote.freq + ((highNote.freq - lowNote.freq) / 2)
        return if (freq <= midPoint) lowNote.name else highNote.name
    }
}
