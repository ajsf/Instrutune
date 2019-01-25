package co.ajsf.tuner.tuner.notefinder

import co.ajsf.tuner.tuner.notefinder.model.ChromaticOctave
import co.ajsf.tuner.tuner.notefinder.model.MusicalNote

data class NoteData(val name: String, val number: Int, val delta: Int)

val NO_NOTE = NoteData("", -1, 0)

class NoteFinder private constructor(private val notes: List<MusicalNote>) {

    private val lowRange: Int = notes.first().relativeFreq(-1)
    private val highRange: Int = notes.last().relativeFreq(1)

    fun findNote(freq: Float): NoteData {
        val freqInt = (freq * 1000).toInt()
        if (freqInt in (lowRange..highRange)) {
            return when (freqInt) {
                in (lowRange..notes.first().freq) -> returnLowNote(freqInt)
                in (notes.last().freq..highRange) -> returnHighNote(freqInt)
                else -> scanNotes(freqInt, notes)
            }
        }
        return NO_NOTE
    }

    private fun returnLowNote(freq: Int): NoteData {
        val note = notes.first()
        val delta = calculateNegativeDelta(freq, lowRange, note.freq)
        return NoteData(note.name, 0, delta)
    }

    private fun returnHighNote(freq: Int): NoteData {
        val note = notes.last()
        val delta = calculatePositiveDelta(freq, highRange, note.freq)
        return NoteData(note.name, notes.lastIndex, delta)
    }

    private fun calculatePositiveDelta(freq: Int, midPoint: Int, noteFreq: Int): Int =
        (freq - noteFreq) * 100 / (midPoint - noteFreq)

    private fun calculateNegativeDelta(freq: Int, midPoint: Int, noteFreq: Int): Int =
        -calculatePositiveDelta(freq, midPoint, noteFreq)

    private fun scanNotes(freq: Int, notes: List<MusicalNote>): NoteData {
        (1 until notes.size).forEach { i ->
            val lowNote = notes[i - 1]
            val highNote = notes[i]
            if (freq in lowNote.freq..highNote.freq)
                return findNearestNote(freq, lowNote, highNote)
        }
        return NO_NOTE
    }

    private fun findNearestNote(freq: Int, lowNote: MusicalNote, highNote: MusicalNote): NoteData {
        val midPoint = lowNote.freq + ((highNote.freq - lowNote.freq) / 2)
        return if (freq <= midPoint) {
            val delta = calculatePositiveDelta(freq, midPoint, lowNote.freq)
            NoteData(lowNote.name, lowNote.number, delta)
        } else {
            val delta = calculateNegativeDelta(freq, midPoint, highNote.freq)
            NoteData(highNote.name, highNote.number, delta)
        }
    }

    companion object {
        fun chromaticNoteFinder(): NoteFinder {
            val octaves = (-4..2).map { ChromaticOctave(it) }
            val notes: List<MusicalNote> = octaves.flatMap { it.notes }
            return NoteFinder(notes)
        }

        fun instrumentNoteFinder(notes: List<MusicalNote>) = NoteFinder(notes)
    }
}
