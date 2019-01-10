package co.ajsf.tuner

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import co.ajsf.tuner.frequencyDetection.FrequencyDetector
import co.ajsf.tuner.model.InstrumentFactory
import co.ajsf.tuner.model.findClosestString
import co.ajsf.tuner.tarsos.DetectionEngineImpl
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val TAG = this.javaClass.canonicalName
    private val frequencyDetector = FrequencyDetector { DetectionEngineImpl.builder() }
    private val requestCode = 202
    private val recordAudioPermission = RecordAudioPermissionHandler(this, requestCode)
    private val instrument = InstrumentFactory.GUITAR

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (recordAudioPermission.isPermissionGranted().not()) {
            recordAudioPermission.requestPermission()
        }
        selectInstrument()
    }

    private fun selectInstrument() {
        tunerView.selectInstrument(instrument)
    }

    override fun onResume() {
        super.onResume()
        if (recordAudioPermission.isPermissionGranted()) {
            listenForFrequencies()
        }
    }

    override fun onPause() {
        super.onPause()
        frequencyDetector.stopListening()
    }

    private fun listenForFrequencies() {
        frequencyDetector.listen {
            Log.d(TAG, it.toString())
            val detectedString = instrument.findClosestString(it)
            tunerView.selectString(detectedString.number)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ): Unit = when {
        requestCode != this.requestCode -> Unit
        recordAudioPermission.isPermissionGranted() -> listenForFrequencies()
        recordAudioPermission.userCheckedNeverAskAgain() ->
            recordAudioPermission.showSettingsReasonAndRequest()
        else -> recordAudioPermission.requestPermission()
    }
}


