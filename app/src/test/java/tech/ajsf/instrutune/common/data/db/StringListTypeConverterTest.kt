package tech.ajsf.instrutune.common.data.db

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class StringListTypeConverterTest {

    @Test
    fun `stringToStringList works properly`() {
        val string = "a,b,c,d,e,f,g"
        val converter = StringListTypeConverter()

        val list = converter.stringToStringList(string)

        val expectedList = listOf("a", "b", "c", "d", "e", "f", "g")

        assertEquals(expectedList, list)
    }

    @Test
    fun `stringToStringList returns an empty list when the string is blank`() {
        val string = "  "
        val converter = StringListTypeConverter()

        val list = converter.stringToStringList(string)

        val expectedList = listOf<String>()

        assertEquals(expectedList, list)
    }

    @Test
    fun `stringListToString works properly`() {
        val list = listOf("a", "b", "c", "d", "e", "f", "g")

        val converter = StringListTypeConverter()

        val string = converter.stringListToString(list)

        val expectedString = "a,b,c,d,e,f,g"

        assertEquals(expectedString, string)
    }

    @Test
    fun `stringListToString returns a blank string when the list is empty`() {
        val list = listOf<String>()

        val converter = StringListTypeConverter()

        val string = converter.stringListToString(list)

        val expectedString = ""

        assertEquals(expectedString, string)
    }
}