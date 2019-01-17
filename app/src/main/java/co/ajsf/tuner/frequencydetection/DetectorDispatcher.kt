package co.ajsf.tuner.frequencydetection

typealias DispatcherListener<T> = (T) -> Unit

interface DetectorDispatcher<T> {

    fun listen(listener: DispatcherListener<T>)

    fun stop()
}