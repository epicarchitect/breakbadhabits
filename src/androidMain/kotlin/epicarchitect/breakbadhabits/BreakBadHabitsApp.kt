package epicarchitect.breakbadhabits

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import epicarchitect.breakbadhabits.database.AppSettingsTheme
import epicarchitect.breakbadhabits.sqldelight.flowOfOneOrNull
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class BreakBadHabitsApp : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        MigrationToV4(this).executeIfNeeded()
        handleDarkMode()
    }

    companion object {
        lateinit var instance: BreakBadHabitsApp
    }
}

private fun handleDarkMode() {
    Environment.database.appSettingsQueries.settings().flowOfOneOrNull().onEach {
        AppCompatDelegate.setDefaultNightMode(
            when (it?.theme) {
                AppSettingsTheme.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
                AppSettingsTheme.DARK -> AppCompatDelegate.MODE_NIGHT_YES
                else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            }
        )
    }.launchIn(CoroutineScope(Dispatchers.Main))
}