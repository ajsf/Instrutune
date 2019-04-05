package tech.ajsf.instrutune.model

import tech.ajsf.instrutune.common.data.InstrumentFactory
import tech.ajsf.instrutune.common.model.InstrumentCategory
import tech.ajsf.instrutune.common.model.toInstrumentInfo
import tech.ajsf.instrutune.features.tuner.SelectedInstrumentInfo
import junit.framework.Assert.assertEquals
import org.junit.jupiter.api.Test

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
            "440Hz"
        )
        assertEquals(expectedInfo, info)
    }
}