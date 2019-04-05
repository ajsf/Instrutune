package tech.ajsf.instrutune.model

import tech.ajsf.instrutune.common.data.InstrumentFactory
import tech.ajsf.instrutune.common.model.InstrumentCategory
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test


internal class InstrumentFactoryTest {

    @Test
    fun `the standard guitar entity contains the correct note names`() {
        val guitarEntity = getInstrumentEntity(InstrumentCategory.Guitar).first()
        val expectedNotes = listOf("E2", "A2", "D3", "G3", "B3", "E4")
        assertEquals(expectedNotes, guitarEntity.numberedNotes)
    }

    @Test
    fun `the buildInstrumentFromEntityFunction returns the correct frequencies for guitar`() {
        val entity = getInstrumentEntity(InstrumentCategory.Guitar)
        val guitar = InstrumentFactory.buildInstrumentsFromEntities(entity).first()

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
        val entity = getInstrumentEntity(InstrumentCategory.Guitar)
        val guitar = InstrumentFactory.buildInstrumentsFromEntities(entity, -8).first()

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
        val entity = getInstrumentEntity(InstrumentCategory.Guitar)
        val guitar = InstrumentFactory.buildInstrumentsFromEntities(entity, 6).first()

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
    fun `the standard bass contains the correct notes`() {
        val bassEntity = getInstrumentEntity(InstrumentCategory.Bass).first()
        val expectedNotes = listOf("E1", "A1", "D2", "G2")
        assertEquals(expectedNotes, bassEntity.numberedNotes)
    }

    @Test
    fun `the buildInstrumentFromEntityFunction returns the correct frequencies for bass`() {
        val entity = getInstrumentEntity(InstrumentCategory.Bass)
        val bass = InstrumentFactory.buildInstrumentsFromEntities(entity).first()

        val freqs = bass.notes.map { it.freq }
        val expectedFreqs = listOf(
            41203,
            55000,
            73416,
            97999
        )
        assertEquals(expectedFreqs, freqs)
    }

    private fun getInstrumentEntity(category: InstrumentCategory, name: String = "Standard") = listOf(
        InstrumentFactory
            .getDefaultEntities()
            .find { it.category == category.toString() && it.tuningName == name }!!
    )
}
