package co.ajsf.tuner

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import co.ajsf.tuner.viewmodel.SelectedInstrumentInfo
import co.ajsf.tuner.viewmodel.SelectedStringInfo
import co.ajsf.tuner.viewmodel.TunerViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val recordAudioRequestCode = 202
    private val recordAudioPermission = RecordAudioPermissionHandler(this, recordAudioRequestCode)

    private lateinit var viewModel: TunerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (recordAudioPermission.isPermissionGranted().not()) {
            recordAudioPermission.requestPermission()
        }
        initViewModel()
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(TunerViewModel::class.java)
        val selectedStringObserver = Observer<SelectedStringInfo> {
            tunerView.selectString(it.first)
        }
        viewModel.selectedStringInfo.observe(this, selectedStringObserver)
        val selectedInstrumentObserver = Observer<SelectedInstrumentInfo> {
            tunerView.selectInstrument(it.first, it.second)
        }
        viewModel.selectedInstrumentInfo.observe(this, selectedInstrumentObserver)
    }

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
        requestCode != this.recordAudioRequestCode -> Unit
        recordAudioPermission.isPermissionGranted() -> viewModel.listenForFrequencies()
        recordAudioPermission.userCheckedNeverAskAgain() ->
            recordAudioPermission.showSettingsReasonAndRequest()
        else -> recordAudioPermission.requestPermission()
    }
}


