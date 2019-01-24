package co.ajsf.tuner.tuner.frequencydetection

import io.reactivex.Flowable

interface FlowableResponseStream<T> {
    fun getResponseStream(): Flowable<T>
}