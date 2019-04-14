package tech.ajsf.instrutune.test.data

import tech.ajsf.instrutune.common.data.db.InstrumentEntity
import tech.ajsf.instrutune.common.model.Instrument
import tech.ajsf.instrutune.common.model.InstrumentCategory
import tech.ajsf.instrutune.common.model.InstrumentNote
import tech.ajsf.instrutune.common.tuner.SelectedNoteInfo
import tech.ajsf.instrutune.common.tuner.SelectedStringInfo
import tech.ajsf.instrutune.common.tuner.notefinder.model.MusicalNote
import tech.ajsf.instrutune.test.data.TestDataFactory.randomFloat
import tech.ajsf.instrutune.test.data.TestDataFactory.randomInt
import tech.ajsf.instrutune.test.data.TestDataFactory.randomString

object InstrumentDataFactory {

    fun randomFreq() = randomFloat() * 111

    fun randomFreqList() = randomList(randomInt(20)) {
        randomFloat().toString()
    }

    fun randomCategoryList() = randomList(10, ::randomString)

    fun randomInstrument() = Instrument(
        category = randomCategory(),
        tuningName = randomString(),
        notes = (0..randomInt(3, 3))
            .map { randomInstrumentNote() },
        id = randomInt()
    )

    fun randomInstrumentList(): List<Instrument> =
        randomList(randomInt(), ::randomInstrument)

 fun randomInstrumentEntityList(): List<InstrumentEntity> =
        randomList(randomInt(), ::randomInstrumentEntity)

    private fun randomInstrumentEntity() = InstrumentEntity(
        category = randomCategory().toString(),
        tuningName = randomString(),
        numberedNotes = randomList(randomInt(8), TestDataFactory::randomString),
        id = randomInt()
    )

    fun randomStringInfoList(size: Int = randomInt(20)) =
        randomList(size) {
            SelectedStringInfo(
                randomString(),
                randomFloat()
            )
        }

    fun randomNoteInfoList(size: Int = randomInt(20)) =
        randomList(size) { SelectedNoteInfo(randomString(), randomInt(), randomString()) }

    private fun randomStringName() = randomString()

    private fun randomCategory(): InstrumentCategory {
        val categories = InstrumentCategory.values()
        val num = randomInt(categories.lastIndex)
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