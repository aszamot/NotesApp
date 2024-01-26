package pl.atk.notes.utils.extensions

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import pl.atk.notes.R

fun SwipeRefreshLayout.setupToolbarOffset() {
    setProgressViewOffset(
        true,
        resources.getDimensionPixelSize(R.dimen.refresher_offset_start),
        resources.getDimensionPixelSize(R.dimen.refresher_offset_end)
    )
}