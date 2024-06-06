package pl.atk.notes.utils.extensions

import org.junit.Assert.*
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Locale

class DateExtensionsTest {

    @Test
    fun dateToReadableStringExtension_shouldReturnReadableString() {
        val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
        val date = dateFormat.parse("01.01.2000 00:00")
        assertEquals("01.01.2000 00:00", date.toReadableString())
    }
}