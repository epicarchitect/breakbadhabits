package epicarchitect.breakbadhabits.data.resources.strings

interface AppSettingsStrings {
    fun titleText(): String
    fun themeSelectionDescription(): String
    fun themeSelectionSystemTheme(): String
    fun themeSelectionDarkTheme(): String
    fun themeSelectionLightTheme(): String
    fun widgetsDescription(): String
    fun widgetsButton(): String
}

class RussianAppSettingsStrings : AppSettingsStrings {
    override fun titleText() = "Настройки"
    override fun themeSelectionDescription() = "Выберите тему оформления приложения:"
    override fun themeSelectionSystemTheme() = "Как в системе"
    override fun themeSelectionDarkTheme() = "Темная тема"
    override fun themeSelectionLightTheme() = "Светлая тема"
    override fun widgetsButton() = "Настройте свои виджеты:"
    override fun widgetsDescription() = "Настройки виджетов"
}

class EnglishAppSettingsStrings : AppSettingsStrings {
    override fun titleText() = "Settings"
    override fun themeSelectionDescription() = "Select a theme for the application:"
    override fun themeSelectionSystemTheme() = "As in the system"
    override fun themeSelectionDarkTheme() = "Dark theme"
    override fun themeSelectionLightTheme() = "Light theme"
    override fun widgetsButton() = "Widget settings"
    override fun widgetsDescription() = "Customize your widgets:"
}