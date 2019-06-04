package tech.ajsf.instrutune.common.data.mapper

import tech.ajsf.instrutune.common.data.db.InstrumentEntity
import tech.ajsf.instrutune.common.model.Instrument
import tech.ajsf.instrutune.common.model.InstrumentCategory
import tech.ajsf.instrutune.common.model.InstrumentNote
import tech.ajsf.instrutune.common.tuner.notefinder.model.ChromaticOctave

interface InstrumentMapper {
    fun toInstrument(entity: InstrumentEntity): Instrument
    fun toInstrumentList(entities: List<InstrumentEntity>): List<Instrument>
}

class EntityToInstrumentMapper : InstrumentMapper {

    override fun toInstrument(entity: InstrumentEntity): Instrument =
        entity.buildInstrument()

    override fun toInstrumentList(entities: List<InstrumentEntity>) =
        entities.map { it.buildInstrument() }

    private fun InstrumentEntity.buildInstrument(): Instrument =
        Instrument(InstrumentCategory.valueOf(category), tuningName, buildNoteList(), id)

    private fun InstrumentEntity.buildNoteList(): List<InstrumentNote> {
        val notes = ChromaticOctave.createFullRange()
        return numberedNotes.map { numberedNote ->
            notes.firstOrNull { it.numberedName == numberedNote }
                ?: throw RuntimeException("Invalid note")
        }.map { InstrumentNote(it.numberedName, it.freq) }
    }
}