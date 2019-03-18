package co.ajsf.tuner.model

import co.ajsf.tuner.features.tuner.SelectedInstrumentInfo
import junit.framework.Assert.assertEquals
import org.junit.jupiter.api.Test

internal class InstrumentModelsTest {

    @Test
    fun `the toInstrumentInfo extension function maps correctly`() {
        val instrument = InstrumentFactory.guitar()
        val info = instrument.toInstrumentInfo()
        val expectedInfo = SelectedInstrumentInfo(
            "Guitar (Standard)",
            instrument.notes.map { it.numberedName },
            "440Hz"
        )
        assertEquals(expectedInfo, info)
    }
}