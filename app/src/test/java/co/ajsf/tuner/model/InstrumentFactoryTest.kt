package co.ajsf.tuner.model

import co.ajsf.tuner.common.data.InstrumentFactory
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
    fun `when an offset of -8 is sent, it creates a guitar with frequencies tuned to A4 = 432`() {
        val guitar = InstrumentFactory.guitar(-8)
        val freqs = guitar.notes.map { it.freq }
        val expectedFreqs = listOf(
            80909,
            108000,
            144163,
            192434,
            242452,
            323634
        )
        assertEquals(expectedFreqs, freqs)
    }

    @Test
    fun `when an offset of 6 is sent, it creates a guitar with frequencies tuned to A4 = 446`() {
        val guitar = InstrumentFactory.guitar(6)
        val freqs = guitar.notes.map { it.freq }
        val expectedFreqs = listOf(
            83531,
            111500,
            148835,
            198670,
            250309,
            334122
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