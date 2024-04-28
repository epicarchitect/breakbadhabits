package epicarchitect.breakbadhabits.defaultDependencies.appSettings

import androidx.compose.ui.text.intl.Locale
import epicarchitect.breakbadhabits.features.appSettings.AppSettingsResources

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

class LocalizedAppSettingsResources(locale: Locale) : AppSettingsResources by (
    if (locale.language == "ru") {
        RussianAppSettingsResources()
    } else {
        EnglishAppSettingsResources()
    }
)