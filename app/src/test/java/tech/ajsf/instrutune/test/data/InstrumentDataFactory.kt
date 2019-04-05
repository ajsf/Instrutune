package tech.ajsf.instrutune.test.data

import tech.ajsf.instrutune.common.model.Instrument
import tech.ajsf.instrutune.common.model.InstrumentCategory
import tech.ajsf.instrutune.common.model.InstrumentNote
import tech.ajsf.instrutune.common.tuner.SelectedNoteInfo
import tech.ajsf.instrutune.common.tuner.SelectedStringInfo
import tech.ajsf.instrutune.common.tuner.notefinder.model.MusicalNote

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