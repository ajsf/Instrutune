package co.ajsf.tuner.common.data

import co.ajsf.tuner.common.data.db.InstrumentEntity
import co.ajsf.tuner.common.model.Instrument
import co.ajsf.tuner.common.model.InstrumentCategory
import co.ajsf.tuner.common.model.InstrumentNote
import co.ajsf.tuner.common.tuner.notefinder.model.ChromaticOctave

private data class InstrumentFactoryModel(
    val tuningName: String,
    val numberedNotes: List<String>
)

object InstrumentFactory {

    private val guitars: List<InstrumentFactoryModel> = listOf(
        InstrumentFactoryModel("Standard", listOf("E2", "A2", "D3", "G3", "B3", "E4")),
        InstrumentFactoryModel("Drop D", listOf("D2", "A2", "D3", "G3", "B3", "E4")),
        InstrumentFactoryModel("Open D", listOf("D2", "A2", "D3", "F#3", "A3", "D4")),
        InstrumentFactoryModel("Open G", listOf("D2", "G2", "D3", "G3", "B3", "D4"))
    )

    private val basses: List<InstrumentFactoryModel> = listOf(
        InstrumentFactoryModel("Standard", listOf("E1", "A1", "D2", "G2")),
        InstrumentFactoryModel("5 String - Low B", listOf("B0", "E1", "A1", "D2", "G2")),
        InstrumentFactoryModel("5 String - High C", listOf("E1", "A1", "D2", "G2", "C3")),
        InstrumentFactoryModel("6 String", listOf("B0", "E1", "A1", "D2", "G2", "C3"))
    )

    private val ukuleles: List<InstrumentFactoryModel> = listOf(
        InstrumentFactoryModel("Standard", listOf("G4", "C4", "E4", "A4")),
        InstrumentFactoryModel("Baritone", listOf("D3", "G3", "B3", "E4"))
    )

    private val tres: List<InstrumentFactoryModel> = listOf(
        InstrumentFactoryModel("Standard", listOf("G4", "G3", "C4", "C4", "E4", "E4")),
        InstrumentFactoryModel("Oriente - C", listOf("G4", "G3", "C4", "C4", "E3", "E4")),
        InstrumentFactoryModel("Standard - D", listOf("A4", "A3", "D4", "D4", "F#4", "F#4")),
        InstrumentFactoryModel("Oriente - D", listOf("A4", "A3", "D4", "D4", "F#3", "F#4"))
    )

    private val strings: List<InstrumentFactoryModel> = listOf(
        InstrumentFactoryModel("Violin", listOf("G3", "D4", "A4", "E5")),
        InstrumentFactoryModel("Viola", listOf("C3", "G3", "D4", "A4")),
        InstrumentFactoryModel("Cello", listOf("C2", "G2", "D3", "A3"))
    )

    fun getDefaultEntities(): List<InstrumentEntity> = listOf(
        guitars.toEntities(InstrumentCategory.Guitar),
        basses.toEntities(InstrumentCategory.Bass),
        ukuleles.toEntities(InstrumentCategory.Ukulele),
        tres.toEntities(InstrumentCategory.Tres),
        strings.toEntities(InstrumentCategory.Strings)
    ).flatten()

    private fun List<InstrumentFactoryModel>.toEntities(category: InstrumentCategory) = map {
        InstrumentEntity(it.tuningName, category.toString(), it.numberedNotes)
    }

    fun buildInstrumentsFromEntities(entities: List<InstrumentEntity>, offset: Int = 0): List<Instrument> =
        entities.map { it.buildInstrument(offset) }

    private fun InstrumentEntity.buildInstrument(offset: Int): Instrument =
        Instrument(InstrumentCategory.valueOf(category), tuningName, buildNoteList(offset))

    private fun InstrumentEntity.buildNoteList(offset: Int): List<InstrumentNote> {
        val notes = ChromaticOctave.createFullRange(offset)
        return numberedNotes.map { numberedNote ->
            notes.firstOrNull { it.numberedName == numberedNote }
                ?: throw Exception("Invalid note")
        }.map { InstrumentNote(it.numberedName, it.freq) }
    }
}