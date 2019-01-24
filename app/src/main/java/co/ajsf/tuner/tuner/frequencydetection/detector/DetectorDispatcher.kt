package co.ajsf.tuner.tuner.frequencydetection.detector

typealias DispatcherListener<T> = (T) -> Unit

interface DetectorDispatcher<T> {

    fun listen(listener: DispatcherListener<T>)

    fun stop()
}