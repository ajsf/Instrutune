package co.ajsf.tuner.features.tuner.view.tunerview

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class TunerVuFunctionsTest {

    @Test
    fun `calculateVuMeterXTranslation returns 0 when delta is 0`() {
        val newX = calculateVuMeterXTranslation(1000, 0f)
        assertEquals(0f, newX)
    }

    @Test
    fun `calculateVuMeterXTranslation returns half the viewWidth when delta is 100`() {
        val newX = calculateVuMeterXTranslation(1000, 100f)
        assertEquals(500f, newX)
    }

    @Test
    fun `calculateVuMeterXTranslation returns negative half view width when delta is -100`() {
        val newX = calculateVuMeterXTranslation(1000, -100f)
        assertEquals(-500f, newX)
    }
}