package co.ajsf.tuner.model

import co.ajsf.tuner.common.data.InstrumentFactory
import co.ajsf.tuner.common.model.InstrumentCategory
import co.ajsf.tuner.common.model.toInstrumentInfo
import co.ajsf.tuner.features.tuner.SelectedInstrumentInfo
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