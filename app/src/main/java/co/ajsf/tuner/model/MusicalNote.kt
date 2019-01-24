package co.ajsf.tuner.model

import kotlin.math.pow
import kotlin.math.roundToInt

data class MusicalNote(val freq: Int, val name: String) {

    fun relativeFreq(steps: Int): Int = (freq * 2.0.pow(steps / 12.0))
        .roundToInt()

    companion object {
        fun fromFloat(floatFreq: Float, name: String = ""): MusicalNote {

            // Multiply frequencies by 100 to store as Int. Makes determining the closest note easier.
            return MusicalNote((floatFreq * 100).toInt(), name)
        }
    }
}