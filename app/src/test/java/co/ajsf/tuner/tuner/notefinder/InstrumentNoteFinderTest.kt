package co.ajsf.tuner.tuner.notefinder

import co.ajsf.tuner.common.data.InstrumentFactory
import co.ajsf.tuner.common.model.Instrument
import co.ajsf.tuner.common.model.InstrumentCategory
import co.ajsf.tuner.test.data.InstrumentDataFactory
import org.junit.jupiter.api.Nested

internal class InstrumentNoteFinderTest {

    @Nested
    inner class GuitarTest : AbstractInstrumentNoteFinderTests() {
        override val instrument: Instrument = InstrumentFactory
            .buildInstrumentsFromEntities(InstrumentFactory.getDefaultEntities(), 0)
            .find { it.category == InstrumentCategory.Guitar && it.tuningName == "Standard" }!!
    }

    @Nested
    inner class OffsetGuitarTest : AbstractInstrumentNoteFinderTests() {
        override val instrument: Instrument = InstrumentFactory
            .buildInstrumentsFromEntities(InstrumentFactory.getDefaultEntities(), -8)
            .find { it.category == InstrumentCategory.Guitar && it.tuningName == "Standard" }!!
    }

    @Nested
    inner class BassTest : AbstractInstrumentNoteFinderTests() {
        override val instrument: Instrument = InstrumentFactory
            .buildInstrumentsFromEntities(InstrumentFactory.getDefaultEntities(), 0)
            .find { it.category == InstrumentCategory.Bass && it.tuningName == "Standard" }!!
    }

    @Nested
    inner class UkuleleTest : AbstractInstrumentNoteFinderTests() {
        override val instrument: Instrument = InstrumentFactory
            .buildInstrumentsFromEntities(InstrumentFactory.getDefaultEntities(), 0)
            .find { it.category == InstrumentCategory.Ukulele && it.tuningName == "Standard" }!!
    }

    @Nested
    inner class RandomInstrumentNoteFinderTest : AbstractInstrumentNoteFinderTests() {
        override val instrument: Instrument = InstrumentDataFactory.randomInstrument()
    }

}