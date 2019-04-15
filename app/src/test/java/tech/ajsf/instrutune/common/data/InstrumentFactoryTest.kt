package tech.ajsf.instrutune.common.data

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tech.ajsf.instrutune.common.model.InstrumentCategory


internal class InstrumentFactoryTest {

    @Test
    fun `the standard guitar entity contains the correct note names`() {
        val guitarEntity = getInstrumentEntity(InstrumentCategory.Guitar)
        val expectedNotes = listOf("E2", "A2", "D3", "G3", "B3", "E4")
        assertEquals(expectedNotes, guitarEntity.numberedNotes)
    }

    @Test
    fun `the standard bass contains the correct notes`() {
        val bassEntity = getInstrumentEntity(InstrumentCategory.Bass)
        val expectedNotes = listOf("E1", "A1", "D2", "G2")
        assertEquals(expectedNotes, bassEntity.numberedNotes)
    }

    private fun getInstrumentEntity(category: InstrumentCategory, name: String = "Standard") =
        InstrumentFactory
            .getDefaultEntities()
            .first { it.category == category.toString() && it.tuningName == name }

}
