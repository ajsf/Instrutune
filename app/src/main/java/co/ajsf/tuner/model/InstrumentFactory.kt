package co.ajsf.tuner.model

object InstrumentFactory {
    val GUITAR = Instrument(
        "Guitar", listOf(
            InstrumentString(name = "E", tunedFreq = 82.41f, minFreq = 60f, maxFreq = 96f),
            InstrumentString(name = "A", tunedFreq = 110f, minFreq = 96.01f, maxFreq = 128f),
            InstrumentString(name = "D", tunedFreq = 146.8f, minFreq = 128.01f, maxFreq = 171f),
            InstrumentString(name = "G", tunedFreq = 196f, minFreq = 171.01f, maxFreq = 221f),
            InstrumentString(name = "B", tunedFreq = 246.9f, minFreq = 221.01f, maxFreq = 287f),
            InstrumentString(name = "E", tunedFreq = 329.6f, minFreq = 287.01f, maxFreq = 371f)
        )
    )

    val BASS = Instrument(
        "Bass", listOf(
            InstrumentString(name = "E", tunedFreq = 41.2f, minFreq = 30f, maxFreq = 48.55f),
            InstrumentString(name = "A", tunedFreq = 55f, minFreq = 48.56f, maxFreq = 64.21f),
            InstrumentString(name = "D", tunedFreq = 73.42f, minFreq = 64.22f, maxFreq = 86.61f),
            InstrumentString(name = "G", tunedFreq = 98f, minFreq = 86.82f, maxFreq = 110f)
        )
    )
}