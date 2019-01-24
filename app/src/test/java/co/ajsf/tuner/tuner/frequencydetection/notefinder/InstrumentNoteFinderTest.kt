package co.ajsf.tuner.tuner.frequencydetection.notefinder

import co.ajsf.tuner.model.Instrument
import co.ajsf.tuner.model.InstrumentFactory
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

}