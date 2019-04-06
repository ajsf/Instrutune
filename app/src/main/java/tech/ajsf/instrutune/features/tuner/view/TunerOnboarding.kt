package tech.ajsf.instrutune.features.tuner.view

import androidx.appcompat.app.AlertDialog
import com.elconfidencial.bubbleshowcase.BubbleShowCaseBuilder
import com.elconfidencial.bubbleshowcase.BubbleShowCaseSequence
import kotlinx.android.synthetic.main.tuner_view.*
import tech.ajsf.instrutune.R
import tech.ajsf.instrutune.features.tuner.TunerActivity

class TunerOnboarding(private val activity: TunerActivity) {

    fun requestOnboarding() = with(AlertDialog.Builder(activity)) {
        setMessage(activity.getString(R.string.request_onboarding))
        setPositiveButton(activity.getString(R.string.yes)) { _, _ -> showOnboarding() }
        setNegativeButton(activity.getString(R.string.no)) { _, _ -> }
    }.show().run { }

    private fun showOnboarding() = with(activity) {
        val vuShowcase = BubbleShowCaseBuilder(this)
            .title(getString(R.string.vu_showcase_text))
            .targetView(tuner_vu_view)
            .backgroundColorResourceId(R.color.colorPrimaryDark)

        val chromaticShowcase = BubbleShowCaseBuilder(this)
            .title(getString(R.string.chromatic_showcase_text))
            .targetView(note_name_text)
            .backgroundColorResourceId(R.color.colorPrimaryDark)

        val instrumentShowcase = BubbleShowCaseBuilder(this)
            .title(getString(R.string.instrument_showcase_text))
            .targetView(instrument_name_text)
            .backgroundColorResourceId(R.color.colorPrimaryDark)

        val middleAShowcase = BubbleShowCaseBuilder(this)
            .title(getString(R.string.middleA_showcase_text))
            .targetView(middlea_freq_text)
            .backgroundColorResourceId(R.color.colorPrimaryDark)

        BubbleShowCaseSequence()
            .addShowCase(vuShowcase)
            .addShowCase(chromaticShowcase)
            .addShowCase(instrumentShowcase)
            .addShowCase(middleAShowcase)
            .show()
    }
}