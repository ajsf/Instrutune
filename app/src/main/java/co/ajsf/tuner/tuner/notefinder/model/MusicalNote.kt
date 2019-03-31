package co.ajsf.tuner.tuner.notefinder.model

import kotlin.math.pow
import kotlin.math.roundToInt

data class MusicalNote(val freq: Int, val numberedName: String = "") {

    val floatFreq = freq / 1000f

    fun relativeFreq(steps: Int): Int = (freq * 2.0.pow(steps / 12.0))
        .roundToInt()

    fun toRelativeNote(steps: Int) = MusicalNote(relativeFreq(steps))

    companion object {
        fun fromFloat(floatFreq: Float, numberedName: String = ""): MusicalNote {

            // Multiply frequencies by 1000 to store as Int.
            // Makes determining the closest note easier.
            return MusicalNote((floatFreq * 1000).roundToInt(), numberedName)
        }

        fun fromFloatAndName(floatFreq: Float, name: String, number: Int): MusicalNote {
            val numberedName = createNumberedName(name, number)
            return MusicalNote.fromFloat(floatFreq, numberedName)
        }

        fun fromName(freq: Int, name: String, number: Int): MusicalNote {
            val numberedName = createNumberedName(name, number)
            return MusicalNote(freq, numberedName)
        }

        fun nameFromNumberedName(numberedName: String): String = numberedName
            .split(("""\d""").toRegex()).first()

        fun nameAndNumberFromNumberedName(numberedName: String): Pair<String, Int> =
            nameFromNumberedName(numberedName) to numberFromNumberedName(numberedName)

        private fun numberFromNumberedName(numberedName: String): Int =
            ("""\d""".toRegex().find(numberedName)?.value ?: "4").toInt()

        private fun createNumberedName(name: String, number: Int): String =
            (name + ((number + 8) / 12).toString())
    }
}