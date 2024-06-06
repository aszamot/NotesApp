package pl.atk.notes.utils.extensions

import android.content.Context
import android.content.res.Configuration

fun Context.dpToPixels(dp: Int): Int {
    return (dp * resources.displayMetrics.density).toInt()
}

fun Context.isInNightMode(): Boolean {
    val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
    return currentNightMode == Configuration.UI_MODE_NIGHT_YES
}