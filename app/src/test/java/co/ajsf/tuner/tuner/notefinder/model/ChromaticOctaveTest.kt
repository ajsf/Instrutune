package co.ajsf.tuner.tuner.notefinder.model

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class ChromaticOctaveTest {

    private val notesNames = listOf(
        "C", "C#", "D", "D#", "E", "F",
        "F#", "G", "G#", "A", "A#", "B"
    )

    @Test
    fun `it creates a list of notes with the correct names from C through B`() {
        val octave = ChromaticOctave()
        val names = octave.notes.map { it.name }

        assertEquals(notesNames, names)
    }

    @Test
    fun `it defaults to the octave starting at middle C - 261_626 Hz - 493_883 Hz`() {
        val octave = ChromaticOctave()
        val freqs = octave.notes.map { it.freq }

        val expectedFreqs = listOf(
            261626, 277183, 293665, 311127, 329628, 349228,
            369994, 391995, 415305, 440000, 466164, 493883
        )
        assertEquals(expectedFreqs, freqs)
    }

    @Test
    fun `the default middle C range contains the numbers corresponding to the keys of a piano 40-51`() {
        val octave = ChromaticOctave()
        val numbers = octave.notes.map { it.number }

        val expectedNumbers = (40..51).map { it }
        assertEquals(expectedNumbers, numbers)
    }

    @Test
    fun `the default middle C range contains the notes with numberedNames C4 - B4`() {
        val octave = ChromaticOctave()
        val numberedNames = octave.notes.map { it.numberedName }

        val expectedNames = notesNames.map { it + (4).toString() }
        assertEquals(expectedNames, numberedNames)
    }

    @Test
    fun `passing 0 returns the lowest octave, ranging from 16_352 Hz - 30_868 Hz`() {
        val octave = ChromaticOctave(0)
        val freqs = octave.notes.map { it.freq }

        val expectedFreqs = listOf(
            16352, 17324, 18354, 19445, 20602, 21827,
            23125, 24500, 25957, 27500, 29135, 30868
        )
        assertEquals(expectedFreqs, freqs)
    }

    @Test
    fun `the 0 range contains note numberedNames C0 through B#0`() {
        val octave = ChromaticOctave(0)
        val numberedNames = octave.notes.map { it.numberedName }

        val expectedNames = notesNames.map { it + (0).toString() }
        assertEquals(expectedNames, numberedNames)
    }

    @Test
    fun `the note with number 1 is A0, the low  A on a piano - 27_5 Hz`() {
        val octave = ChromaticOctave(0)
        val noteOne = octave.notes.find { it.number == 1 }!!

        val expectedNote = MusicalNote(27500, "A", 1)
        assertEquals(expectedNote, noteOne)
        assertEquals("A0", noteOne.numberedName)
    }

    @Test
    fun `passing 8 returns the highest range, from 4186_009 - 7902_133`() {
        val octave = ChromaticOctave(8)
        val freqs = octave.notes.map { it.freq }

        val expectedFreqs = listOf(
            4186009, 4434922, 4698636, 4978032, 5274041, 5587652, 5919911,
            6271927, 6644875, 7040000, 7458620, 7902133
        )

        assertEquals(expectedFreqs, freqs)
    }

    @Test
    fun `passing 8 returns note numbers 88 - 99`() {
        val octave = ChromaticOctave(8)
        val numbers = octave.notes.map { it.number }

        val expectedNumbers = (88..99).map { it }
        assertEquals(expectedNumbers, numbers)
    }

    @Test
    fun `the note with number 88 is C8 on a piano - 4186_009 Hz`() {
        val octave = ChromaticOctave(8)
        val noteOne = octave.notes.find { it.number == 88 }!!

        val expectedNote = MusicalNote(4186009, "C", 88)
        assertEquals(expectedNote, noteOne)
        assertEquals("C8", noteOne.numberedName)
    }

    @Test
    fun `passing a negative number throws an exception`() {
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            ChromaticOctave(-1)
        }
    }

    @Test
    fun `passing a number greater than 8 throws an exception`() {
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            ChromaticOctave(9)
        }
    }

    @Test
    fun `the createFullRange companion function creates a list of notes ranging from numbers -8 to 99`() {
        val numbers = ChromaticOctave.createFullRange()
            .map { it.number }
        val expectedNumbers = (-8..99).map { it }
        assertEquals(expectedNumbers, numbers)
    }

    @Test
    fun `the lowest note from createFullRange range is C0 - 16_352`() {
        val first = ChromaticOctave.createFullRange().first()
        assertEquals(16352, first.freq)
        assertEquals("C", first.name)
        assertEquals("C0", first.numberedName)
    }

    @Test
    fun `the highest note from createFullRange range is B8 - 7902_133`() {
        val last = ChromaticOctave.createFullRange().last()
        assertEquals(7902133, last.freq)
        assertEquals("B", last.name)
        assertEquals("B8", last.numberedName)
    }
}