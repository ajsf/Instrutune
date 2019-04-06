package tech.ajsf.instrutune.features.tuner

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.NumberPicker
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_tuner.*
import kotlinx.android.synthetic.main.tuner_view.*
import org.kodein.di.Kodein
import tech.ajsf.instrutune.R
import tech.ajsf.instrutune.common.model.InstrumentCategory
import tech.ajsf.instrutune.common.view.InjectedActivity
import tech.ajsf.instrutune.common.viewmodel.buildViewModel
import tech.ajsf.instrutune.features.customtuning.CUSTOM_TUNING_EXTRA
import tech.ajsf.instrutune.features.customtuning.CustomTuningActivity
import tech.ajsf.instrutune.features.tuner.di.tunerActivityModule
import tech.ajsf.instrutune.features.tuner.view.TunerOnboarding

class TunerActivity : InjectedActivity() {

    override fun activityModule() = Kodein.Module("tuner") {
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
            setupUi()
        }
    }

    private fun setupUi(requestOnboarding: Boolean = false) {
        initViewModel()
        initButtons()
        if (requestOnboarding) TunerOnboarding(this).requestOnboarding()
    }

    private fun initViewModel(): Unit = with(viewModel) {
        selectedInstrumentInfo.observe(this@TunerActivity, Observer { (name, numberedNames, middleA) ->
            instrument_name_text.text = name
            middlea_freq_text.text = middleA
            tuner_view.selectInstrument(numberedNames, selectedStringInfo, this@TunerActivity)
        })

        tuner_view.setFreqLiveData(mostRecentFrequency, this@TunerActivity)
        tuner_view.setNoteNameLiveData(mostRecentNoteName, this@TunerActivity)
        tuner_view.setChromaticDeltaLiveData(mostRecentNoteDelta, this@TunerActivity)
    }

    private fun initButtons() {
        instrument_name_text.setOnClickListener { showSelectInstrumentDialog() }
        middlea_freq_text.setOnClickListener { showSetMiddleADialog() }
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
        R.id.set_center_a -> {
            showSetMiddleADialog()
            true
        }
        R.id.create_tuning -> {
            launchCustomTuning()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun launchCustomTuning() {
        val intent = Intent(this, CustomTuningActivity::class.java)
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                val name = data?.getStringExtra(CUSTOM_TUNING_EXTRA)
                name?.let {
                    viewModel.saveSelectedCategory(InstrumentCategory.Custom.toString())
                    viewModel.saveSelectedTuning(it)
                }
            }
        }
    }

    private fun showSelectInstrumentDialog() {
        val title = getString(R.string.select_instrument_title)
        val instrumentNames = viewModel.getInstruments().toTypedArray()

        showSelectorDialog(title, instrumentNames) {
            viewModel.saveSelectedCategory(instrumentNames[it])
            showSelectTuningDialog()
        }
    }

    private fun showSelectTuningDialog() {
        val title = getString(R.string.select_tuning_title)
        val tunings = viewModel.getTunings().map { it.tuningName }.toTypedArray()

        showSelectorDialog(title, tunings) { viewModel.saveSelectedTuning(tunings[it]) }
    }

    private fun showSelectorDialog(title: String, items: Array<String>, onClick: (Int) -> Unit) {
        with(AlertDialog.Builder(this)) {
            setTitle(title)
            setItems(items) { _, i -> onClick(i) }
            create()
        }.show()
    }

    private fun showSetMiddleADialog() {
        var newOffset = viewModel.getOffset()
        val maxVariance = 10

        val inflater = LayoutInflater.from(applicationContext)

        val picker = inflater.inflate(R.layout.freq_picker, null) as NumberPicker
        picker.apply {
            maxValue = maxVariance * 2
            minValue = 0
            wrapSelectorWheel = false
            displayedValues = (440 - maxVariance..440 + maxVariance)
                .map { it.toString() }.toTypedArray()

            setOnValueChangedListener { numPicker, _, _ ->
                newOffset = numPicker.value - maxVariance
            }

            value = newOffset + maxVariance
        }

        with(AlertDialog.Builder(this)) {
            setView(picker)
            setTitle(getString(R.string.select_center_freq))
            setPositiveButton(getString(R.string.btn_select_text)) { _, _ -> viewModel.saveOffset(newOffset) }
            setNegativeButton(getString(R.string.btn_cancel_text)) { _, _ -> }
            setNeutralButton(getString(R.string.btn_reset_text)) { _, _ -> viewModel.saveOffset(0) }
            setCancelable(true)
            create()
        }.show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ): Unit = when {
        requestCode != recordAudioPermission.requestCode -> Unit
        recordAudioPermission.isPermissionGranted() -> setupUi(true)
        recordAudioPermission.userCheckedNeverAskAgain() ->
            recordAudioPermission.showSettingsReasonAndRequest()
        else -> recordAudioPermission.requestPermission()
    }
}