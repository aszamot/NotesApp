package pl.atk.notes.utils.extensions

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Calendar

class LongExtensionsTest {

    @Test
    fun longExtension_shouldReturnDateFromTimeStamp() {
        val timestamp = 1717598083000L
        val expectedDate = Calendar.getInstance().apply {
            timeInMillis = timestamp
        }.time

        val result = timestamp.toDate()
        assertEquals(expectedDate, result)
    }
}