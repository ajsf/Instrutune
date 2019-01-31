package co.ajsf.tuner.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import co.ajsf.tuner.R
import co.ajsf.tuner.RecordAudioPermissionHandler
import co.ajsf.tuner.di.tunerActivityModule
import co.ajsf.tuner.viewmodel.TunerViewModel
import kotlinx.android.synthetic.main.activity_tuner.*
import org.jetbrains.anko.selector
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein

class TunerActivity : AppCompatActivity(), KodeinAware {

    private val _parentKodein: Kodein by closestKodein()

    override val kodein = Kodein.lazy {
        extend(_parentKodein)
        import(tunerActivityModule())
    }

    private val viewModel: TunerViewModel by buildViewModel()

    private val recordAudioPermission = RecordAudioPermissionHandler(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tuner)
        if (recordAudioPermission.isPermissionGranted().not()) {
            recordAudioPermission.requestPermission()
        } else {
            initViewModel()
        }
    }

    private fun initViewModel(): Unit = with(viewModel) {

        selectedInstrumentInfo.observe(this@TunerActivity, Observer { (name, numberedNames) ->
            title = "${resources.getString(R.string.app_name)} - $name"
            tuner_view.selectInstrument(numberedNames, selectedStringInfo, this@TunerActivity)
        })

        tuner_view.setFreqLiveData(mostRecentFrequency, this@TunerActivity)
        tuner_view.setNoteNameLiveData(mostRecentNoteName, this@TunerActivity)
        tuner_view.setChromaticDeltaLiveData(mostRecentNoteDelta, this@TunerActivity)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = when (item?.itemId) {
        R.id.select_instrument -> {
            showSelectInstrumentDialog()
            true
        }
        R.id.select_tuning -> {
            showSelectTuningDialog()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun showSelectInstrumentDialog() {
        val instrumentNames = viewModel.getInstruments()
        val title = getString(R.string.select_instrument_title)
        selector(title, instrumentNames) { _, i ->
            viewModel.saveSelectedCategory(instrumentNames[i])
            showSelectTuningDialog()
        }
    }

    private fun showSelectTuningDialog() {
        val tunings = viewModel.getTunings().map { it.tuningName }
        val title = getString(R.string.select_tuning_title)
        selector(title, tunings) { _, i ->
            viewModel.saveSelectedTuning(tunings[i])
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ): Unit = when {
        requestCode != recordAudioPermission.requestCode -> Unit
        recordAudioPermission.isPermissionGranted() -> initViewModel()
        recordAudioPermission.userCheckedNeverAskAgain() ->
            recordAudioPermission.showSettingsReasonAndRequest()
        else -> recordAudioPermission.requestPermission()
    }
}