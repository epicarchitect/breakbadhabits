package epicarchitect.breakbadhabits

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BreakBadHabitsApp : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
        CoroutineScope(Dispatchers.Main).launch {
            handleDarkMode()
        }
    }

    companion object {
        lateinit var instance: BreakBadHabitsApp
    }
}