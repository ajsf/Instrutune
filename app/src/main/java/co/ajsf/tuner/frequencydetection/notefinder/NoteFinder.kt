package co.ajsf.tuner.frequencydetection.notefinder

import co.ajsf.tuner.model.MusicalNote

abstract class NoteFinder {

    abstract fun findNote(freq: Float): String

    protected fun findNote(freq: Int, notes: List<MusicalNote>): String {
        (1 until notes.size).forEach { i ->
            val lowNote = notes[i - 1]
            val highNote = notes[i]
            if (freq in lowNote.freq..highNote.freq) return findNearestNote(freq, lowNote, highNote)
        }
        return ""
    }

    private fun findNearestNote(freq: Int, lowNote: MusicalNote, highNote: MusicalNote): String {
        val midPoint = lowNote.freq + ((highNote.freq - lowNote.freq) / 2)
        return if (freq <= midPoint) lowNote.name else highNote.name
    }
}
