package co.ajsf.tuner.model

data class InstrumentNote(
    val numberedName: String,
    val freq: Int
)

data class Instrument(
    val name: String,
    val notes: List<InstrumentNote>
)