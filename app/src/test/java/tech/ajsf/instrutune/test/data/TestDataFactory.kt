package tech.ajsf.instrutune.test.data

import java.util.*
import java.util.concurrent.ThreadLocalRandom

object TestDataFactory {

    fun randomFloat(): Float = Math.random().toFloat()
    fun randomInt(maxNumber: Int = 50, minNumber: Int = 1): Int =
        ThreadLocalRandom.current().nextInt(minNumber, maxNumber + 1)

    fun randomBoolean(): Boolean = Math.random() < 0.5
    fun randomString(): String = UUID.randomUUID().toString().take(5)

}

