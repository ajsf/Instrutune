package co.ajsf.tuner.test.data

import co.ajsf.tuner.model.Instrument
import co.ajsf.tuner.model.InstrumentCategory
import co.ajsf.tuner.model.InstrumentNote
import co.ajsf.tuner.tuner.SelectedNoteInfo
import co.ajsf.tuner.tuner.SelectedStringInfo
import co.ajsf.tuner.tuner.notefinder.model.MusicalNote

object InstrumentDataFactory {

    fun randomFreq() = TestDataFactory.randomFloat() * 111

    fun randomFreqList() = randomList(TestDataFactory.randomInt(20)) { TestDataFactory.randomFloat().toString() }

    fun randomInstrument() = Instrument(
        category = randomCategory(),
        tuningName = TestDataFactory.randomString(),
        notes = (0..TestDataFactory.randomInt(3, 3))
            .map { randomInstrumentNote() }
    )

    fun randomStringInfoList(size: Int = TestDataFactory.randomInt(20)) =
        randomList(size) {
            SelectedStringInfo(
                TestDataFactory.randomString(),
                TestDataFactory.randomFloat()
            )
        }

    fun randomNoteInfoList(size: Int = TestDataFactory.randomInt(20)) =
        randomList(size) {
            SelectedNoteInfo(
                TestDataFactory.randomString(),
                TestDataFactory.randomInt()
            )
        }

    private fun randomStringName() = TestDataFactory.randomString()

    private fun randomCategory(): InstrumentCategory {
        val categories = InstrumentCategory.values()
        val num = TestDataFactory.randomInt(categories.lastIndex)
        return InstrumentCategory.values()[num]
    }

    private fun randomNote() = MusicalNote.fromFloat(
        randomFreq(),
        randomStringName()
    )

    private fun randomInstrumentNote(): InstrumentNote {
        val note = randomNote()
        return InstrumentNote(
            note.numberedName,
            note.freq
        )
    }


    private fun <T> randomList(size: Int, creator: () -> T) = (0 until size).map {
        creator.invoke()
    }
}