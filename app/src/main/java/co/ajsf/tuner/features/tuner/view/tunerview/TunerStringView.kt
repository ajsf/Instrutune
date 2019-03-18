package co.ajsf.tuner.features.tuner.view.tunerview

import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import co.ajsf.tuner.R
import kotlinx.android.synthetic.main.tuner_string.view.*

class TunerStringView
@JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        LayoutInflater.from(context).inflate(R.layout.tuner_string, this)
        layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f)
        string_name_outline_selected.imageAlpha = 0
    }

    private var stringIsSelected = false

    var numberedName: String = ""
        set(numberedName) {
            string_name_text.text = numberedName
            field = numberedName
        }

    fun updateSelectedString(selectedString: String) {
        if (selectedString == numberedName) {
            stringIsSelected = true
            setSelected()
        } else if (stringIsSelected) {
            stringIsSelected = false
            unselect()
        }
    }

    private fun setSelected() {
        val stringShakeAnimation = AnimationUtils.loadAnimation(context, R.anim.string_shake)
        string_image.startAnimation(stringShakeAnimation)
        ObjectAnimator.ofInt(string_name_outline_selected, "imageAlpha", 255).apply {
            duration = 200
            start()
        }
    }

    private fun unselect() {
        string_image.clearAnimation()
        val shakeEndAnimation = AnimationUtils.loadAnimation(context, R.anim.string_shake_end)
        string_image.startAnimation(shakeEndAnimation)
        ObjectAnimator.ofInt(string_name_outline_selected, "imageAlpha", 0).apply {
            duration = 600
            start()
        }
    }
}