package tech.ajsf.instrutune.features.tuner.view.tunerview

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tech.ajsf.instrutune.features.tuner.view.scaleAndCurveDelta

internal class TunerMeterFunctionsTest {

    @Test
    fun `scaleAndCurveDelta returns 0 for 0`() {
        val delta = scaleAndCurveDelta(0)
        assertEquals(0, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 1 for 1`() {
        val delta = scaleAndCurveDelta(1)
        assertEquals(1, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 1 for 2`() {
        val delta = scaleAndCurveDelta(2)
        assertEquals(1, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 1 for 3`() {
        val delta = scaleAndCurveDelta(3)
        assertEquals(1, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 2 for 4`() {
        val delta = scaleAndCurveDelta(4)
        assertEquals(2, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 2 for 5`() {
        val delta = scaleAndCurveDelta(5)
        assertEquals(2, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 3 for 6`() {
        val delta = scaleAndCurveDelta(6)
        assertEquals(3, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 3 for 9`() {
        val delta = scaleAndCurveDelta(9)
        assertEquals(3, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 4 for 10`() {
        val delta = scaleAndCurveDelta(10)
        assertEquals(4, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 4 for 12`() {
        val delta = scaleAndCurveDelta(12)
        assertEquals(4, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 5 for 13`() {
        val delta = scaleAndCurveDelta(13)
        assertEquals(5, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 5 for 17`() {
        val delta = scaleAndCurveDelta(17)
        assertEquals(5, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 6 for 18`() {
        val delta = scaleAndCurveDelta(18)
        assertEquals(6, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 6 for 21`() {
        val delta = scaleAndCurveDelta(21)
        assertEquals(6, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 7 for 22`() {
        val delta = scaleAndCurveDelta(22)
        assertEquals(7, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 7 for 27`() {
        val delta = scaleAndCurveDelta(27)
        assertEquals(7, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 8 for 28`() {
        val delta = scaleAndCurveDelta(28)
        assertEquals(8, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 8 for 32`() {
        val delta = scaleAndCurveDelta(32)
        assertEquals(8, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 9 for 34`() {
        val delta = scaleAndCurveDelta(33)
        assertEquals(9, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 9 for 38`() {
        val delta = scaleAndCurveDelta(38)
        assertEquals(9, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 10 for 39`() {
        val delta = scaleAndCurveDelta(39)
        assertEquals(10, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 10 for 44`() {
        val delta = scaleAndCurveDelta(44)
        assertEquals(10, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 11 for 45`() {
        val delta = scaleAndCurveDelta(45)
        assertEquals(11, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 11 for 51`() {
        val delta = scaleAndCurveDelta(51)
        assertEquals(11, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 12 for 52`() {
        val delta = scaleAndCurveDelta(52)
        assertEquals(12, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 12 for 58`() {
        val delta = scaleAndCurveDelta(58)
        assertEquals(12, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 13 for 55`() {
        val delta = scaleAndCurveDelta(59)
        assertEquals(13, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 13 for 65`() {
        val delta = scaleAndCurveDelta(65)
        assertEquals(13, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 14 for 66`() {
        val delta = scaleAndCurveDelta(66)
        assertEquals(14, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 14 for 73`() {
        val delta = scaleAndCurveDelta(73)
        assertEquals(14, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 15 for 74`() {
        val delta = scaleAndCurveDelta(74)
        assertEquals(15, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 15 for 81`() {
        val delta = scaleAndCurveDelta(81)
        assertEquals(15, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 16 for 82`() {
        val delta = scaleAndCurveDelta(82)
        assertEquals(16, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 16 for 100`() {
        val delta = scaleAndCurveDelta(100)
        assertEquals(16, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 16 for Int MAX_VALUE`() {
        val delta = scaleAndCurveDelta(Int.MAX_VALUE)
        assertEquals(16, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 1 for -1`() {
        val delta = scaleAndCurveDelta(-1)
        assertEquals(1, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 8 for -32`() {
        val delta = scaleAndCurveDelta(-32)
        assertEquals(8, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 16 for -100`() {
        val delta = scaleAndCurveDelta(-1)
        assertEquals(1, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 16 for Int MIN_VALUE`() {
        val delta = scaleAndCurveDelta(Int.MIN_VALUE)
        assertEquals(16, delta)
    }

}