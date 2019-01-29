package co.ajsf.tuner.view.tunerview

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.widget.LinearLayout
import androidx.lifecycle.LiveData
import co.ajsf.tuner.R

class StringsView
@JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        orientation = LinearLayout.HORIZONTAL
        gravity = Gravity.CENTER
        setPadding(16, 8, 16, 0)
        setBackgroundResource(R.color.backgroundColor)
    }

    fun setStrings(numberedNames: List<String>, numberedNameLiveData: LiveData<String>) {
        removeAllViews()
        numberedNames.map {
            TunerStringView(context).apply {
                numberedName = it
                setSelectedNameLiveData(numberedNameLiveData)
            }
        }.onEach { addView(it) }
    }
}