package co.ajsf.tuner.model

import co.ajsf.tuner.tuner.notefinder.model.ChromaticOctave

private data class InstrumentFactoryModel(
    val tuningName: String,
    val numberedNotes: List<String>
)

object InstrumentFactory {

    private val notes = ChromaticOctave.createFullRange()

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

    fun getAllInstruments(): List<Instrument> = listOf(
        guitars.map { it.buildInstrument(InstrumentCategory.Guitar) },
        basses.map { it.buildInstrument(InstrumentCategory.Bass) },
        ukuleles.map { it.buildInstrument(InstrumentCategory.Ukulele) },
        tres.map { it.buildInstrument(InstrumentCategory.Tres) }
    ).flatten()

    fun guitar() = guitars.first().buildInstrument(InstrumentCategory.Guitar)
    fun bass() = basses.first().buildInstrument(InstrumentCategory.Bass)
    fun ukulele() = ukuleles.first().buildInstrument(InstrumentCategory.Ukulele)

    fun getDefaultInstrumentForCategory(category: String): Instrument = getAllInstruments()
        .asSequence()
        .filter { it.category.toString() == category }
        .first { it.tuningName == "Standard" }

    private fun InstrumentFactoryModel.buildInstrument(category: InstrumentCategory): Instrument =
        Instrument(category, tuningName, buildNoteList())

    private fun InstrumentFactoryModel.buildNoteList(): List<InstrumentNote> =
        numberedNotes.map { numberedNote ->
            notes.firstOrNull { it.numberedName == numberedNote }
                ?: throw Exception("Invalid note")
        }.map { InstrumentNote(it.numberedName, it.freq) }
}