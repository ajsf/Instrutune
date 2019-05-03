package tech.ajsf.instrutune.common.tuner.notefinder

import org.junit.jupiter.api.Nested
import tech.ajsf.instrutune.common.data.InstrumentFactory
import tech.ajsf.instrutune.common.data.mapper.EntityToInstrumentMapper
import tech.ajsf.instrutune.common.model.Instrument
import tech.ajsf.instrutune.common.model.InstrumentCategory
import tech.ajsf.instrutune.test.data.InstrumentDataFactory

internal class InstrumentNoteFinderTest {

    private val mapper = EntityToInstrumentMapper()

    @Nested
    inner class GuitarTest : AbstractInstrumentNoteFinderTests() {
        override val instrument: Instrument =
            mapper.toInstrument(getEntity(InstrumentCategory.Guitar))
    }

    @Nested
    inner class BassTest : AbstractInstrumentNoteFinderTests() {
        override val instrument: Instrument =
            mapper.toInstrument(getEntity(InstrumentCategory.Bass))
    }

    @Nested
    inner class UkuleleTest : AbstractInstrumentNoteFinderTests() {
        override val instrument: Instrument =
            mapper.toInstrument(getEntity(InstrumentCategory.Ukulele))
    }

    @Nested
    inner class RandomInstrumentNoteFinderTest : AbstractInstrumentNoteFinderTests() {
        override val instrument: Instrument = InstrumentDataFactory.randomInstrument()
    }

    private fun getEntity(category: InstrumentCategory, name: String = "Standard") =
        InstrumentFactory
            .getDefaultEntities()
            .first { it.category == category.toString() && it.tuningName == name }
}