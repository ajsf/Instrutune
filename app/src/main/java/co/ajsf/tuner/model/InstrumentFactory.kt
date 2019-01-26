package co.ajsf.tuner.model

object InstrumentFactory {
    val GUITAR = Instrument(
        "Guitar", listOf(
            InstrumentString(name = "E", freq = 82.41f),
            InstrumentString(name = "A", freq = 110f),
            InstrumentString(name = "D", freq = 146.83f),
            InstrumentString(name = "G", freq = 196f),
            InstrumentString(name = "B", freq = 246.94f),
            InstrumentString(name = "E", freq = 329.63f)
        )
    )

    val BASS = Instrument(
        "Bass", listOf(
            InstrumentString(name = "E", freq = 41.204f),
            InstrumentString(name = "A", freq = 55f),
            InstrumentString(name = "D", freq = 73.416f),
            InstrumentString(name = "G", freq = 97.999f)
        )
    )

    val BASS_5_LOW = Instrument(
        "Bass (5 String Low B)", listOf(
            InstrumentString(name = "B", freq = 30.8f),
            InstrumentString(name = "E", freq = 41.204f),
            InstrumentString(name = "A", freq = 55f),
            InstrumentString(name = "D", freq = 73.416f),
            InstrumentString(name = "G", freq = 97.999f)
        )
    )

    val BASS_5_HI = Instrument(
        "Bass (5 String High C)", listOf(
            InstrumentString(name = "E", freq = 41.204f),
            InstrumentString(name = "A", freq = 55f),
            InstrumentString(name = "D", freq = 73.416f),
            InstrumentString(name = "G", freq = 97.999f),
            InstrumentString(name = "C", freq = 130.83f)
        )
    )

    val BASS_6 = Instrument(
        "Bass (6 String)", listOf(
            InstrumentString(name = "B", freq = 30.8f),
            InstrumentString(name = "E", freq = 41.204f),
            InstrumentString(name = "A", freq = 55f),
            InstrumentString(name = "D", freq = 73.416f),
            InstrumentString(name = "G", freq = 97.999f),
            InstrumentString(name = "C", freq = 130.83f)
        )
    )

    val UKULELE = Instrument(
        "Ukulele (Standard)", listOf(
            InstrumentString(name = "G", freq = 392f),
            InstrumentString(name = "C", freq = 261.63f),
            InstrumentString(name = "E", freq = 329.63f),
            InstrumentString(name = "A", freq = 440f)
        )
    )

    val BARITONE_UKULELE = Instrument(
        "Ukulele (Baritone)", listOf(
            InstrumentString(name = "D", freq = 146.83f),
            InstrumentString(name = "G", freq = 196f),
            InstrumentString(name = "B", freq = 246.94f),
            InstrumentString(name = "E", freq = 329.63f)
        )
    )

    fun getAllInstruments() = listOf(
        GUITAR, BASS, BASS_5_LOW, BASS_5_HI, BASS_6, UKULELE, BARITONE_UKULELE
    )
}