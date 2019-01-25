package co.ajsf.tuner.test.data

import co.ajsf.tuner.model.Instrument
import co.ajsf.tuner.model.InstrumentString

internal object InstrumentDataFactory {

    fun randomFreq() = TestDataFactory.randomFloat() * 111
    fun randomStringName() = TestDataFactory.randomString()

    fun randomInstrument() = Instrument(
        name = TestDataFactory.randomString(),
        strings = (0..TestDataFactory.randomInt(8, 3))
            .map { randomInstrumentString() }
            .sortedBy { it.freq }
    )

    private fun randomInstrumentString() =
        InstrumentString(randomStringName(), randomFreq())
}