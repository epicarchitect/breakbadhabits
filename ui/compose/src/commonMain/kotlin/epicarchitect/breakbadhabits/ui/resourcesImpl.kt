package epicarchitect.breakbadhabits.ui

import androidx.compose.ui.text.intl.Locale
import epicarchitect.breakbadhabits.ui.dashboard.DashboardResources
import epicarchitect.breakbadhabits.ui.settings.AppSettingsResources

fun dashboardResourcesOf(locale: Locale) = when (locale.language) {
    "ru" -> DashboardResources(
        titleText = "Сломай плохие привычки",
        newHabitButtonText = "Создать новую привычку",
        emptyHabitsText = "У вас нет плохих привычек!.. Или есть?",
        habitHasNoEvents = "События отсутствуют"
    )

    else -> DashboardResources(
        titleText = "Break Bad Habits",
        newHabitButtonText = "Create new habit",
        emptyHabitsText = "You have no bad habits!.. Or not?",
        habitHasNoEvents = "No events"
    )
}

fun appSettingsResourcesOf(locale: Locale) = when (locale.language) {
    "ru" -> AppSettingsResources(
        titleText = "Настройки",
        themeSelectionDescription = "Выберите тему оформления приложения",
        themeSelectionLightTheme = "Светлая тема",
        themeSelectionDarkTheme = "Темная тема",
        themeSelectionSystemTheme = "Как в системе",
        widgetsButton = "Настройки виджетов",
        widgetsDescription = "Настройте свои виджеты"
    )

    else -> AppSettingsResources(
        titleText = "Settings",
        themeSelectionDescription = "Select a theme for the application",
        themeSelectionLightTheme = "Light theme",
        themeSelectionDarkTheme = "Dark theme",
        themeSelectionSystemTheme = "As in the system",
        widgetsButton = "Widget settings",
        widgetsDescription = "Customize your widgets"
    )
}