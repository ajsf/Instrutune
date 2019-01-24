package co.ajsf.tuner.tuner.frequencydetection.detector

import io.reactivex.Flowable

interface FlowableResponseStream<T> {
    fun getResponseStream(): Flowable<T>
}