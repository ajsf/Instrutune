package co.ajsf.tuner.model

import co.ajsf.tuner.tuner.notefinder.model.ChromaticOctave

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

    fun getAllInstruments(offset: Int = 0): List<Instrument> = listOf(
        guitars.map { it.buildInstrument(InstrumentCategory.Guitar, offset) },
        basses.map { it.buildInstrument(InstrumentCategory.Bass, offset) },
        ukuleles.map { it.buildInstrument(InstrumentCategory.Ukulele, offset) },
        tres.map { it.buildInstrument(InstrumentCategory.Tres, offset) }
    ).flatten()

    fun guitar(offset: Int = 0) = guitars.first().buildInstrument(InstrumentCategory.Guitar, offset)
    fun bass(offset: Int = 0) = basses.first().buildInstrument(InstrumentCategory.Bass, offset)
    fun ukulele(offset: Int = 0) = ukuleles.first().buildInstrument(InstrumentCategory.Ukulele, offset)

    fun getDefaultInstrumentForCategory(category: String, offset: Int): Instrument = getAllInstruments(offset)
        .asSequence()
        .filter { it.category.toString() == category }
        .first { it.tuningName == "Standard" }

    private fun InstrumentFactoryModel.buildInstrument(category: InstrumentCategory, offset: Int): Instrument =
        Instrument(category, tuningName, buildNoteList(offset))

    private fun InstrumentFactoryModel.buildNoteList(offset: Int): List<InstrumentNote> {
        val notes = ChromaticOctave.createFullRange(offset)
        return numberedNotes.map { numberedNote ->
            notes.firstOrNull { it.numberedName == numberedNote }
                ?: throw Exception("Invalid note")
        }.map { InstrumentNote(it.numberedName, it.freq) }
    }
}