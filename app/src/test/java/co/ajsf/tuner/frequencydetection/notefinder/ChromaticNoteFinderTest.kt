package co.ajsf.tuner.frequencydetection.notefinder

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class ChromaticNoteFinderTest {

    private val noteFinder = NoteFinder.chromaticNoteFinder()

    @Test
    fun `440 returns A with delta of 0`() {
        val note = noteFinder.findNote(440f)
        assertEquals("A", note.name)
        assertEquals(0, note.delta)
    }

    @Test
    fun `27_5 returns A with delta of 0`() {
        val note = noteFinder.findNote(27.5f)
        assertEquals("A", note.name)
        assertEquals(0, note.delta)
    }

    @Test
    fun `25_96 returns A with a delta of -100`() {
        val note = noteFinder.findNote(25.96f)
        assertEquals("A", note.name)
        assertEquals(-100, note.delta)
    }

    @Test
    fun `below 25_96 returns NO_NOTE`() {
        val note = noteFinder.findNote(25.95f)
        assertEquals(NO_NOTE, note)
    }

    @Test
    fun `3322_44 returns G# with a delta of 0`() {
        val note = noteFinder.findNote(3322.44f)
        assertEquals(0, note.delta)
    }

    @Test
    fun `3520 returns G# with a delta of 100`() {
        val note = noteFinder.findNote(3520f)
        assertEquals(100, note.delta)
    }

    @Test
    fun `above 3352 returns NO_NOTE`() {
        val note = noteFinder.findNote(3520.01f)
        assertEquals(NO_NOTE, note)
    }

    @Test
    fun `it returns A with delta of 0 for 7 octaves starting from 27_5`() {
        var freq = 27.5f
        repeat(7) {
            val note = noteFinder.findNote(freq)
            assertEquals("A", note.name)
            assertEquals(0, note.delta)
            freq *= 2
        }
    }

    @Test
    fun `it returns A# for 7 octaves starting from 29_14`() {
        var freq = 29.14f
        repeat(7) {
            val note = noteFinder.findNote(freq)
            assertEquals("A#", note.name)
            assertEquals(0, note.delta)
            freq *= 2
        }
    }

    @Test
    fun `it returns B for 7 octaves starting from 30_87`() {
        var freq = 30.87f
        repeat(7) {
            val note = noteFinder.findNote(freq)
            assertEquals("B", note.name)
            assertEquals(0, note.delta)
            freq *= 2
        }
    }

    @Test
    fun `it returns C for 7 octaves starting from 32_70`() {
        var freq = 32.70f
        repeat(7) {
            val note = noteFinder.findNote(freq)
            assertEquals("C", note.name)
            assertEquals(0, note.delta)
            freq *= 2
        }
    }

    @Test
    fun `it returns C# for 7 octaves starting from 34_65`() {
        var freq = 34.65f
        repeat(7) {
            val note = noteFinder.findNote(freq)
            assertEquals("C#", note.name)
            assertEquals(0, note.delta)
            freq *= 2
        }
    }

    @Test
    fun `it returns D for 7 octaves starting from 36_71`() {
        var freq = 36.71f
        repeat(7) {
            val note = noteFinder.findNote(freq)
            assertEquals("D", note.name)
            assertEquals(0, note.delta)
            freq *= 2
        }
    }

    @Test
    fun `it returns D# for 7 octaves starting from 38_89`() {
        var freq = 38.89f
        repeat(7) {
            val note = noteFinder.findNote(freq)
            assertEquals("D#", note.name)
            assertEquals(0, note.delta)
            freq *= 2
        }
    }

    @Test
    fun `it returns E for 7 octaves starting from 41_20`() {
        var freq = 41.20f
        repeat(7) {
            val note = noteFinder.findNote(freq)
            assertEquals("E", note.name)
            assertEquals(0, note.delta)
            freq *= 2
        }
    }

    @Test
    fun `it returns F for 7 octaves starting from 43_65`() {
        var freq = 43.65f
        repeat(7) {
            val note = noteFinder.findNote(freq)
            assertEquals("F", note.name)
            assertEquals(0, note.delta)
            freq *= 2
        }
    }

    @Test
    fun `it returns F# for 7 octaves starting from 46_25`() {
        var freq = 46.25f
        repeat(7) {
            val note = noteFinder.findNote(freq)
            assertEquals("F#", note.name)
            assertEquals(0, note.delta)
            freq *= 2
        }
    }

    @Test
    fun `it returns G for 7 octaves starting from 49`() {
        var freq = 49f
        repeat(7) {
            val note = noteFinder.findNote(freq)
            assertEquals("G", note.name)
            assertEquals(0, note.delta)
            freq *= 2
        }
    }

    @Test
    fun `it returns G# for 7 octaves starting from 51_91`() {
        var freq = 51.91f
        repeat(7) {
            val note = noteFinder.findNote(freq)
            assertEquals("G#", note.name)
            assertEquals(0, note.delta)
            freq *= 2
        }
    }
}