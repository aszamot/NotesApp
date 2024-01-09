package pl.atk

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import pl.atk.notes.BuildConfig
import timber.log.Timber

@HiltAndroidApp
class NotesApp : Application() {

    override fun onCreate() {
        super.onCreate()
        setupTimber()
    }

    private fun setupTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}