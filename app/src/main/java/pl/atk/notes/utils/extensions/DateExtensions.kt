package pl.atk.notes.utils.extensions

import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun Date.toReadableString(): String {
    val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
    val defaultTimeZone = TimeZone.getDefault()
    Timber.d(defaultTimeZone.toString())
    dateFormat.timeZone = defaultTimeZone
    return dateFormat.format(this)
}