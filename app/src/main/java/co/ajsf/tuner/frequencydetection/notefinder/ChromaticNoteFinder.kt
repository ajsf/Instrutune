package co.ajsf.tuner.frequencydetection.notefinder

import co.ajsf.tuner.model.Octave

class ChromaticNoteFinder : NoteFinder() {

    private val octaves = (-4..2).map { Octave(it) }

    override fun findNote(freq: Float): String {
        val freqInt = (freq * 100).toInt()
        octaves.forEach {
            if (freqInt in it.frequencyRange) {
                return when (freqInt) {
                    in (it.frequencyRange.start..it.notes.first().freq) -> it.notes.first().name
                    in (it.frequencyRange.last..it.notes.last().freq) -> it.notes.last().name
                    else -> findNote(freqInt, it.notes)
                }
            }
        }
        return ""
    }
}