package tech.ajsf.instrutune.common.tuner

import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import tech.ajsf.instrutune.common.model.Instrument
import tech.ajsf.instrutune.common.tuner.frequencydetection.FrequencyDetector
import tech.ajsf.instrutune.common.tuner.notefinder.NoteFinder

sealed class TunerMode
object InstrumentMode : TunerMode()
object ChromaticMode : TunerMode()

data class NoteInfo(
    val numberedName: String = "",
    val name: String = " ",
    val delta: Int = 0,
    val freq: Float = 0f
)

class Tuner(
    private val frequencyDetector: FrequencyDetector,
    private val mapper: DetectionToNoteMapper,
    private val scheduler: Scheduler = Schedulers.computation()
) {

    var mode: TunerMode = InstrumentMode

    private var chromaticNoteFinder: NoteFinder = NoteFinder.chromaticNoteFinder(0)
    private var instrumentNoteFinder: NoteFinder = chromaticNoteFinder

    fun getTunerFlow(): Flowable<NoteInfo> {
        return frequencyDetector
            .listen()
            .observeOn(scheduler)
            .map {
                val finder = when (mode) {
                    InstrumentMode -> instrumentNoteFinder
                    else -> chromaticNoteFinder
                }
                mapper.map(it, finder.findNote(it))
            }
    }

    fun configTuner(instrument: Instrument, offset: Int) {
        chromaticNoteFinder = NoteFinder.chromaticNoteFinder(offset)
        instrumentNoteFinder = NoteFinder.instrumentNoteFinder(instrument, offset)
    }
}