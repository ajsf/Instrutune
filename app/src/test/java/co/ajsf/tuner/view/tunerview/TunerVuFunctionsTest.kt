package co.ajsf.tuner.view.tunerview

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class TunerVuFunctionsTest {

    @Test
    fun `calculateTunerXValue returns half of the viewWidth when delta is 0`() {
        val newX = calculateTunerXValue(1000, 0f)
        assertEquals(500f, newX)
    }

    @Test
    fun `calculateTunerXValue returns the viewWidth value when delta is 100`() {
        val newX = calculateTunerXValue(1000, 100f)
        assertEquals(1000f, newX)
    }

    @Test
    fun `calculateTunerXValue returns 0 when delta is -100`() {
        val newX = calculateTunerXValue(1000, -100f)
        assertEquals(0f, newX)
    }
}