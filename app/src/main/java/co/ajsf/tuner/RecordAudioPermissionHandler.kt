package co.ajsf.tuner

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.yesButton

class RecordAudioPermissionHandler(private val activity: AppCompatActivity, private val requestCode: Int) {

    fun requestPermission(): Unit = with(activity) {
        val requestRecordAudioPermissionTitle = resources.getString(R.string.record_audio_permission_title)
        val requestRecordAudioPermissionMessage = resources.getString(R.string.record_audio_permission_message)
        alert {
            title = requestRecordAudioPermissionTitle
            message = requestRecordAudioPermissionMessage
            yesButton {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.RECORD_AUDIO),
                    requestCode
                )
            }
            noButton { finishAndRemoveTask() }
        }.show()
    }

    fun showSettingsReasonAndRequest(): Unit = with(activity) {
        val showSettingsTitle = resources.getString(R.string.show_settings_title)
        val showSettingsMessage = resources.getString(R.string.show_settings_message)
        alert {
            title = showSettingsTitle
            message = showSettingsMessage
            yesButton {
                startActivity(
                    Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts("package", packageName, null)
                    ).apply { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) })
            }
            noButton { finish() }
        }.show()
    }

    fun isPermissionGranted(): Boolean = ContextCompat
        .checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED

    fun userCheckedNeverAskAgain(): Boolean = !ActivityCompat
        .shouldShowRequestPermissionRationale(activity, Manifest.permission.RECORD_AUDIO)
}