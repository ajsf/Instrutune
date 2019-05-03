package tech.ajsf.instrutune.features.tuner.view

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.animation.doOnCancel
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.tuner_view.view.*
import tech.ajsf.instrutune.R
import tech.ajsf.instrutune.features.tuner.NoteViewState

class TunerView
@JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val colorAccent: Int = ContextCompat.getColor(context, R.color.secondaryColor)
    private val textColor: Int = ContextCompat.getColor(context, R.color.textColor)
    private val disabledColor: Int = ContextCompat.getColor(context, R.color.colorDisabled)

    private var currentTextColor: Int = disabledColor

    private var noteTextAnimation: ObjectAnimator? = null
    private var freqTextAnimation: ObjectAnimator? = null

    private val activeDelay = 80L
    private val inactiveDelay = 160L

    init {
        inflate(context, R.layout.tuner_view, this)
    }

    private val leftChevrons = listOf(
        chevron_left_one, chevron_left_two, chevron_left_three
    )

    private val rightChevrons = listOf(
        chevron_right_one, chevron_right_two, chevron_right_three
    )

    private fun processDelta(delta: Int) {

        when {
            delta < -5 -> {
                activateChevrons(-delta, leftChevrons)
                rightChevrons.forEach { it.setInactive(200, inactiveDelay) }

            }
            delta > 5 -> {
                activateChevrons(delta, rightChevrons)
                leftChevrons.forEach { it.setInactive(200, inactiveDelay) }
            }
            else -> clearChevrons()
        }

        currentTextColor = if (delta in (-5..5)) colorAccent else textColor
        tuner_meter_view.setIndicatorDelta(delta)
        note_name_text.setTextColor(currentTextColor)
    }

    private fun activateChevrons(delta: Int, chevrons: List<Chevron>) {
        val smoothedDelta = smoothChevronDelta(delta)
        chevrons.forEachIndexed { index, chevron ->
            if (index <= smoothedDelta) chevron.setActive(index * activeDelay) else chevron.setInactive(
                100,
                inactiveDelay / 2
            )
        }
    }

    private fun smoothChevronDelta(delta: Int) = Math.min(delta / 33, 2)

    private fun clearChevrons() {
        val lastIndex = leftChevrons.lastIndex
        (0..lastIndex).reversed().forEach {
            val delay = inactiveDelay * (lastIndex - it)
            leftChevrons[it].setInactive(250, delay)
            rightChevrons[it].setInactive(250, delay)
        }
    }

    fun selectInstrument(names: List<String>) {
        strings_view.setStrings(names)
    }

    fun setNoteViewState(
        noteLiveData: LiveData<NoteViewState>,
        owner: LifecycleOwner
    ) {
        noteLiveData.observe(owner, Observer {
            if (it.freqFloat != 0f) {
                recent_freq_text.text = it.freqString
            }

            if (it.numberedName.isNotBlank()) {
                clearAnimations()
                recent_freq_text.setTextColor(textColor)
                note_name_text.setTextColor(textColor)
                note_name_text.text = it.noteName
                processDelta(it.delta)
            } else {
                disableText(textColor)
                tuner_meter_view.clearIndicators()
                clearChevrons()
            }

            strings_view.setSelectedString(it.numberedName)
        })
    }

    fun clearView() {
        disableText()
        recent_freq_text.text = context.getString(R.string.blank_freq)
        note_name_text.text = ""
    }

    private fun disableText(color: Int = currentTextColor) {
        freqTextAnimation = textDisabledAnimation(recent_freq_text, color)
        noteTextAnimation = textDisabledAnimation(note_name_text, currentTextColor)
        currentTextColor = disabledColor
    }

    private fun clearAnimations() {
        noteTextAnimation?.cancel()
        noteTextAnimation = null
        freqTextAnimation?.cancel()
        freqTextAnimation = null
    }

    private fun textDisabledAnimation(view: TextView, startColor: Int): ObjectAnimator {
        return ObjectAnimator.ofInt(view, "textColor", startColor, disabledColor)
            .apply {
                setEvaluator(ArgbEvaluator())
                startDelay = 400
                duration = 1000
                start()
                doOnCancel { view.setTextColor(currentTextColor) }
            }
    }
}