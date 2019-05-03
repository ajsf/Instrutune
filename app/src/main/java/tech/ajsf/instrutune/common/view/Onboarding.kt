package tech.ajsf.instrutune.common.view

import androidx.appcompat.app.AppCompatActivity
import com.elconfidencial.bubbleshowcase.BubbleShowCaseBuilder
import tech.ajsf.instrutune.R

abstract class Onboarding(private val activity: AppCompatActivity) : DialogHelper() {

    abstract fun requestOnboarding()

    protected fun builder() = BubbleShowCaseBuilder(activity)
        .backgroundColorResourceId(R.color.secondaryColor)

    protected fun nextArrowBuilder() = builder()
        .closeActionImageResourceId(R.drawable.ic_arrow_forward_white_24dp)

    protected fun noCloseBuilder() = builder()
        .disableCloseAction(true)
}