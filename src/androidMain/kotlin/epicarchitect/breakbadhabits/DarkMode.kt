package epicarchitect.breakbadhabits

import android.content.res.Configuration
import android.content.res.Resources
import androidx.appcompat.app.AppCompatDelegate
import epicarchitect.breakbadhabits.data.AppData
import epicarchitect.breakbadhabits.data.database.appSettings.AppSettingsTheme
import epicarchitect.breakbadhabits.operation.sqldelight.flowOfOneOrNull


fun isSystemDarkModeEnabled() = Resources.getSystem().configuration
    .uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES

suspend fun handleDarkMode() {
    AppData.database.appSettingsQueries.settings().flowOfOneOrNull().collect {
        AppCompatDelegate.setDefaultNightMode(
            when (it?.theme) {
                AppSettingsTheme.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
                AppSettingsTheme.DARK  -> AppCompatDelegate.MODE_NIGHT_YES
                else                   -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            }
        )
    }
}