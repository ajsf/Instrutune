package tech.ajsf.instrutune.features.tuner

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_tuner.*
import kotlinx.android.synthetic.main.tuner_view.*
import org.kodein.di.Kodein
import tech.ajsf.instrutune.R
import tech.ajsf.instrutune.common.tuner.InstrumentMode
import tech.ajsf.instrutune.common.tuner.TunerMode
import tech.ajsf.instrutune.common.view.InjectedActivity
import tech.ajsf.instrutune.common.viewmodel.buildViewModel
import tech.ajsf.instrutune.features.customtuning.CUSTOM_TUNING_EXTRA
import tech.ajsf.instrutune.features.customtuning.CustomTuningActivity
import tech.ajsf.instrutune.features.tuner.di.tunerActivityModule
import tech.ajsf.instrutune.features.tuner.view.TunerOnboarding

private const val CUSTOM_TUNING_REQUEST_CODE = 101

class TunerActivity : InjectedActivity() {

    override fun activityModule() = Kodein.Module("tuner") {
        import(tunerActivityModule())
    }

    private val viewModel: TunerViewModel by buildViewModel()

    private val recordAudioPermission = RecordAudioPermissionHandler(this)

    private var onboarding: TunerOnboarding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tuner)
        if (recordAudioPermission.isPermissionGranted().not()) {
            recordAudioPermission.requestPermission()
        } else {
            setupUi()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        onboarding?.clear()
        viewModel.onActivityDestroyed()
    }

    private fun setupUi(requestOnboarding: Boolean = false) {
        initViewModel()
        initButtons()
        if (requestOnboarding) {
            onboarding = TunerOnboarding(this)
            onboarding?.requestOnboarding()
        }
    }

    private fun initViewModel(): Unit = with(viewModel) {
        tuner_view.setNoteViewState(noteViewState, this@TunerActivity)

        tunerViewState.observe(this@TunerActivity, Observer { viewState ->
            viewState?.let {
                instrument_name_text.text = it.tuningName
                middlea_freq_text.text = it.middleA
                tuner_view.selectInstrument(it.noteNames)
                setMode(it.mode)
            }
        })
    }

    private fun setMode(mode: TunerMode) {
        tuner_view.clearView()
        when (mode) {
            InstrumentMode -> setInstrumentMode()
            else -> setChromaticMode()
        }
    }

    private fun setInstrumentMode() {
        motionLayout?.transitionToStart()
        mode_button.text = getString(R.string.instrument_mode)
    }

    private fun setChromaticMode() {
        motionLayout?.transitionToEnd()
        instrument_name_text.text = getString(R.string.chromatic)
        mode_button.text = getString(R.string.chromatic_mode)
    }

    private fun initButtons() {
        instrument_name_text.setOnClickListener { viewModel.showSelectCategory() }
        middlea_freq_text.setOnClickListener { viewModel.showSelectMiddleA() }
        mode_button.setOnClickListener { viewModel.toggleMode() }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.select_instrument -> viewModel.showSelectCategory()
            R.id.select_tuning -> viewModel.showSelectTuning()
            R.id.set_center_a -> viewModel.showSelectMiddleA()
            R.id.create_tuning -> launchCustomTuning()
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    private fun launchCustomTuning() {
        val intent = Intent(this, CustomTuningActivity::class.java)
        startActivityForResult(intent, CUSTOM_TUNING_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CUSTOM_TUNING_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val id = data?.getIntExtra(CUSTOM_TUNING_EXTRA, -1)
            viewModel.saveSelectedTuning(id ?: -1)
        }
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