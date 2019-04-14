package tech.ajsf.instrutune.features.tuner.view

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.tuner_view.view.*
import tech.ajsf.instrutune.R
import tech.ajsf.instrutune.common.tuner.SelectedStringInfo
import tech.ajsf.instrutune.features.tuner.ChromaticViewState

class TunerView
@JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val colorAccentId: Int
    private val textColorId: Int

    init {
        inflate(context, R.layout.tuner_view, this)
        colorAccentId = ContextCompat.getColor(context, R.color.secondaryColor)
        textColorId = ContextCompat.getColor(context, R.color.textColor)
    }

    private val chevrons = mapOf(
        Pair((6..100), chevron_right_one),
        Pair((33..100), chevron_right_two),
        Pair((66..100), chevron_right_three),
        Pair((-100..-6), chevron_left_one),
        Pair((-100..-33), chevron_left_two),
        Pair((-100..-66), chevron_left_three)
    )

    private fun processDelta(delta: Int) {
        chevrons.onEach { (range, view) ->
            val color = if (delta in range) colorAccentId else textColorId
            view.setColorFilter(color)
        }
        if (delta in (-5..5)) {
            note_name_text.setTextColor(colorAccentId)
        } else {
            note_name_text.setTextColor(textColorId)
        }
    }

    fun selectInstrument(names: List<String>) {
        strings_view.setStrings(names)
    }

    fun setChromaticLiveData(
        chromaticLiveData: LiveData<ChromaticViewState>,
        owner: LifecycleOwner
    ) {
        chromaticLiveData.observe(owner, Observer {
            recent_freq_text.text = it.freq
            note_name_text.text = it.noteName
            processDelta(it.delta)
        })
    }

    fun setInstrumentLiveData(
        instrumentLiveData: LiveData<SelectedStringInfo>,
        owner: LifecycleOwner
    ) {
        instrumentLiveData.observe(owner, Observer {
            if (it.numberedName.isNotBlank()) {
                tuner_vu_view.setIndicatorDelta(it.delta)
            } else {
                tuner_vu_view.clearIndicators()
            }
            strings_view.setSelectedString(it.numberedName)
        })
    }
}