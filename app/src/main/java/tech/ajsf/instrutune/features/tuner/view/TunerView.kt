package tech.ajsf.instrutune.features.tuner.view

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import tech.ajsf.instrutune.R
import tech.ajsf.instrutune.common.tuner.SelectedStringInfo
import kotlinx.android.synthetic.main.tuner_view.view.*

class TunerView
@JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val colorAccentId: Int
    private val textColorId: Int

    init {
        LinearLayout.inflate(context, R.layout.tuner_view, this)
        colorAccentId = ContextCompat.getColor(context, R.color.secondaryColor)
        textColorId = ContextCompat.getColor(context, R.color.textColor)
    }

    private val noteNameObserver = Observer<Int> {
        if (it in (-5..5)) {
            note_name_text.setTextColor(colorAccentId)
        } else {
            note_name_text.setTextColor(textColorId)
        }
    }

    private val observers = listOf(
        noteNameObserver,
        chevronObserver((6..100), chevron_right_one),
        chevronObserver((33..100), chevron_left_two),
        chevronObserver((66..100), chevron_left_three),
        chevronObserver((-100..-6), chevron_left_one),
        chevronObserver((-100..-33), chevron_left_two),
        chevronObserver((-100..-66), chevron_left_three)
    )

    private fun chevronObserver(selectedRange: IntRange, chevronView: ImageView) = Observer<Int> {
        val color = when (it) {
            in selectedRange -> colorAccentId
            else -> textColorId
        }
        chevronView.setColorFilter(color)
    }

    fun selectInstrument(names: List<String>, stringData: LiveData<SelectedStringInfo>, owner: LifecycleOwner) {
        stringData.observe(owner, Observer {
            if (it.numberedName.isNotBlank()) {
                tuner_vu_view.setIndicatorVisibility(true)
                tuner_vu_view.setIndicatorDelta(it.delta)
            } else {
                tuner_vu_view.setIndicatorVisibility(false)
            }
            strings_view.setSelectedString(it.numberedName)
        })
        strings_view.setStrings(names)
    }

    fun setChromaticDeltaLiveData(deltaLiveData: LiveData<Int>, owner: LifecycleOwner) = observers
        .onEach { deltaLiveData.observe(owner, it) }

    fun setFreqLiveData(freqLiveData: LiveData<String>, owner: LifecycleOwner) = freqLiveData
        .observe(owner, Observer { recent_freq_text.text = it })

    fun setNoteNameLiveData(noteNameLiveData: LiveData<String>, owner: LifecycleOwner) = noteNameLiveData
        .observe(owner, Observer { note_name_text.text = it })
}