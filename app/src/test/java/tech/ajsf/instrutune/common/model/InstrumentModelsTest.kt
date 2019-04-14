package tech.ajsf.instrutune.common.model

import junit.framework.Assert.assertEquals
import org.junit.jupiter.api.Test
import tech.ajsf.instrutune.common.data.InstrumentFactory

internal class InstrumentModelsTest {

    @Test
    fun `the toInstrumentInfo extension function maps correctly`() {
        val instrument = InstrumentFactory
            .buildInstrumentsFromEntities(InstrumentFactory.getDefaultEntities(), 0)
            .find { it.category == InstrumentCategory.Guitar && it.tuningName == "Standard" }!!

        val info = instrument.toInstrumentInfo()
        val expectedInfo = SelectedInstrumentInfo(
            "Guitar (Standard)",
            instrument.notes.map { it.numberedName },
            "A4=440Hz",
            InstrumentCategory.Guitar.toString()
        )
        assertEquals(expectedInfo, info)
    }
}