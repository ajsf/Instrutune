package tech.ajsf.instrutune.common.data.mapper

import tech.ajsf.instrutune.common.data.db.InstrumentEntity
import tech.ajsf.instrutune.common.model.Instrument
import tech.ajsf.instrutune.common.model.InstrumentCategory
import tech.ajsf.instrutune.common.model.InstrumentNote
import tech.ajsf.instrutune.common.tuner.notefinder.model.ChromaticOctave

interface InstrumentMapper {
    fun toInstrument(entity: InstrumentEntity, offset: Int = 0): Instrument
    fun toInstrumentList(entities: List<InstrumentEntity>, offset: Int = 0): List<Instrument>
}

class EntityToInstrumentMapper : InstrumentMapper {

    override fun toInstrument(entity: InstrumentEntity, offset: Int): Instrument =
        entity.buildInstrument(offset)

    override fun toInstrumentList(entities: List<InstrumentEntity>, offset: Int) =
        entities.map { it.buildInstrument(offset) }

    private fun InstrumentEntity.buildInstrument(offset: Int): Instrument =
        Instrument(InstrumentCategory.valueOf(category), tuningName, buildNoteList(offset), id)

    private fun InstrumentEntity.buildNoteList(offset: Int): List<InstrumentNote> {
        val notes = ChromaticOctave.createFullRange(offset)
        return numberedNotes.map { numberedNote ->
            notes.firstOrNull { it.numberedName == numberedNote }
                ?: throw RuntimeException("Invalid note")
        }.map { InstrumentNote(it.numberedName, it.freq) }
    }
}