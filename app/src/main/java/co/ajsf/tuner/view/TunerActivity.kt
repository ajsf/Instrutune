package co.ajsf.tuner.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import co.ajsf.tuner.R
import co.ajsf.tuner.RecordAudioPermissionHandler
import co.ajsf.tuner.di.frequencyDetectionModule
import co.ajsf.tuner.di.tunerActivityModule
import co.ajsf.tuner.viewmodel.TunerViewModel
import kotlinx.android.synthetic.main.activity_tuner.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.retainedKodein

class TunerActivity : AppCompatActivity(), KodeinAware {

    override val kodein: Kodein by retainedKodein {
        import(tunerActivityModule())
        import(frequencyDetectionModule())
    }

    private val viewModel: TunerViewModel by buildViewModel()

    private val recordAudioPermission = RecordAudioPermissionHandler(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tuner)
        if (recordAudioPermission.isPermissionGranted().not()) {
            recordAudioPermission.requestPermission()
        }
        initViewModel()
    }

    private fun initViewModel() = viewModel.apply {
        selectedStringInfo.onUpdate {
            tuner_view.selectString(it.first)
            tuner_view.setDelta(it.second)
        }
        selectedInstrumentInfo.onUpdate { tuner_view.selectInstrument(it.first, it.second) }
    }

    private fun <T> LiveData<T>.onUpdate(action: (T) -> Unit) =
        observe(this@TunerActivity, Observer { action.invoke(it) })

    override fun onResume() {
        super.onResume()
        if (recordAudioPermission.isPermissionGranted()) {
            viewModel.listenForFrequencies()
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopListening()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ): Unit = when {
        requestCode != recordAudioPermission.requestCode -> Unit
        recordAudioPermission.isPermissionGranted() -> viewModel.listenForFrequencies()
        recordAudioPermission.userCheckedNeverAskAgain() ->
            recordAudioPermission.showSettingsReasonAndRequest()
        else -> recordAudioPermission.requestPermission()
    }
}


