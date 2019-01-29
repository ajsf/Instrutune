package co.ajsf.tuner.test.data

import co.ajsf.tuner.model.Instrument
import co.ajsf.tuner.model.InstrumentNote
import co.ajsf.tuner.tuner.notefinder.model.MusicalNote

internal object InstrumentDataFactory {

    fun randomFreq() = TestDataFactory.randomFloat() * 111
    private fun randomStringName() = TestDataFactory.randomString()

    fun randomInstrument() = Instrument(
        name = TestDataFactory.randomString(),
        notes = (0..TestDataFactory.randomInt(3, 3))
            .map { randomInstrumentNote() }
    )

    private fun randomNote() = MusicalNote.fromFloat(randomFreq(), randomStringName())

    private fun randomInstrumentNote(): InstrumentNote {
        val note = randomNote()
        return InstrumentNote(
            note.numberedName,
            note.freq
        )
    }
}