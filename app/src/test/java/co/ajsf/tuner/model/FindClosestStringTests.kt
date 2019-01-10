package co.ajsf.tuner.model

import org.junit.jupiter.api.Nested

internal class FindClosestStringTests {

    @Nested
    inner class TestDataTest : AbstractInstrumentTests() {
        override val instrument: Instrument = Instrument(
            "Guitar", listOf(
                InstrumentString(name = "E", tunedFreq = 82f, minFreq = 60f, maxFreq = 96f),
                InstrumentString(name = "A", tunedFreq = 110f, minFreq = 96.01f, maxFreq = 128f),
                InstrumentString(name = "D", tunedFreq = 146f, minFreq = 128.01f, maxFreq = 171f),
                InstrumentString(name = "G", tunedFreq = 196f, minFreq = 171.01f, maxFreq = 221f),
                InstrumentString(name = "B", tunedFreq = 246f, minFreq = 221.01f, maxFreq = 287f),
                InstrumentString(name = "E", tunedFreq = 329f, minFreq = 287.01f, maxFreq = 371f)
            )
        )
    }

    @Nested
    inner class GuitarTest : AbstractInstrumentTests() {
        override val instrument: Instrument = InstrumentFactory.GUITAR
    }

    @Nested
    inner class BassTest : AbstractInstrumentTests() {
        override val instrument: Instrument = InstrumentFactory.BASS
    }
}




