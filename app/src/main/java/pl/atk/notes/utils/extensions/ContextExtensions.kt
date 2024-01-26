package pl.atk.notes.utils.extensions

import android.content.Context

fun Context.dpToPixels(dp: Int): Int {
    return (dp * resources.displayMetrics.density).toInt()
}