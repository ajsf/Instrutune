package co.ajsf.tuner.view.tunerview

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import co.ajsf.tuner.R
import co.ajsf.tuner.tuner.SelectedStringInfo
import kotlinx.android.synthetic.main.tuner_view.view.*

class TunerView
@JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val colorAccentId: Int
    private val textColorId: Int

    init {
        LinearLayout.inflate(context, R.layout.tuner_view, this)
        colorAccentId = ContextCompat.getColor(context, R.color.colorAccent)
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

    fun selectInstrument(numberedNames: List<String>, stringLiveData: LiveData<SelectedStringInfo>) {
        stringLiveData.observeForever {
            if (it.numberedName.isNotBlank()) {
                tuner_vu_view.setIndicatorVisibility(true)
                tuner_vu_view.setIndicatorDelta(it.delta)
            } else {
                tuner_vu_view.setIndicatorVisibility(false)
            }
        }
        strings_view.setStrings(numberedNames, Transformations.map(stringLiveData) { it.numberedName })
    }

    fun setChromaticDeltaLiveData(deltaLiveData: LiveData<Int>) = observers
        .onEach { deltaLiveData.observeForever(it) }

    fun setFreq(freq: String) {
        recent_freq_text.text = freq
    }

    fun setNoteName(noteName: String) {
        note_name_text.text = noteName
    }
}
