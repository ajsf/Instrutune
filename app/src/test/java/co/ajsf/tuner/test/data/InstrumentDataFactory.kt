package co.ajsf.tuner.test.data

import co.ajsf.tuner.model.Instrument
import co.ajsf.tuner.model.InstrumentString
import co.ajsf.tuner.tuner.notefinder.model.MusicalNote

internal object InstrumentDataFactory {

    fun randomFreq() = TestDataFactory.randomFloat() * 111
    private fun randomStringName() = TestDataFactory.randomString()

    fun randomInstrument() = Instrument(
        name = TestDataFactory.randomString(),
        strings = (0..TestDataFactory.randomInt(3, 3))
            .map { randomInstrumentString() }
    )

    private fun randomNote() = MusicalNote.fromFloat(randomFreq(), randomStringName(), TestDataFactory.randomInt())

    private fun randomInstrumentString(): InstrumentString {
        val note = randomNote()
        return InstrumentString(
            note.name,
            note.floatFreq,
            note.number,
            note.numberedName
        )
    }
}