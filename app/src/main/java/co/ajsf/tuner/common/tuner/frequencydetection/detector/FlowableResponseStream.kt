package co.ajsf.tuner.common.tuner.frequencydetection.detector

import io.reactivex.Flowable

interface FlowableResponseStream<T> {
    fun getResponseStream(): Flowable<T>
}