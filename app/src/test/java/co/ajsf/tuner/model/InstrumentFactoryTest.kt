package co.ajsf.tuner.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class InstrumentFactoryTest {

    @Test
    fun `it creates a guitar with the correct note frequencies`() {
        val guitar = InstrumentFactory.guitar()
        val freqs = guitar.notes.map { it.freq }
        val expectedFreqs = listOf(
            82407,
            110000,
            146832,
            195998,
            246942,
            329628
        )
        assertEquals(expectedFreqs, freqs)
    }

    @Test
    fun `it creates a guitar with the correct note names`() {
        val guitar = InstrumentFactory.guitar()
        val names = guitar.notes.map { it.numberedName }
        val expectedNames = listOf(
            "E2", "A2", "D3", "G3", "B3", "E4"
        )
        assertEquals(expectedNames, names)
    }

    @Test
    fun `it creates a bass with the correct note frequencies`() {
        val bass = InstrumentFactory.bass()
        val freqs = bass.notes.map { it.freq }
        val expectedFreqs = listOf(
            41203,
            55000,
            73416,
            97999
        )
        assertEquals(expectedFreqs, freqs)
    }

    @Test
    fun `it creates a bass with the correct note names`() {
        val bass = InstrumentFactory.bass()
        val names = bass.notes.map { it.numberedName }
        val expectedNames = listOf(
            "E1", "A1", "D2", "G2"
        )
        assertEquals(expectedNames, names)
    }
}