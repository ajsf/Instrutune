package co.ajsf.tuner.tuner.notefinder

import co.ajsf.tuner.model.Instrument
import co.ajsf.tuner.model.InstrumentFactory
import co.ajsf.tuner.test.data.InstrumentDataFactory
import org.junit.jupiter.api.Nested

internal class InstrumentNoteFinderTest {

    @Nested
    inner class GuitarTest : AbstractInstrumentNoteFinderTests() {
        override val instrument: Instrument = InstrumentFactory.GUITAR
    }

    @Nested
    inner class BassTest : AbstractInstrumentNoteFinderTests() {
        override val instrument: Instrument = InstrumentFactory.BASS
    }

    @Nested
    inner class UkuleleTest : AbstractInstrumentNoteFinderTests() {
        override val instrument: Instrument = InstrumentFactory.UKULELE
    }

    @Nested
    inner class RandomInstrumentNoteFinderTest : AbstractInstrumentNoteFinderTests() {
        override val instrument: Instrument = InstrumentDataFactory.randomInstrument()
    }

}