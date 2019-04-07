package tech.ajsf.instrutune.model

import junit.framework.Assert.assertEquals
import org.junit.jupiter.api.Test
import tech.ajsf.instrutune.common.data.InstrumentFactory
import tech.ajsf.instrutune.common.model.InstrumentCategory
import tech.ajsf.instrutune.common.model.toInstrumentInfo
import tech.ajsf.instrutune.features.tuner.SelectedInstrumentInfo

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
            "A4=440Hz"
        )
        assertEquals(expectedInfo, info)
    }
}