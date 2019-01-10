package co.ajsf.tuner.model

data class InstrumentString(
    val name: String,
    val tunedFreq: Float,
    val minFreq: Float,
    val maxFreq: Float
)

data class Instrument(val name: String, val strings: List<InstrumentString>)

data class StringData(val number: Int, val delta: Int)

val NO_STRING: StringData = StringData(-1, -1)

fun Instrument.findClosestString(freq: Float): StringData {
    if (freq < 0) return NO_STRING
    strings.forEachIndexed { index, string ->
        if (string.minFreq <= freq && string.maxFreq >= freq) {
            val initialDelta = (freq - string.tunedFreq).toInt()
            val delta = if (initialDelta > 0) {
                minOf(((initialDelta * 100) / (string.maxFreq - string.tunedFreq)).toInt(), 75)
            } else {
                maxOf(((initialDelta * 100) / (string.tunedFreq - string.minFreq)).toInt(), -75)
            }
            return StringData(index, delta)
        }
    }
    return NO_STRING
}