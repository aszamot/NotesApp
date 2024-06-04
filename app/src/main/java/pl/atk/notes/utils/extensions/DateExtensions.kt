package pl.atk.notes.utils.extensions

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Date.toReadableString(): String {
    val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
    return dateFormat.format(this)
}