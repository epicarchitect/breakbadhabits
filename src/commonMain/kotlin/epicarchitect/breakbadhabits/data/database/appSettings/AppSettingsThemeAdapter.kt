package epicarchitect.breakbadhabits.data.database.appSettings

import app.cash.sqldelight.ColumnAdapter

object AppSettingsThemeAdapter : ColumnAdapter<AppSettingsTheme, Long> {
    override fun decode(databaseValue: Long) = when (databaseValue) {
        1L   -> AppSettingsTheme.LIGHT
        2L   -> AppSettingsTheme.DARK
        else -> AppSettingsTheme.SYSTEM
    }

    override fun encode(value: AppSettingsTheme) = when (value) {
        AppSettingsTheme.SYSTEM -> 0L
        AppSettingsTheme.LIGHT  -> 1L
        AppSettingsTheme.DARK   -> 2L
    }
}