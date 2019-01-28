package co.ajsf.tuner.view.tunerview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import androidx.lifecycle.LiveData
import co.ajsf.tuner.R
import kotlinx.android.synthetic.main.tuner_string.view.*

class TunerStringView
@JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 1f)
        LayoutInflater.from(context).inflate(R.layout.tuner_string, this)
    }

    var numberedName: String = ""
        set(numberedName) {
            string_name_text.text = numberedName.first().toString()
            field = numberedName
        }

    fun setSelectedNameLiveData(selectedNameLiveData: LiveData<String>) {
        var selected = false
        selectedNameLiveData.observeForever {
            if (it == numberedName) {
                selected = true
                setSelected()
            } else if (selected) {
                selected = false
                unselect()
            }
        }
    }

    private fun setSelected() {
        string_name_outline.setImageResource(R.drawable.ic_string_name_box_selected)
        val stringShakeAnimation = AnimationUtils.loadAnimation(context, R.anim.string_shake)
        string_image.startAnimation(stringShakeAnimation)
    }

    private fun unselect() {
        string_image.clearAnimation()
        val shakeEndAnimation = AnimationUtils.loadAnimation(context, R.anim.string_shake_end)
        string_image.startAnimation(shakeEndAnimation)
        string_name_outline.setImageResource(R.drawable.ic_string_name_box)
    }
}