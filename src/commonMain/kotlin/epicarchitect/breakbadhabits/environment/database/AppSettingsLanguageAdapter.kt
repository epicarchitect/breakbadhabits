package epicarchitect.breakbadhabits.environment.database

import app.cash.sqldelight.ColumnAdapter

object AppSettingsLanguageAdapter : ColumnAdapter<AppSettingsLanguage, Long> {
    override fun decode(databaseValue: Long) = when (databaseValue) {
        1L -> AppSettingsLanguage.ENGLISH
        2L -> AppSettingsLanguage.RUSSIAN
        else -> AppSettingsLanguage.SYSTEM
    }

    override fun encode(value: AppSettingsLanguage) = when (value) {
        AppSettingsLanguage.RUSSIAN -> 2L
        AppSettingsLanguage.ENGLISH -> 1L
        AppSettingsLanguage.SYSTEM -> 0L
    }
}