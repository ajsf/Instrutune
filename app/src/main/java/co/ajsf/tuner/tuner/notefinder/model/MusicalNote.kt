package co.ajsf.tuner.tuner.notefinder.model

import kotlin.math.pow
import kotlin.math.roundToInt

data class MusicalNote(val freq: Int, val name: String = "", val number: Int = -1) {

    val floatFreq = freq / 1000f
    val numberedName: String = (name + ((number + 8) / 12).toString())

    fun relativeFreq(steps: Int): Int = (freq * 2.0.pow(steps / 12.0))
        .roundToInt()

    fun toRelativeNote(steps: Int) = MusicalNote(relativeFreq(steps))

    companion object {
        fun fromFloat(floatFreq: Float, name: String = "", number: Int = -1): MusicalNote {

            // Multiply frequencies by 1000 to store as Int.
            // Makes determining the closest note easier.
            return MusicalNote((floatFreq * 1000).roundToInt(), name, number)
        }
    }
}