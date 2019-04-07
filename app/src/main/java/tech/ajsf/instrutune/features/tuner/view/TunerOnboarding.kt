package tech.ajsf.instrutune.features.tuner.view

import androidx.appcompat.app.AlertDialog
import com.elconfidencial.bubbleshowcase.BubbleShowCaseSequence
import kotlinx.android.synthetic.main.tuner_view.*
import tech.ajsf.instrutune.R
import tech.ajsf.instrutune.common.view.Onboarding
import tech.ajsf.instrutune.features.tuner.TunerActivity

class TunerOnboarding(private val activity: TunerActivity) : Onboarding(activity) {

    fun requestOnboarding() = with(AlertDialog.Builder(activity)) {
        setMessage(activity.getString(R.string.tuner_onboarding_request))
        setPositiveButton(activity.getString(R.string.yes)) { _, _ -> showOnboarding() }
        setNegativeButton(activity.getString(R.string.no)) { _, _ -> }
        setCancelable(false)
    }.show().run { }

    private fun showOnboarding() = with(activity) {
        val vuShowcase = nextArrowBuilder()
            .title(getString(R.string.vu_showcase_text))
            .targetView(tuner_vu_view)

        val chromaticShowcase = nextArrowBuilder()
            .title(getString(R.string.chromatic_showcase_text))
            .targetView(note_name_text)

        val instrumentShowcase = nextArrowBuilder()
            .title(getString(R.string.instrument_showcase_text))
            .targetView(instrument_name_text)

        val middleAShowcase = builder()
            .title(getString(R.string.middleA_showcase_text))
            .targetView(middlea_freq_text)

        BubbleShowCaseSequence()
            .addShowCase(vuShowcase)
            .addShowCase(chromaticShowcase)
            .addShowCase(instrumentShowcase)
            .addShowCase(middleAShowcase)
            .show()
    }
}