package co.ajsf.tuner.tuner.notefinder.model

import co.ajsf.tuner.test.data.InstrumentDataFactory
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.math.roundToInt

internal class MusicalNoteTest {

    @Test
    fun `the fromFloat companion function takes the frequency as a float and returns a MusicalNote with the frequency multiplied by 1000 and stored as an int`() {
        val freq = InstrumentDataFactory.randomFreq()
        val note = MusicalNote.fromFloat(freq)
        val expectedFreqInt = (freq * 1000).roundToInt()
        assertEquals(expectedFreqInt, note.freq)
    }

    @Test
    fun `fromFloat stores the frequency with accuracy up to the thousandth and rounds down at 4 `() {
        val freq = 440.0004f
        val note = MusicalNote.fromFloat(freq)
        assertEquals(440000, note.freq)
    }

    @Test
    fun `fromFloat rounds up at 5`() {
        val freq = 440.0005f
        val note = MusicalNote.fromFloat(freq)
        assertEquals(440001, note.freq)
    }

    @Test
    fun `relativeFreq returns the correct frequency based on the number of steps`() {
        val note = MusicalNote.fromFloat(440f)
        val expectedFreqs = listOf(
            440000,
            466164,
            493883,
            523251,
            554365,
            587330,
            622254,
            659255,
            698456,
            739989,
            783991,
            830609
        )
        (0..11).forEach {
            assertEquals(expectedFreqs[it], note.relativeFreq(it))
        }
    }
}

