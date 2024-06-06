package pl.atk.notes.utils.extensions

import org.junit.Assert.assertEquals
import org.junit.Test

class StringExtensionsTest {

    @Test
    fun stringEmptyExtension_shouldReturnEmptyString() {
        val emptyString = String.empty

        assertEquals("", emptyString)
    }
}