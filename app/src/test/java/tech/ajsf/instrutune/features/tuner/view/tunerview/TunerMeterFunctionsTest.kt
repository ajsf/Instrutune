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
    fun `scaleAndCurveDelta returns 2 for 2`() {
        val delta = scaleAndCurveDelta(2f)
        assertEquals(2, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 2 for 4`() {
        val delta = scaleAndCurveDelta(4f)
        assertEquals(2, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 3 for 5`() {
        val delta = scaleAndCurveDelta(5f)
        assertEquals(3, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 3 for 7`() {
        val delta = scaleAndCurveDelta(7f)
        assertEquals(3, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 4 for 8`() {
        val delta = scaleAndCurveDelta(8f)
        assertEquals(4, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 4 for 11`() {
        val delta = scaleAndCurveDelta(11f)
        assertEquals(4, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 5 for 12`() {
        val delta = scaleAndCurveDelta(12f)
        assertEquals(5, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 5 for 16`() {
        val delta = scaleAndCurveDelta(16f)
        assertEquals(5, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 6 for 17`() {
        val delta = scaleAndCurveDelta(17f)
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
    fun `scaleAndCurveDelta returns 8 for 33`() {
        val delta = scaleAndCurveDelta(33f)
        assertEquals(8, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 9 for 34`() {
        val delta = scaleAndCurveDelta(34f)
        assertEquals(9, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 9 for 40`() {
        val delta = scaleAndCurveDelta(40f)
        assertEquals(9, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 10 for 41`() {
        val delta = scaleAndCurveDelta(41f)
        assertEquals(10, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 10 for 47`() {
        val delta = scaleAndCurveDelta(47f)
        assertEquals(10, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 11 for 48`() {
        val delta = scaleAndCurveDelta(48f)
        assertEquals(11, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 11 for 54`() {
        val delta = scaleAndCurveDelta(54f)
        assertEquals(11, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 12 for 55`() {
        val delta = scaleAndCurveDelta(55f)
        assertEquals(12, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 12 for 62`() {
        val delta = scaleAndCurveDelta(62f)
        assertEquals(12, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 13 for 63`() {
        val delta = scaleAndCurveDelta(63f)
        assertEquals(13, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 13 for 71`() {
        val delta = scaleAndCurveDelta(71f)
        assertEquals(13, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 14 for 72`() {
        val delta = scaleAndCurveDelta(72f)
        assertEquals(14, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 14 for 80`() {
        val delta = scaleAndCurveDelta(80f)
        assertEquals(14, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 15 for 81`() {
        val delta = scaleAndCurveDelta(81f)
        assertEquals(15, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 15 for 89`() {
        val delta = scaleAndCurveDelta(89f)
        assertEquals(15, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 16 for 90`() {
        val delta = scaleAndCurveDelta(90f)
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
    fun `scaleAndCurveDelta returns 2 for -2`() {
        val delta = scaleAndCurveDelta(-2f)
        assertEquals(2, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 2 for -4`() {
        val delta = scaleAndCurveDelta(-4f)
        assertEquals(2, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 3 for -5`() {
        val delta = scaleAndCurveDelta(-5f)
        assertEquals(3, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 3 for -7`() {
        val delta = scaleAndCurveDelta(-7f)
        assertEquals(3, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 4 for -8`() {
        val delta = scaleAndCurveDelta(-8f)
        assertEquals(4, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 4 for -11`() {
        val delta = scaleAndCurveDelta(-11f)
        assertEquals(4, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 5 for -12`() {
        val delta = scaleAndCurveDelta(-12f)
        assertEquals(5, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 5 for -16`() {
        val delta = scaleAndCurveDelta(-16f)
        assertEquals(5, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 6 for -17`() {
        val delta = scaleAndCurveDelta(-17f)
        assertEquals(6, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 6 for -21`() {
        val delta = scaleAndCurveDelta(-21f)
        assertEquals(6, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 7 for -22`() {
        val delta = scaleAndCurveDelta(-22f)
        assertEquals(7, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 7 for -27`() {
        val delta = scaleAndCurveDelta(-27f)
        assertEquals(7, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 8 for -28`() {
        val delta = scaleAndCurveDelta(-28f)
        assertEquals(8, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 8 for -33`() {
        val delta = scaleAndCurveDelta(-33f)
        assertEquals(8, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 9 for -34`() {
        val delta = scaleAndCurveDelta(-34f)
        assertEquals(9, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 9 for -40`() {
        val delta = scaleAndCurveDelta(-40f)
        assertEquals(9, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 10 for -41`() {
        val delta = scaleAndCurveDelta(-41f)
        assertEquals(10, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 10 for -47`() {
        val delta = scaleAndCurveDelta(-47f)
        assertEquals(10, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 11 for -48`() {
        val delta = scaleAndCurveDelta(-48f)
        assertEquals(11, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 11 for -54`() {
        val delta = scaleAndCurveDelta(-54f)
        assertEquals(11, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 12 for -55`() {
        val delta = scaleAndCurveDelta(-55f)
        assertEquals(12, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 12 for -62`() {
        val delta = scaleAndCurveDelta(-62f)
        assertEquals(12, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 13 for -63`() {
        val delta = scaleAndCurveDelta(-63f)
        assertEquals(13, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 13 for -71`() {
        val delta = scaleAndCurveDelta(-71f)
        assertEquals(13, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 14 for -72`() {
        val delta = scaleAndCurveDelta(-72f)
        assertEquals(14, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 14 for -80`() {
        val delta = scaleAndCurveDelta(-80f)
        assertEquals(14, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 15 for -81`() {
        val delta = scaleAndCurveDelta(-81f)
        assertEquals(15, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 15 for -89`() {
        val delta = scaleAndCurveDelta(-89f)
        assertEquals(15, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 16 for -90`() {
        val delta = scaleAndCurveDelta(-90f)
        assertEquals(16, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 16 for -100`() {
        val delta = scaleAndCurveDelta(-100f)
        assertEquals(16, delta)
    }

    @Test
    fun `scaleAndCurveDelta returns 16 for Int MIN_VALUE`() {
        val delta = scaleAndCurveDelta(Int.MIN_VALUE.toFloat())
        assertEquals(16, delta)
    }
}