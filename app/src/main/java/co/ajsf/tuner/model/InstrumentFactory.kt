package co.ajsf.tuner.model

import co.ajsf.tuner.tuner.notefinder.model.ChromaticOctave

private data class InstrumentFactoryModel(
    val name: String,
    val numberedNotes: List<String>
)

object InstrumentFactory {

    private val notes = ChromaticOctave.createFullRange()

    private val GUITAR = InstrumentFactoryModel("Guitar", listOf("E2", "A2", "D3", "G3", "B3", "E4"))
    private val GUITAR_DROP_D = InstrumentFactoryModel("Guitar - Drop D", listOf("D2", "A2", "D3", "G3", "B3", "E4"))
    private val GUITAR_OPEN_D = InstrumentFactoryModel("Guitar - Open D", listOf("D2", "A2", "D3", "F#3", "A3", "D4"))
    private val GUITAR_OPEN_G = InstrumentFactoryModel("Guitar - Open G", listOf("D2", "G2", "D3", "G3", "B3", "D4"))

    private val BASS = InstrumentFactoryModel("Bass", listOf("E1", "A1", "D2", "G2"))
    private val BASS_5_LOW = InstrumentFactoryModel("Bass (5 String Low B)", listOf("B0", "E1", "A1", "D2", "G2"))
    private val BASS_5_HIGH = InstrumentFactoryModel("Bass (5 String High C)", listOf("E1", "A1", "D2", "G2", "C3"))
    private val BASS_6 = InstrumentFactoryModel("Bass (6 String)", listOf("B0", "E1", "A1", "D2", "G2", "C3"))

    private val UKULELE = InstrumentFactoryModel("Ukulele", listOf("G4", "C4", "E4", "A4"))
    private val BARITONE_UKULELE = InstrumentFactoryModel("Ukulele (Baritone)", listOf("D3", "G3", "B3", "E4"))

    private val TRES = InstrumentFactoryModel("Tres", listOf("G4", "G3", "C4", "C4", "E4", "E4"))
    private val TRES_ORIENTE = InstrumentFactoryModel("Tres - Oriente", listOf("G4", "G3", "C4", "C4", "E3", "E4"))

    private val TRES_D = InstrumentFactoryModel("Tres (D)", listOf("A4", "A3", "D4", "D4", "F#4", "F#4"))
    private val TRES_D_ORIENTE =
        InstrumentFactoryModel("Tres - Oriente (D)", listOf("A4", "A3", "D4", "D4", "F#3", "F#4"))


    private val instrumentModels: List<InstrumentFactoryModel> = listOf(
        GUITAR,
        GUITAR_DROP_D,
        GUITAR_OPEN_D,
        GUITAR_OPEN_G,
        BASS,
        BASS_5_LOW,
        BASS_5_HIGH,
        BASS_6,
        UKULELE,
        BARITONE_UKULELE,
        TRES,
        TRES_ORIENTE,
        TRES_D,
        TRES_D_ORIENTE
    )

    fun getAllInstruments(): List<Instrument> = instrumentModels
        .map { it.buildInstrument() }

    fun guitar() = GUITAR.buildInstrument()
    fun bass() = BASS.buildInstrument()
    fun ukulele() = UKULELE.buildInstrument()

    private fun InstrumentFactoryModel.buildInstrument(): Instrument =
        Instrument(name, buildNoteList())

    private fun InstrumentFactoryModel.buildNoteList(): List<InstrumentNote> =
        numberedNotes.map { numberedNote ->
            notes.firstOrNull { it.numberedName == numberedNote }
                ?: throw Exception("Invalid note")
        }.map { InstrumentNote(it.numberedName, it.freq) }
}