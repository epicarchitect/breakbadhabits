package epicarchitect.breakbadhabits

import android.app.Application
import android.content.ComponentCallbacks
import android.content.res.Configuration
import epicarchitect.breakbadhabits.environment.language.ActualSystemLanguage
import epicarchitect.breakbadhabits.ui.theme.handleDarkMode
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

        registerComponentCallbacks(
            object : ComponentCallbacks {
                override fun onConfigurationChanged(newConfig: Configuration) {
                    ActualSystemLanguage.update(newConfig.locales.get(0))
                }

                override fun onLowMemory() {}
            }
        )
    }

    companion object {
        lateinit var instance: BreakBadHabitsApp
    }
}