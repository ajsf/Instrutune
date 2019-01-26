package co.ajsf.tuner.model

object InstrumentFactory {
    val GUITAR = Instrument(
        "Guitar", listOf(
            InstrumentString(name = "E", freq = 82.407f),
            InstrumentString(name = "A", freq = 110f),
            InstrumentString(name = "D", freq = 146.832f),
            InstrumentString(name = "G", freq = 195.998f),
            InstrumentString(name = "B", freq = 246.942f),
            InstrumentString(name = "E", freq = 329.628f)
        )
    )

    val BASS = Instrument(
        "Bass", listOf(
            InstrumentString(name = "E", freq = 41.203f),
            InstrumentString(name = "A", freq = 55f),
            InstrumentString(name = "D", freq = 73.416f),
            InstrumentString(name = "G", freq = 97.999f)
        )
    )

    val BASS_5_LOW = Instrument(
        "Bass (5 String Low B)", listOf(
            InstrumentString(name = "B", freq = 30.868f),
            InstrumentString(name = "E", freq = 41.203f),
            InstrumentString(name = "A", freq = 55f),
            InstrumentString(name = "D", freq = 73.416f),
            InstrumentString(name = "G", freq = 97.999f)
        )
    )

    val BASS_5_HI = Instrument(
        "Bass (5 String High C)", listOf(
            InstrumentString(name = "E", freq = 41.203f),
            InstrumentString(name = "A", freq = 55f),
            InstrumentString(name = "D", freq = 73.416f),
            InstrumentString(name = "G", freq = 97.999f),
            InstrumentString(name = "C", freq = 130.813f)
        )
    )

    val BASS_6 = Instrument(
        "Bass (6 String)", listOf(
            InstrumentString(name = "B", freq = 30.868f),
            InstrumentString(name = "E", freq = 41.203f),
            InstrumentString(name = "A", freq = 55f),
            InstrumentString(name = "D", freq = 73.416f),
            InstrumentString(name = "G", freq = 97.999f),
            InstrumentString(name = "C", freq = 130.813f)
        )
    )

    val UKULELE = Instrument(
        "Ukulele (Standard)", listOf(
            InstrumentString(name = "G", freq = 391.995f),
            InstrumentString(name = "C", freq = 261.626f),
            InstrumentString(name = "E", freq = 329.628f),
            InstrumentString(name = "A", freq = 440f)
        )
    )

    val BARITONE_UKULELE = Instrument(
        "Ukulele (Baritone)", listOf(
            InstrumentString(name = "D", freq = 146.832f),
            InstrumentString(name = "G", freq = 195.998f),
            InstrumentString(name = "B", freq = 246.942f),
            InstrumentString(name = "E", freq = 329.628f)
        )
    )

    fun getAllInstruments() = listOf(
        GUITAR, BASS, BASS_5_LOW, BASS_5_HI, BASS_6, UKULELE, BARITONE_UKULELE
    )
}