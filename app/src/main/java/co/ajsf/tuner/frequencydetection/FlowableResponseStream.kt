package co.ajsf.tuner.frequencydetection

import io.reactivex.Flowable

interface FlowableResponseStream<T> {
    fun getResponseStream(): Flowable<T>
}