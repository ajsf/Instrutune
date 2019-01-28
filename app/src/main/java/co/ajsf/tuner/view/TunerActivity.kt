package co.ajsf.tuner.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
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

        selectedInstrumentInfo
            .onUpdate { (name, numberedNames) ->
                title = "${resources.getString(R.string.app_name)} - $name"
                tuner_view.selectInstrument(numberedNames, selectedStringInfo)
            }

        mostRecentFrequency.onUpdate { tuner_view.setFreq(it) }
        mostRecentNoteName.onUpdate { tuner_view.setNoteName(it) }

        tuner_view.setChromaticDeltaLiveData(mostRecentNoteDelta)
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
        else -> super.onOptionsItemSelected(item)
    }

    private fun showSelectInstrumentDialog() {
        val instrumentNames = viewModel.getInstruments().map { it.name }
        val title = getString(R.string.select_instrument_title)
        selector(title, instrumentNames) { _, i ->
            viewModel.saveSelectedInstrument(instrumentNames[i])
        }
    }

    private fun <T> LiveData<T>.onUpdate(action: (T) -> Unit) =
        observe(this@TunerActivity, Observer { action.invoke(it) })

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


