package tech.ajsf.instrutune.features.tuner.view.tunerview

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tech.ajsf.instrutune.features.tuner.view.scaleAndCurveDelta

internal class TunerMeterFunctionsTest {

    @Test
    fun `scaleAndCurveDelta returns 0 for 0`() {
        val delta = scaleAndCurveDelta(0f)
        assertEquals(0, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 1 for 1`() {
        val delta = scaleAndCurveDelta(1f)
        assertEquals(1, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 1 for 2`() {
        val delta = scaleAndCurveDelta(2f)
        assertEquals(1, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 1 for 3`() {
        val delta = scaleAndCurveDelta(3f)
        assertEquals(1, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 2 for 4`() {
        val delta = scaleAndCurveDelta(4f)
        assertEquals(2, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 2 for 5`() {
        val delta = scaleAndCurveDelta(5f)
        assertEquals(2, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 3 for 6`() {
        val delta = scaleAndCurveDelta(6f)
        assertEquals(3, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 3 for 9`() {
        val delta = scaleAndCurveDelta(9f)
        assertEquals(3, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 4 for 10`() {
        val delta = scaleAndCurveDelta(10f)
        assertEquals(4, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 4 for 12`() {
        val delta = scaleAndCurveDelta(12f)
        assertEquals(4, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 5 for 13`() {
        val delta = scaleAndCurveDelta(13f)
        assertEquals(5, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 5 for 17`() {
        val delta = scaleAndCurveDelta(17f)
        assertEquals(5, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 6 for 18`() {
        val delta = scaleAndCurveDelta(18f)
        assertEquals(6, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 6 for 21`() {
        val delta = scaleAndCurveDelta(21f)
        assertEquals(6, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 7 for 22`() {
        val delta = scaleAndCurveDelta(22f)
        assertEquals(7, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 7 for 27`() {
        val delta = scaleAndCurveDelta(27f)
        assertEquals(7, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 8 for 28`() {
        val delta = scaleAndCurveDelta(28f)
        assertEquals(8, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 8 for 32`() {
        val delta = scaleAndCurveDelta(32f)
        assertEquals(8, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 9 for 34`() {
        val delta = scaleAndCurveDelta(33f)
        assertEquals(9, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 9 for 38`() {
        val delta = scaleAndCurveDelta(38f)
        assertEquals(9, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 10 for 39`() {
        val delta = scaleAndCurveDelta(39f)
        assertEquals(10, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 10 for 44`() {
        val delta = scaleAndCurveDelta(44f)
        assertEquals(10, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 11 for 45`() {
        val delta = scaleAndCurveDelta(45f)
        assertEquals(11, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 11 for 51`() {
        val delta = scaleAndCurveDelta(51f)
        assertEquals(11, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 12 for 52`() {
        val delta = scaleAndCurveDelta(52f)
        assertEquals(12, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 12 for 58`() {
        val delta = scaleAndCurveDelta(58f)
        assertEquals(12, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 13 for 55`() {
        val delta = scaleAndCurveDelta(59f)
        assertEquals(13, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 13 for 65`() {
        val delta = scaleAndCurveDelta(65f)
        assertEquals(13, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 14 for 66`() {
        val delta = scaleAndCurveDelta(66f)
        assertEquals(14, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 14 for 73`() {
        val delta = scaleAndCurveDelta(73f)
        assertEquals(14, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 15 for 74`() {
        val delta = scaleAndCurveDelta(74f)
        assertEquals(15, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 15 for 81`() {
        val delta = scaleAndCurveDelta(81f)
        assertEquals(15, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 16 for 82`() {
        val delta = scaleAndCurveDelta(82f)
        assertEquals(16, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 16 for 100`() {
        val delta = scaleAndCurveDelta(100f)
        assertEquals(16, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 16 for Float MAX_VALUE`() {
        val delta = scaleAndCurveDelta(Float.MAX_VALUE)
        assertEquals(16, delta)
    }
    
    @Test
    fun `scaleAndCurveDelta returns 1 for -1`() {
        val delta = scaleAndCurveDelta(-1f)
        assertEquals(1, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 8 for -32`() {
        val delta = scaleAndCurveDelta(-32f)
        assertEquals(8, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 16 for -100`() {
        val delta = scaleAndCurveDelta(-1f)
        assertEquals(1, delta)
    }
}