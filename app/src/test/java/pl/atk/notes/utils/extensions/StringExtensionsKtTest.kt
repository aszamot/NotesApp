package pl.atk.notes.utils.extensions

import org.junit.Assert.assertEquals
import org.junit.Test

class StringExtensionsKtTest {

    @Test
    fun stringEmptyExtension_shouldReturnEmptyString() {
        val emptyString = String.empty

        assertEquals("", emptyString)
    }
}