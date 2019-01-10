package co.ajsf.tuner.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal abstract class AbstractInstrumentTests {

    abstract val instrument: Instrument

    @Test
    fun `it returns string number -1 for negative frequencies`() {
        val stringData = instrument.findClosestString(-1f)
        assertEquals(-1, stringData.number)
    }

    @Test
    fun `it returns string number -1 for frequencies that are below the range of all the strings`() {
        val stringData = instrument.findClosestString(instrument.strings.first().minFreq - 0.01f)
        assertEquals(-1, stringData.number)
    }

    @Test
    fun `it returns string number -1 for frequencies that are above the range of all the strings`() {
        val stringData = instrument.findClosestString(instrument.strings.last().maxFreq + 0.01f)
        assertEquals(-1, stringData.number)
    }

    @Test
    fun `it returns the correct string number for tuned frequencies`() {
        instrument.strings.forEachIndexed { index, string ->
            val stringData = instrument.findClosestString(string.tunedFreq)
            assertEquals(index, stringData.number)
        }
    }

    @Test
    fun `it returns a delta of 0 for tuned frequencies`() {
        instrument.strings.forEach { string ->
            val stringData = instrument.findClosestString(string.tunedFreq)
            assertEquals(0, stringData.delta)
        }
    }

    @Test
    fun `it returns the correct string number for max frequencies`() {
        instrument.strings.forEachIndexed { index, string ->
            val stringData = instrument.findClosestString(string.maxFreq)
            assertEquals(index, stringData.number)
        }
    }

    @Test
    fun `it returns a delta of 75 for max frequencies`() {
        instrument.strings.forEach { string ->
            val stringData = instrument.findClosestString(string.maxFreq)
            assertEquals(75, stringData.delta)
        }
    }

    @Test
    fun `it returns the correct string number when the frequency is just below the max freq`() {
        instrument.strings.forEachIndexed { index, string ->
            val stringData = instrument.findClosestString(string.maxFreq - 0.01f)
            assertEquals(index, stringData.number)
        }
    }

    @Test
    fun `it returns the correct string number for min frequencies`() {
        instrument.strings.forEachIndexed { index, string ->
            val stringData = instrument.findClosestString(string.minFreq)
            assertEquals(index, stringData.number)
        }
    }

    @Test
    fun `it returns a delta of -75 for min frequencies`() {
        instrument.strings.forEach { string ->
            val stringData = instrument.findClosestString(string.minFreq)
            assertEquals(-75, stringData.delta)
        }
    }

    @Test
    fun `it returns the correct string number when the frequency is just above the min freq`() {
        instrument.strings.forEachIndexed { index, string ->
            val stringData = instrument.findClosestString(string.minFreq + 0.01f)
            assertEquals(index, stringData.number)
        }
    }
}