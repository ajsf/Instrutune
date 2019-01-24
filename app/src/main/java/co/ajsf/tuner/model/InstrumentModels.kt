package co.ajsf.tuner.model

data class InstrumentString(
    val name: String,
    val freq: Float
)

data class Instrument(
    val name: String,
    val strings: List<InstrumentString>
)