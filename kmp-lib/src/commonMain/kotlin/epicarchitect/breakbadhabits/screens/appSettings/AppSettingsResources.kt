package epicarchitect.breakbadhabits.screens.appSettings

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.text.intl.Locale

val LocalAppSettingsResources = compositionLocalOf {
    if (Locale.current.language == "ru") {
        RussianAppSettingsResources()
    } else {
        EnglishAppSettingsResources()
    }
}

interface AppSettingsResources {
    fun titleText(): String
    fun themeSelectionDescription(): String
    fun themeSelectionSystemTheme(): String
    fun themeSelectionDarkTheme(): String
    fun themeSelectionLightTheme(): String
    fun widgetsDescription(): String
    fun widgetsButton(): String
}

class RussianAppSettingsResources : AppSettingsResources {
    override fun titleText() = "Настройки"
    override fun themeSelectionDescription() = "Выберите тему оформления приложения"
    override fun themeSelectionSystemTheme() = "Как в системе"
    override fun themeSelectionDarkTheme() = "Темная тема"
    override fun themeSelectionLightTheme() = "Светлая тема"
    override fun widgetsDescription() = "Настройки виджетов"
    override fun widgetsButton() = "Настройте свои виджеты"
}

class EnglishAppSettingsResources : AppSettingsResources {
    override fun titleText() = "Settings"
    override fun themeSelectionDescription() = "Select a theme for the application"
    override fun themeSelectionSystemTheme() = "As in the system"
    override fun themeSelectionDarkTheme() = "Dark theme"
    override fun themeSelectionLightTheme() = "Light theme"
    override fun widgetsDescription() = "Customize your widgets"
    override fun widgetsButton() = "Widget settings"
}