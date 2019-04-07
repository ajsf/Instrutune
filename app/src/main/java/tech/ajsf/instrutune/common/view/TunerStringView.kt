package tech.ajsf.instrutune.common.view

import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import androidx.core.animation.doOnEnd
import tech.ajsf.instrutune.R
import kotlinx.android.synthetic.main.tuner_string.view.*

class TunerStringView
@JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var currentAnimation: ObjectAnimator? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.tuner_string, this)
        string_name_outline_selected.imageAlpha = 0
    }

    private var stringIsSelected = false

    var numberedName: String = ""
        set(numberedName) {
            string_name_text.text = numberedName
            field = numberedName
        }

    var noteNumber = 0

    fun updateSelectedString(selectedString: String, shakeString: Boolean = true) {
        if (selectedString == numberedName) {
            if (!stringIsSelected) selectString()
            if (shakeString) shakeString()
        } else if (stringIsSelected) {
            clearAnimations()
            unselect()
        }
    }

    private fun selectString() {
        stringIsSelected = true
        currentAnimation = imageAlphaAnimation(0, 255).apply {
            duration = 200
            start()
        }
    }

    private fun shakeString() {
        val stringShakeAnimation = AnimationUtils.loadAnimation(context, R.anim.string_shake)
        string_image.startAnimation(stringShakeAnimation)
    }

    private fun unselect() {
        stringIsSelected = false
        val shakeEndAnimation = AnimationUtils.loadAnimation(context, R.anim.string_shake_end)
        string_image.startAnimation(shakeEndAnimation)

        currentAnimation = imageAlphaAnimation(255, 0).apply {
            duration = 1000
            start()
        }
    }

    private fun imageAlphaAnimation(fromValue: Int, toValue: Int): ObjectAnimator = ObjectAnimator
        .ofInt(string_name_outline_selected, "imageAlpha", fromValue, toValue)
        .apply {
            doOnEnd {
                currentAnimation = null
                string_name_outline_selected.imageAlpha = toValue
            }
        }

    private fun clearAnimations() {
        string_image.clearAnimation()
        currentAnimation?.cancel()
        currentAnimation = null
    }
}