package co.ajsf.tuner.model

data class InstrumentNote(
    val numberedName: String,
    val freq: Int
)

data class Instrument(
    val category: InstrumentCategory,
    val tuningName: String,
    val notes: List<InstrumentNote>
)

enum class InstrumentCategory {
    Guitar, Bass, Ukulele, Tres
}