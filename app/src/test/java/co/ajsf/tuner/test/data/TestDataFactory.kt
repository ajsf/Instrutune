package co.ajsf.tuner.test.data

import java.util.concurrent.ThreadLocalRandom

internal object TestDataFactory {

    fun randomFloat(): Float = Math.random().toFloat()
    fun randomInt(maxNumber: Int): Int = ThreadLocalRandom.current().nextInt(1, maxNumber + 1)
    fun randomBoolean(): Boolean = Math.random() < 0.5
}

