package tech.ajsf.instrutune.common.viewmodel

import androidx.lifecycle.ViewModel
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import tech.ajsf.instrutune.common.data.InstrumentRepository
import tech.ajsf.instrutune.common.view.InstrumentDialogHelper

abstract class BaseViewModel : ViewModel() {

    protected abstract val instrumentRepository: InstrumentRepository
    protected abstract val dialogHelper: InstrumentDialogHelper
    protected abstract val uiScheduler: Scheduler

    protected val disposable = CompositeDisposable()

    fun onActivityDestroyed() {
        dialogHelper.clear()
        disposable.clear()
    }

    fun showSelectCategory(): Unit = disposable.add(instrumentRepository
        .getCategories()
        .observeOn(uiScheduler)
        .subscribeBy { instruments ->
            dialogHelper
                .showSelectInstrumentDialog(instruments) { showSelectTuning(it) }
        }
    ).run { }

    fun showSelectTuning(category: String) {
        disposable.add(getTunings(category)
            .subscribeBy { tunings ->
                if (tunings.isNotEmpty()) {
                    dialogHelper.showSelectTuningDialog(tunings.map { it.tuningName }) {
                        val tuningId = tunings[it].id
                        if (tuningId != null) onTuningsSelected(tuningId)
                    }
                }
            })
    }

    abstract fun onTuningsSelected(id: Int)

    protected fun getTunings(category: String) =
        instrumentRepository.getTuningsForCategory(category)
            .observeOn(uiScheduler)
}