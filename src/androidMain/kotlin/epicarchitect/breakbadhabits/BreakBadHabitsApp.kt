package epicarchitect.breakbadhabits

import android.app.Application
import android.content.ComponentCallbacks
import android.content.res.Configuration
import epicarchitect.breakbadhabits.environment.Environment
import epicarchitect.breakbadhabits.environment.language.ActualSystemLanguage
import epicarchitect.breakbadhabits.environment.language.AppLanguage
import epicarchitect.breakbadhabits.ui.format.updateFormatters
import epicarchitect.breakbadhabits.ui.theme.handleDarkMode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.Locale

class BreakBadHabitsApp : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
        val scope = CoroutineScope(Dispatchers.Main)

        scope.launch {
            handleDarkMode()
        }

        Environment.appLanguage.state.onEach {
            updateFormatters(
                this@BreakBadHabitsApp,
                when (it) {
                    AppLanguage.RUSSIAN -> Locale("ru")
                    AppLanguage.ENGLISH -> Locale("en")
                }
            )
        }.launchIn(scope)


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