package co.ajsf.tuner.model

object InstrumentFactory {
    val GUITAR = Instrument(
        "Guitar", listOf(
            InstrumentString(name = "E", freq = 82.41f),
            InstrumentString(name = "A", freq = 110f),
            InstrumentString(name = "D", freq = 146.8f),
            InstrumentString(name = "G", freq = 196f),
            InstrumentString(name = "B", freq = 246.9f),
            InstrumentString(name = "E", freq = 329.6f)
        )
    )

    val BASS = Instrument(
        "Bass", listOf(
            InstrumentString(name = "E", freq = 41.2f),
            InstrumentString(name = "A", freq = 55f),
            InstrumentString(name = "D", freq = 73.42f),
            InstrumentString(name = "G", freq = 98f)
        )
    )
}