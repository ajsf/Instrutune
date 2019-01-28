package co.ajsf.tuner.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class InstrumentFactoryTest {

    @Test
    fun `it creates a guitar with the correct note frequencies`() {
        val guitar = InstrumentFactory.guitar()
        val freqs = guitar.strings.map { it.freq }
        val expectedFreqs = listOf(
            82.407f,
            110f,
            146.832f,
            195.998f,
            246.942f,
            329.628f
        )
        assertEquals(expectedFreqs, freqs)
    }

    @Test
    fun `it creates a guitar with the correct note names`() {
        val guitar = InstrumentFactory.guitar()
        val names = guitar.strings.map { it.name }
        val expectedNames = listOf(
            "E", "A", "D", "G", "B", "E"
        )
        assertEquals(expectedNames, names)
    }

    @Test
    fun `it creates a bass with the correct note frequencies`() {
        val bass = InstrumentFactory.bass()
        val freqs = bass.strings.map { it.freq }
        val expectedFreqs = listOf(
            41.203f,
            55f,
            73.416f,
            97.999f
        )
        assertEquals(expectedFreqs, freqs)
    }

    @Test
    fun `it creates a bass with the correct note names`() {
        val bass = InstrumentFactory.bass()
        val names = bass.strings.map { it.name }
        val expectedNames = listOf(
            "E", "A", "D", "G"
        )
        assertEquals(expectedNames, names)
    }
}