package co.ajsf.tuner.tuner.notefinder

import co.ajsf.tuner.model.Instrument
import co.ajsf.tuner.tuner.notefinder.model.MusicalNote
import co.ajsf.tuner.tuner.notefinder.model.mapToMusicalNoteList
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal abstract class AbstractInstrumentNoteFinderTests {

    protected abstract val instrument: Instrument

    private lateinit var noteFinder: NoteFinder

    private lateinit var sortedFreqs: List<Float>

    @BeforeEach
    fun setup() {
        noteFinder = NoteFinder
            .instrumentNoteFinder(instrument.mapToMusicalNoteList())
        sortedFreqs = instrument.strings.map { it.freq }.sorted()
    }

    @Test
    fun `it returns NO_NOTE for negative frequencies`() {
        val noteData = noteFinder.findNote(-1f)
        assertEquals(NO_NOTE, noteData)
    }

    @Test
    fun `it returns NO_NOTE for frequencies that are a whole step below the lowest note`() {
        val note = MusicalNote(
            MusicalNote
                .fromFloat(sortedFreqs.first())
                .relativeFreq(-2)
        )

        val noteData = noteFinder.findNote(note.floatFreq)

        assertEquals(NO_NOTE, noteData)
    }

    @Test
    fun `it returns the first string with a delta of -100 for frequencies that are a half step below the lowest note`() {
        val note = MusicalNote
            .fromFloat(sortedFreqs.first())
            .toRelativeNote(-1)

        val noteData = noteFinder.findNote(note.floatFreq)
        val expectedString = instrument.strings.minBy { it.freq }!!

        val expectedNote = NoteData(expectedString.name, expectedString.numberedName, -100)

        assertEquals(expectedNote, noteData)
    }

    @Test
    fun `it returns NO_NOTE for frequencies that are a whole step above the highest string`() {
        val note = MusicalNote
            .fromFloat(sortedFreqs.last())
            .toRelativeNote(2)

        val noteData = noteFinder.findNote(note.floatFreq)

        assertEquals(NO_NOTE, noteData)
    }

    @Test
    fun `it returns the highest string with a delta of 100 for frequencies that are a half step above the highest note`() {
        val note = MusicalNote
            .fromFloat(sortedFreqs.last())
            .toRelativeNote(1)

        val noteData = noteFinder.findNote(note.floatFreq)
        val expectedString = instrument.strings.maxBy { it.freq }!!

        val expectedData = NoteData(expectedString.name, expectedString.numberedName, 100)

        assertEquals(expectedData, noteData)
    }

    @Test
    fun `it returns the correct note and numberedName with a delta of 0 for tuned frequencies`() {
        instrument.strings.forEachIndexed { index, string ->
            val noteData = noteFinder.findNote(string.freq)
            val expectedData = NoteData(string.name, string.numberedName, 0)

            assertEquals(expectedData, noteData)
        }
    }

    @Test
    fun `it returns the lower note when the frequency is halfway between two notes`() {
        (1..sortedFreqs.lastIndex).forEach { i ->
            val freq = ((sortedFreqs[i] - sortedFreqs[i - 1]) / 2f) + sortedFreqs[i - 1]
            val noteData = noteFinder.findNote(freq)

            val expectedString = instrument.strings.find { it.freq == sortedFreqs[i - 1] }!!

            assertEquals(expectedString.name, noteData.name)
            assertEquals(expectedString.numberedName, noteData.numberedName)
            assertTrue { noteData.delta in (99..100) }
        }
    }

    @Test
    fun `it returns the higher note when the frequency is just above halfway between two notes`() {
        (1..sortedFreqs.lastIndex).forEach { i ->
            val freq = (((sortedFreqs[i] - sortedFreqs[i - 1]) / 2f) + sortedFreqs[i - 1]) + .0019f
            val noteData = noteFinder.findNote(freq)

            val expectedString = instrument.strings.find { it.freq == sortedFreqs[i] }!!

            assertEquals(expectedString.name, noteData.name)
            assertEquals(expectedString.numberedName, noteData.numberedName)
            assertTrue { noteData.delta in (-100..-99) }
        }
    }
}