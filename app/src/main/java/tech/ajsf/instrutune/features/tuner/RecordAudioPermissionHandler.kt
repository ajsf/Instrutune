package tech.ajsf.instrutune.features.tuner

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import tech.ajsf.instrutune.R
import tech.ajsf.instrutune.common.view.DialogHelper

class RecordAudioPermissionHandler(private val activity: AppCompatActivity) : DialogHelper() {

    val requestCode = 202

    fun requestPermission() {
        val requestRecordAudioPermissionTitle = getString(R.string.record_audio_permission_title)
        val requestRecordAudioPermissionMessage =
            getString(R.string.record_audio_permission_message)

        with(AlertDialog.Builder(activity)) {
            setTitle(requestRecordAudioPermissionTitle)
            setMessage(requestRecordAudioPermissionMessage)
            setPositiveButton("OK") { _, _ ->
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.RECORD_AUDIO),
                    requestCode
                )
            }
            setNegativeButton("Cancel") { _, _ -> activity.finishAndRemoveTask() }
            setCancelable(false)
            finishBuilding()
        }.show()
    }

    fun showSettingsReasonAndRequest() {
        val showSettingsTitle = getString(R.string.show_settings_title)
        val showSettingsMessage = getString(R.string.show_settings_message)

        with(AlertDialog.Builder(activity)) {
            setTitle(showSettingsTitle)
            setMessage(showSettingsMessage)
            setPositiveButton("OK") { _, _ ->
                val intent = Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts("package", activity.packageName, null)
                ).apply { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) }
                activity.startActivity(intent)
            }
            setNegativeButton("Cancel") { _, _ -> activity.finish() }
            setCancelable(false)
            finishBuilding()
        }.show()
    }

    fun isPermissionGranted(): Boolean = ContextCompat
        .checkSelfPermission(
            activity,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED

    fun userCheckedNeverAskAgain(): Boolean = !ActivityCompat
        .shouldShowRequestPermissionRationale(activity, Manifest.permission.RECORD_AUDIO)

    private fun getString(id: Int) = activity.resources.getString(id)
}