package pl.atk.notes.presentation.main

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import pl.atk.notes.databinding.ActivityMainBinding

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Test
    fun activityOnCreate_bindingCorrect() {
        val scenario = launchActivity<MainActivity>()

        scenario.moveToState(Lifecycle.State.CREATED)
        scenario.onActivity { activity ->
            val binding = ActivityMainBinding.bind(activity.findViewById(android.R.id.content))
            assertNotNull(binding)
        }
    }
}