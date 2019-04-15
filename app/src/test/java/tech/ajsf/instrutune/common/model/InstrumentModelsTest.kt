package tech.ajsf.instrutune.common.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tech.ajsf.instrutune.test.data.InstrumentDataFactory

internal class InstrumentModelsTest {

    @Test
    fun `the toInstrumentInfo extension function maps correctly`() {
        val instrument = InstrumentDataFactory.randomInstrument()

        val info = instrument.toInstrumentInfo()

        val expectedName = "${instrument.category} (${instrument.tuningName})"

        val expectedInfo = SelectedInstrumentInfo(
            expectedName,
            instrument.notes.map { it.numberedName },
            "A4=440Hz",
            instrument.category.toString()
        )
        assertEquals(expectedInfo, info)
    }
}