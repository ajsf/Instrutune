package co.ajsf.tuner.test.data

internal object TestDataFactory {

    fun randomFloat(): Float = Math.random().toFloat()
    fun randomBoolean(): Boolean = Math.random() < 0.5
}

