package epicarchitect.breakbadhabits.ui.theme

import android.content.res.Configuration
import android.content.res.Resources
import androidx.appcompat.app.AppCompatDelegate
import epicarchitect.breakbadhabits.environment.Environment
import epicarchitect.breakbadhabits.environment.database.AppSettingsTheme
import epicarchitect.breakbadhabits.operation.sqldelight.flowOfOneOrNull


fun isSystemDarkModeEnabled() = Resources.getSystem().configuration
    .uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES

suspend fun handleDarkMode() {
    Environment.database.appSettingsQueries.settings().flowOfOneOrNull().collect {
        AppCompatDelegate.setDefaultNightMode(
            when (it?.theme) {
                AppSettingsTheme.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
                AppSettingsTheme.DARK  -> AppCompatDelegate.MODE_NIGHT_YES
                else                   -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            }
        )
    }
}