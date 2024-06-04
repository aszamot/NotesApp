package pl.atk.notes.utils.extensions

import java.util.Date

fun Long.toDate() : Date {
    return Date(this)
}
