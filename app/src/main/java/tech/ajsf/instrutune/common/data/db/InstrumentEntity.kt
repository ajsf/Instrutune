package tech.ajsf.instrutune.common.data.db

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import tech.ajsf.instrutune.common.model.Instrument
import tech.ajsf.instrutune.common.model.InstrumentCategory
import tech.ajsf.instrutune.common.model.InstrumentNote
import tech.ajsf.instrutune.common.tuner.notefinder.model.ChromaticOctave

@Entity(tableName = "instrument", indices = [Index(value = ["category"])])
data class InstrumentEntity(
    val tuningName: String,
    val category: String,
    val numberedNotes: List<String>,
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null
)

fun InstrumentEntity.buildInstrument(offset: Int): Instrument =
    Instrument(InstrumentCategory.valueOf(category), tuningName, buildNoteList(offset), id)

private fun InstrumentEntity.buildNoteList(offset: Int): List<InstrumentNote> {
    val notes = ChromaticOctave.createFullRange(offset)
    return numberedNotes.map { numberedNote ->
        notes.firstOrNull { it.numberedName == numberedNote }
            ?: throw Exception("Invalid note")
    }.map { InstrumentNote(it.numberedName, it.freq) }
}