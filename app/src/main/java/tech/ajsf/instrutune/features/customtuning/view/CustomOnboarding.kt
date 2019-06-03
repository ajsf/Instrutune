package tech.ajsf.instrutune.features.customtuning.view

import android.view.MotionEvent
import androidx.appcompat.app.AlertDialog
import androidx.core.view.get
import androidx.core.view.size
import com.elconfidencial.bubbleshowcase.BubbleShowCase
import com.elconfidencial.bubbleshowcase.BubbleShowCaseListener
import com.elconfidencial.bubbleshowcase.BubbleShowCaseSequence
import kotlinx.android.synthetic.main.custom_tuning_builder.*
import kotlinx.android.synthetic.main.custom_tuning_starter.*
import kotlinx.android.synthetic.main.tuner_string.view.*
import tech.ajsf.instrutune.R
import tech.ajsf.instrutune.common.view.Onboarding
import tech.ajsf.instrutune.common.view.TunerStringView
import tech.ajsf.instrutune.features.customtuning.CustomTuningActivity

private sealed class OnboardingState
private object StepOne : OnboardingState()
private object StepTwo : OnboardingState()
private object StepThree : OnboardingState()
private object StepFour : OnboardingState()

class CustomOnboarding(private val activity: CustomTuningActivity) : Onboarding(activity) {

    private var onboardingState: OnboardingState = StepOne

    override fun requestOnboarding() = with(AlertDialog.Builder(activity)) {
        setMessage(activity.getString(R.string.custom_onboarding_request))
        setPositiveButton(activity.getString(R.string.yes)) { _, _ -> showOnboarding() }
        setNegativeButton(activity.getString(R.string.no)) { _, _ -> activity.onboarding = null }
        setCancelable(false)
        finishBuilding()
    }.show()

    private fun showOnboarding() = with(activity) {
        onboarding = (this@CustomOnboarding)
        val starterShowcase = noCloseBuilder()
            .title(getString(R.string.starter_showcase_one))
            .arrowPosition(BubbleShowCase.ArrowPosition.BOTTOM)
            .targetView(button_wrapper)
            .listener(object : BubbleShowCaseListener {
                override fun onBackgroundDimClick(bubbleShowCase: BubbleShowCase) {}
                override fun onBubbleClick(bubbleShowCase: BubbleShowCase) {}
                override fun onCloseActionImageClick(bubbleShowCase: BubbleShowCase) {}
                override fun onTargetClick(bubbleShowCase: BubbleShowCase) {
                    btn_blank.performClick()
                }
            })

        val fabShowcase = noCloseBuilder()
            .title(getString(R.string.fab_showcase_text))
            .targetView(add_string_fab)
            .listener(object : BubbleShowCaseListener {
                override fun onBackgroundDimClick(bubbleShowCase: BubbleShowCase) {}
                override fun onBubbleClick(bubbleShowCase: BubbleShowCase) {}
                override fun onCloseActionImageClick(bubbleShowCase: BubbleShowCase) {}
                override fun onTargetClick(bubbleShowCase: BubbleShowCase) {
                    add_string_fab.performClick()
                }
            })

        BubbleShowCaseSequence()
            .addShowCase(starterShowcase)
            .addShowCase(fabShowcase)
            .show()
    }

    private fun advanceOnboardingOne(): Unit = with(activity) {
        this@CustomOnboarding.onboardingState = StepTwo
        noCloseBuilder()
            .title(getString(R.string.fab_showcase_two_text))
            .targetView(add_string_fab)
            .listener(object : BubbleShowCaseListener {
                override fun onBackgroundDimClick(bubbleShowCase: BubbleShowCase) {}
                override fun onBubbleClick(bubbleShowCase: BubbleShowCase) {}
                override fun onCloseActionImageClick(bubbleShowCase: BubbleShowCase) {}
                override fun onTargetClick(bubbleShowCase: BubbleShowCase) {
                    add_string_fab.performClick()
                }
            }).show()
    }

    private fun noteShowcase(): Unit = with(activity) {
        this@CustomOnboarding.onboardingState = StepThree
        if (strings_view.size > 1) {
            val stringView = strings_view[1] as TunerStringView
            stringView.updateSelectedString(stringView.numberedName, false)
            noCloseBuilder()
                .title(getString(R.string.note_showcase_text))
                .targetView(stringView.string_name_outline)
                .listener(object : BubbleShowCaseListener {
                    override fun onBackgroundDimClick(bubbleShowCase: BubbleShowCase) {}
                    override fun onBubbleClick(bubbleShowCase: BubbleShowCase) {}
                    override fun onCloseActionImageClick(bubbleShowCase: BubbleShowCase) {}
                    override fun onTargetClick(bubbleShowCase: BubbleShowCase) {
                        stringView.string_name_outline.performClick()
                        stringView.updateSelectedString("", false)
                    }
                }).show()
        }
    }

    private fun dragShowcase(): Unit = with(activity) {
        this@CustomOnboarding.onboardingState = StepFour

        if (strings_view.size > 1) {
            val stringView = strings_view[1] as TunerStringView
            noCloseBuilder()
                .title(getString(R.string.drag_showcase_text))
                .targetView(stringView)
                .arrowPosition(BubbleShowCase.ArrowPosition.RIGHT)
                .dismissOnTargetDown(true)
                .listener(object : BubbleShowCaseListener {
                    override fun onBackgroundDimClick(bubbleShowCase: BubbleShowCase) {}
                    override fun onBubbleClick(bubbleShowCase: BubbleShowCase) {}
                    override fun onCloseActionImageClick(bubbleShowCase: BubbleShowCase) {}
                    override fun onTargetClick(bubbleShowCase: BubbleShowCase) {}
                    override fun onTouch(bubbleShowCase: BubbleShowCase, event: MotionEvent) {
                        stringView.dispatchTouchEvent(event)
                    }

                })
                .show()
        }
    }

    private fun nameShowcase(): Unit = with(activity) {
        onboarding = null
        builder()
            .title(getString(R.string.name_showcase_text))
            .targetView(tuning_name_edit_text)
            .show()
    }

    fun advance(): Unit = when (onboardingState) {
        StepOne -> advanceOnboardingOne()
        StepTwo -> noteShowcase()
        StepThree -> dragShowcase()
        StepFour -> nameShowcase()
    }
}