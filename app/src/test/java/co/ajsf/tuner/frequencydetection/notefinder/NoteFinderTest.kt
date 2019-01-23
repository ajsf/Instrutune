package co.ajsf.tuner.frequencydetection.notefinder

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class NoteFinderTest {

    private val noteFinder = NoteFinder()

    @Test
    fun `440 returns A`() {
        val note = noteFinder.findNote(440f)
        assertEquals("A", note)
    }

    @Test
    fun `27_5 returns A`() {
        val note = noteFinder.findNote(27.5f)
        assertEquals("A", note)
    }

    @Test
    fun `26_73 returns A`() {
        val note = noteFinder.findNote(26.73f)
        assertEquals("A", note)
    }

    @Test
    fun `below 26_73 returns a blank string`() {
        val note = noteFinder.findNote(26.72f)
        assertEquals("", note)
    }

    @Test
    fun `3322_44 returns G#`() {
        val note = noteFinder.findNote(3322.44f)
        assertEquals("G#", note)
    }

    @Test
    fun `above 3323_44 returns a blank string`() {
        val note = noteFinder.findNote(3322.45f)
        assertEquals("", note)
    }

    @Test
    fun `it returns A for four octaves starting from 27_5`() {
        var freq = 27.5f
        repeat(7) {
            assertEquals("A", noteFinder.findNote(freq))
            freq *= 2
        }
    }

    @Test
    fun `it returns A# for 7 octaves starting from 29_14`() {
        var freq = 29.14f
        repeat(7) {
            assertEquals("A#", noteFinder.findNote(freq))
            freq *= 2
        }
    }

    @Test
    fun `it returns B for 7 octaves starting from 30_87`() {
        var freq = 30.87f
        repeat(7) {
            assertEquals("B", noteFinder.findNote(freq))
            freq *= 2
        }
    }

    @Test
    fun `it returns C for 7 octaves starting from 32_70`() {
        var freq = 32.70f
        repeat(7) {
            assertEquals("C", noteFinder.findNote(freq))
            freq *= 2
        }
    }

    @Test
    fun `it returns C# for 7 octaves starting from 34_65`() {
        var freq = 34.65f
        repeat(7) {
            assertEquals("C#", noteFinder.findNote(freq))
            freq *= 2
        }
    }

    @Test
    fun `it returns D for 7 octaves starting from 36_71`() {
        var freq = 36.71f
        repeat(7) {
            assertEquals("D", noteFinder.findNote(freq))
            freq *= 2
        }
    }

    @Test
    fun `it returns D# for 7 octaves starting from 38_89`() {
        var freq = 38.89f
        repeat(7) {
            assertEquals("D#", noteFinder.findNote(freq))
            freq *= 2
        }
    }

    @Test
    fun `it returns E for 7 octaves starting from 41_20`() {
        var freq = 41.20f
        repeat(7) {
            assertEquals("E", noteFinder.findNote(freq))
            freq *= 2
        }
    }

    @Test
    fun `it returns F for 7 octaves starting from 43_65`() {
        var freq = 43.65f
        repeat(7) {
            assertEquals("F", noteFinder.findNote(freq))
            freq *= 2
        }
    }

    @Test
    fun `it returns F# for 7 octaves starting from 46_25`() {
        var freq = 46.25f
        repeat(7) {
            assertEquals("F#", noteFinder.findNote(freq))
            freq *= 2
        }
    }

    @Test
    fun `it returns G for 7 octaves starting from 49`() {
        var freq = 49f
        repeat(7) {
            assertEquals("G", noteFinder.findNote(freq))
            freq *= 2
        }
    }

    @Test
    fun `it returns G# for 7 octaves starting from 51_91`() {
        var freq = 51.91f
        repeat(7) {
            assertEquals("G#", noteFinder.findNote(freq))
            freq *= 2
        }
    }
}