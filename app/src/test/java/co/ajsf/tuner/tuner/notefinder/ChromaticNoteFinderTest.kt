package co.ajsf.tuner.tuner.notefinder

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class ChromaticNoteFinderTest {

    private val noteFinder = NoteFinder.chromaticNoteFinder(0)

    @Test
    fun `440 returns A4 with delta of 0`() {
        val note = noteFinder.findNote(440f)
        assertEquals("A4", note.numberedName)
        assertEquals(0, note.delta)
    }

    @Test
    fun `27_5 returns A0 with delta of 0`() {
        val note = noteFinder.findNote(27.5f)
        assertEquals("A0", note.numberedName)
        assertEquals(0, note.delta)
    }

    @Test
    fun `261_626 returns C4 with a delta of 0`() {
        val note = noteFinder.findNote(261.626f)
        assertEquals("C4", note.numberedName)
        assertEquals(0, note.delta)
    }

    @Test
    fun `16_352 returns C0 with a delta of 0`() {
        val note = noteFinder.findNote(16.352f)
        assertEquals("C0", note.numberedName)
        assertEquals(0, note.delta)
    }

    @Test
    fun `15_434 returns C0 with a delta of -100`() {
        val note = noteFinder.findNote(15.434f)
        assertEquals("C0", note.numberedName)
        assertEquals(-100, note.delta)
    }

    @Test
    fun `below 15_434 returns NO_NOTE`() {
        val note = noteFinder.findNote(15.4339f)
        assertEquals(NO_NOTE, note)
    }

    @Test
    fun `3322_44 returns G#7 with a delta of 0`() {
        val note = noteFinder.findNote(3322.44f)
        assertEquals("G#7", note.numberedName)
        assertEquals(0, note.delta)
    }

    @Test
    fun `3520 returns A7 with a delta of 0`() {
        val note = noteFinder.findNote(3520f)
        assertEquals("A7", note.numberedName)
        assertEquals(0, note.delta)
    }

    @Test
    fun `7902_133 returns B8 with a delta of 0`() {
        val note = noteFinder.findNote(7902.133f)
        assertEquals("B8", note.numberedName)
        assertEquals(0, note.delta)
    }

    @Test
    fun `8372_019 returns B8 with a delta of 100`() {
        val note = noteFinder.findNote(8372.019f)
        assertEquals("B8", note.numberedName)
        assertEquals(100, note.delta)
    }

    @Test
    fun `above 8372_019 returns NO_NOTE`() {
        val note = noteFinder.findNote(8372.02f)
        assertEquals(NO_NOTE, note)
    }

    @Test
    fun `it returns C with a delta of 0 for 9 octaves starting from 16_352`() {
        var freq = 16.352f
        repeat(9) {
            val note = noteFinder.findNote(freq)
            assertEquals("C$it", note.numberedName)
            assertEquals(0, note.delta)
            freq *= 2
        }
    }

    @Test
    fun `it returns C# for 9 octaves starting from 17_324`() {
        var freq = 17.324f
        repeat(9) {
            val note = noteFinder.findNote(freq)
            assertEquals("C#$it", note.numberedName)
            assertEquals(0, note.delta)
            freq *= 2
        }
    }

    @Test
    fun `it returns D for 9 octaves starting from 18_354`() {
        var freq = 18.354f
        repeat(9) {
            val note = noteFinder.findNote(freq)
            assertEquals("D$it", note.numberedName)
            assertEquals(0, note.delta)
            freq *= 2
        }
    }

    @Test
    fun `it returns D# for 9 octaves starting from 19_445`() {
        var freq = 19.445f
        repeat(9) {
            val note = noteFinder.findNote(freq)
            assertEquals("D#$it", note.numberedName)
            assertEquals(0, note.delta)
            freq *= 2
        }
    }

    @Test
    fun `it returns E for 9 octaves starting from 20_601`() {
        var freq = 20.601f
        repeat(9) {
            val note = noteFinder.findNote(freq)
            assertEquals("E$it", note.numberedName)
            assertEquals(0, note.delta)
            freq *= 2
        }
    }

    @Test
    fun `it returns F for 9 octaves starting from 21_826`() {
        var freq = 21.826f
        repeat(9) {
            val note = noteFinder.findNote(freq)
            assertEquals("F$it", note.numberedName)
            assertEquals(0, note.delta)
            freq *= 2
        }
    }

    @Test
    fun `it returns F# for 9 octaves starting from 23_124`() {
        var freq = 23.124f
        repeat(9) {
            val note = noteFinder.findNote(freq)
            assertEquals("F#$it", note.numberedName)
            assertEquals(0, note.delta)
            freq *= 2
        }
    }

    @Test
    fun `it returns G for 9 octaves starting from 24_499`() {
        var freq = 24.499f
        repeat(7) {
            val note = noteFinder.findNote(freq)
            assertEquals("G$it", note.numberedName)
            assertEquals(0, note.delta)
            freq *= 2
        }
    }

    @Test
    fun `it returns G# for 9 octaves starting from 25_956`() {
        var freq = 25.956f
        repeat(9) {
            val note = noteFinder.findNote(freq)
            assertEquals("G#$it", note.numberedName)
            assertEquals(0, note.delta)
            freq *= 2
        }
    }

    @Test
    fun `it returns A for 9 octaves starting from 27_5`() {
        var freq = 27.5f
        repeat(9) {
            val note = noteFinder.findNote(freq)
            assertEquals("A$it", note.numberedName)
            assertEquals(0, note.delta)
            freq *= 2
        }
    }

    @Test
    fun `it returns A# for 9 octaves starting from 29_14`() {
        var freq = 29.14f
        repeat(9) {
            val note = noteFinder.findNote(freq)
            assertEquals("A#$it", note.numberedName)
            assertEquals(0, note.delta)
            freq *= 2
        }
    }

    @Test
    fun `it returns B for 9 octaves starting from 30_87`() {
        var freq = 30.87f
        repeat(9) {
            val note = noteFinder.findNote(freq)
            assertEquals("B$it", note.numberedName)
            assertEquals(0, note.delta)
            freq *= 2
        }
    }

    @Test
    fun `when offset is set to -8, 432 returns A4 with a delta of 0`() {
        val offsetNoteFinder = NoteFinder.chromaticNoteFinder(-8)
        val note = offsetNoteFinder.findNote(432f)
        assertEquals("A4", note.numberedName)
        assertEquals(0, note.delta)
    }

    @Test
    fun `when offset is set to -8, 256_87 returns C4 with a delta of 0`() {
        val offsetNoteFinder = NoteFinder.chromaticNoteFinder(-8)
        val note = offsetNoteFinder.findNote(256.87f)
        assertEquals("C4", note.numberedName)
        assertEquals(0, note.delta)
    }
}