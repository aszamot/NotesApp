package pl.atk.notes.utils.extensions

import java.util.Calendar
import java.util.Date

fun Long.toDate() : Date {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this
    return calendar.time
}
