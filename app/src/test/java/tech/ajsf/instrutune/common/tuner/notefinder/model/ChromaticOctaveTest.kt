package tech.ajsf.instrutune.common.tuner.notefinder.model

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class ChromaticOctaveTest {

    private val notesNames = listOf(
        "C", "C#", "D", "D#", "E", "F",
        "F#", "G", "G#", "A", "A#", "B"
    )

    @Test
    fun `it creates a list of notes with the correct names from C through B, defaulting to octave 4`() {
        val octave = ChromaticOctave()
        val names = octave.notes.map { it.numberedName }
        val expectedNames = notesNames.map { it + "4" }
        assertEquals(expectedNames, names)
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
    fun `the note with numberedName A0 is the low  A on a piano - 27_5 Hz`() {
        val octave = ChromaticOctave(0)
        val noteOne = octave.notes.find { it.numberedName == "A0" }!!

        val expectedNote = MusicalNote(27500, "A0")
        assertEquals(expectedNote, noteOne)
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
    fun `passing 8 returns notes C8 - B8`() {
        val octave = ChromaticOctave(8)
        val names = octave.notes.map { it.numberedName }

        val expectedNames = notesNames.map { it + "8" }
        assertEquals(names, expectedNames)
    }

    @Test
    fun `the note with numberedName C8 has frequency 4186_009 Hz`() {
        val octave = ChromaticOctave(8)
        val noteOne = octave.notes.find { it.numberedName == "C8" }!!

        val expectedNote = MusicalNote(4186009, "C8")
        assertEquals(expectedNote, noteOne)
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
    fun `the createFullRange companion function creates a list of notes ranging from C0 to B8`() {
        val names = ChromaticOctave.createFullRange().map { it.numberedName }
        val expectedNames = (0..8).map { n -> notesNames.map { it + "$n" } }.flatten()
        assertEquals(expectedNames, names)
    }

    @Test
    fun `the lowest note from createFullRange range is C0 - 16_352`() {
        val first = ChromaticOctave.createFullRange().first()
        val expectedNote = MusicalNote(16352, "C0")
        assertEquals(expectedNote, first)
    }

    @Test
    fun `the highest note from createFullRange range is B8 - 7902_133`() {
        val last = ChromaticOctave.createFullRange().last()
        val expectedNote = MusicalNote(7902133, "B8")
        assertEquals(expectedNote, last)
    }

    @Test
    fun `to string generates the expected string`() {
        val octave = ChromaticOctave()
        val expectedString =
            "C4-261.626 Hz, C#4-277.183 Hz, D4-293.665 Hz, D#4-311.127 Hz, E4-329.628 Hz, F4-349.228 Hz, F#4-369.994 Hz, G4-391.995 Hz, G#4-415.305 Hz, A4-440.000 Hz, A#4-466.164 Hz, B4-493.883 Hz"
        assertEquals(expectedString, octave.toString())
    }
}