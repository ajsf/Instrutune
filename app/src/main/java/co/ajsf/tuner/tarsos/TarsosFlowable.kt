package co.ajsf.tuner.tarsos

import co.ajsf.tuner.frequencydetection.FlowableResponseStream
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Scheduler
import java.io.IOException

typealias TarsosResponseStream = FlowableResponseStream<TarsosResponse>

class TarsosFlowable(tarsosDispatcher: TarsosDispatcher, scheduler: Scheduler) : TarsosResponseStream {

    private val flowable: Flowable<TarsosResponse>

    init {
        flowable = Flowable.create<TarsosResponse>({ emitter ->
            println("Detect Engine: creating")

            emitter.setCancellable {
                println("Detect Engine: cancelling")
                tarsosDispatcher.stop()
                emitter.onComplete()
            }

            tarsosDispatcher.listen { res ->
                try {
                    if (!emitter.isCancelled) {
                        emitter.onNext(res)
                    } else {
                        emitter.onComplete()
                    }
                } catch (e: IOException) {
                    emitter.onError(e)
                }
            }

        }, BackpressureStrategy.DROP).subscribeOn(scheduler).share()
    }

    override fun getResponseStream(): Flowable<TarsosResponse> {
        return flowable
    }
}