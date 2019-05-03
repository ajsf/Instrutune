package tech.ajsf.instrutune.common.tuner

import tech.ajsf.instrutune.common.tuner.notefinder.NoteData
import tech.ajsf.instrutune.common.tuner.notefinder.model.MusicalNote

class DetectionToNoteMapper {

    fun map(freq: Float, data: NoteData): NoteInfo {
        return if (freq == -1f) {
            NoteInfo()
        } else {
            val name = MusicalNote.nameFromNumberedName(data.numberedName)
            NoteInfo(data.numberedName, name, data.delta, freq)
        }
    }

}