package co.ajsf.tuner

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import co.ajsf.tuner.frequencyDetection.FrequencyDetectorImpl


class MainActivity : AppCompatActivity() {

    private val TAG = this.javaClass.canonicalName
    private val frequencyDetector = FrequencyDetectorImpl()
    private val requestCode = 202
    private val recordAudioPermission = RecordAudioPermissionHandler(this, requestCode)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (recordAudioPermission.isPermissionGranted().not()) {
            recordAudioPermission.requestPermission()
        }
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


