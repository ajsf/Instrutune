package tech.ajsf.instrutune.common.data.mapper

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import tech.ajsf.instrutune.common.data.InstrumentFactory
import tech.ajsf.instrutune.common.model.InstrumentCategory
import tech.ajsf.instrutune.test.data.InstrumentDataFactory

internal class EntityToInstrumentMapperTest {

    private val mapper = EntityToInstrumentMapper()

    @Test
    fun `the buildInstrumentFromEntityFunction returns the correct frequencies for guitar`() {
        val entity = getInstrumentEntity(InstrumentCategory.Guitar)
        val guitar = mapper.toInstrument(entity)

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
    fun `the buildInstrumentFromEntityFunction returns the correct frequencies for bass`() {
        val entity = getInstrumentEntity(InstrumentCategory.Bass)
        val bass = mapper.toInstrument(entity)

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
    fun `when an offset of 6 is sent, it creates a guitar with frequencies tuned to A4 = 446`() {
        val entity = getInstrumentEntity(InstrumentCategory.Guitar)
        val guitar = mapper.toInstrument(entity, 6)

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
    fun `when an offset of -8 is sent, it creates a guitar with frequencies tuned to A4 = 432`() {
        val entity = getInstrumentEntity(InstrumentCategory.Guitar)
        val guitar = mapper.toInstrument(entity, -8)

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
    fun `toInstrumentList maps each entity`() {
        val entities = InstrumentFactory.getDefaultEntities()
        val instruments = mapper.toInstrumentList(entities)
        instruments.forEachIndexed { index, instrument ->
            val entity = entities[index]
            with(instrument) {
                assertEquals(entity.category, category.toString())
                assertEquals(entity.id, id)
                assertEquals(entity.tuningName, tuningName)
                assertEquals(entity.numberedNotes, notes.map { it.numberedName })
            }
        }
    }

    @Test
    fun `if the entity has invalid note names, the mapper throws a runtime exception`() {
        val entity = InstrumentDataFactory.randomInstrumentEntityList().first()

        assertThrows(RuntimeException::class.java) {
            mapper.toInstrument(entity)
        }
    }

    private fun getInstrumentEntity(category: InstrumentCategory, name: String = "Standard") =
        InstrumentFactory
            .getDefaultEntities()
            .first { it.category == category.toString() && it.tuningName == name }
}