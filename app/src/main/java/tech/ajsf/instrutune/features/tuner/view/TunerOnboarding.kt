package tech.ajsf.instrutune.features.tuner.view

import androidx.appcompat.app.AlertDialog
import com.elconfidencial.bubbleshowcase.BubbleShowCase
import com.elconfidencial.bubbleshowcase.BubbleShowCaseListener
import com.elconfidencial.bubbleshowcase.BubbleShowCaseSequence
import kotlinx.android.synthetic.main.tuner_view.*
import tech.ajsf.instrutune.R
import tech.ajsf.instrutune.common.view.Onboarding
import tech.ajsf.instrutune.features.tuner.TunerActivity

class TunerOnboarding(private val activity: TunerActivity) : Onboarding(activity) {

    override fun requestOnboarding() = with(AlertDialog.Builder(activity)) {
        setMessage(activity.getString(R.string.tuner_onboarding_request))
        setPositiveButton(activity.getString(R.string.yes)) { _, _ -> showOnboarding() }
        setNegativeButton(activity.getString(R.string.no)) { _, _ -> }
        setCancelable(false)
        finishBuilding()
    }.show()

    private fun showOnboarding() = with(activity) {
        val modeShowcase = nextArrowBuilder()
            .title(getString(R.string.mode_showcase_text))
            .targetView(mode_button)

        val meterShowcase = nextArrowBuilder()
            .title(getString(R.string.meter_showcase_text))
            .targetView(tuner_meter_view)

        val noteShowcase = nextArrowBuilder()
            .title(getString(R.string.note_name_showcase))
            .targetView(note_name_text)
            .listener(object : BubbleShowCaseListener {
                override fun onBackgroundDimClick(bubbleShowCase: BubbleShowCase) {
                    note_name_text.text = " "
                }

                override fun onBubbleClick(bubbleShowCase: BubbleShowCase) {}
                override fun onCloseActionImageClick(bubbleShowCase: BubbleShowCase) {
                    note_name_text.text = " "
                }

                override fun onTargetClick(bubbleShowCase: BubbleShowCase) {
                    note_name_text.text = " "
                }
            })

        val instrumentShowcase = nextArrowBuilder()
            .title(getString(R.string.instrument_showcase_text))
            .targetView(instrument_name_text)

        val middleAShowcase = builder()
            .title(getString(R.string.middleA_showcase_text))
            .targetView(middlea_freq_text)

        note_name_text.text = "A"

        BubbleShowCaseSequence()
            .addShowCase(modeShowcase)
            .addShowCase(meterShowcase)
            .addShowCase(noteShowcase)
            .addShowCase(instrumentShowcase)
            .addShowCase(middleAShowcase)
            .show()

    }
}