package epicarchitect.breakbadhabits.screens

import androidx.compose.ui.text.intl.Locale
import epicarchitect.breakbadhabits.logic.habits.validator.IncorrectHabitNewName
import epicarchitect.breakbadhabits.screens.dashboard.DashboardResources
import epicarchitect.breakbadhabits.screens.habits.HabitCreationResources
import epicarchitect.breakbadhabits.screens.settings.AppSettingsResources

fun dashboardResourcesOf(locale: Locale) = when (locale.language) {
    "ru" -> object : DashboardResources {
        override val titleText = "Сломай плохие привычки"
        override val newHabitButtonText = "Создать новую привычку"
        override val emptyHabitsText = "У вас нет плохих привычек!.. Или есть?"
        override val habitHasNoEvents = "События отсутствуют"
    }

    else -> object : DashboardResources {
        override val titleText = "Break Bad Habits"
        override val newHabitButtonText = "Create new habit"
        override val emptyHabitsText = "You have no bad habits!.. Or not?"
        override val habitHasNoEvents = "No events"
    }
}

fun appSettingsResourcesOf(locale: Locale) = when (locale.language) {
    "ru" -> object : AppSettingsResources {
        override val titleText = "Настройки"
        override val themeSelectionDescription = "Выберите тему оформления приложения"
        override val themeSelectionSystemTheme = "Как в системе"
        override val themeSelectionDarkTheme = "Темная тема"
        override val themeSelectionLightTheme = "Светлая тема"
        override val widgetsDescription = "Настройки виджетов"
        override val widgetsButton = "Настройте свои виджеты"
    }

    else -> object : AppSettingsResources {
        override val titleText = "Settings"
        override val themeSelectionDescription = "Select a theme for the application"
        override val themeSelectionSystemTheme = "As in the system"
        override val themeSelectionDarkTheme = "Dark theme"
        override val themeSelectionLightTheme = "Light theme"
        override val widgetsDescription = "Customize your widgets"
        override val widgetsButton = "Widget settings"
    }
}

fun habitCreationResourcesOf(locale: Locale) = when (locale.language) {
    "ru" -> object : HabitCreationResources {
        override val titleText = "Новая привычка"
        override val habitNameDescription = "Введите название привычки, например курение."
        override val habitNameLabel = "Название привычки"
        override val habitIconDescription = "Выберите подходящую иконку для привычки."
        override val finishButtonText = "Создать привычку"
        override fun habitNameValidationError(
            reason: IncorrectHabitNewName.Reason
        ) = when (reason) {
            IncorrectHabitNewName.Reason.AlreadyUsed -> "Это название уже используется."
            IncorrectHabitNewName.Reason.Empty -> "Название не может быть пустым."
            is IncorrectHabitNewName.Reason.TooLong -> "Название не может быть длиннее чем ${reason.maxLength} символов."
        }
    }

    else -> object : HabitCreationResources {
        override val titleText = "New habit"
        override val habitNameDescription = "Enter a name for the habit, such as smoking."
        override val habitNameLabel = "Habit name"
        override val habitIconDescription = "Choose the appropriate icon for the habit."
        override val finishButtonText = "Create a habit"
        override fun habitNameValidationError(
            reason: IncorrectHabitNewName.Reason
        ) = when (reason) {
            IncorrectHabitNewName.Reason.AlreadyUsed -> "This name has already been used."
            IncorrectHabitNewName.Reason.Empty -> "The title cannot be empty."
            is IncorrectHabitNewName.Reason.TooLong -> "The name cannot be longer than ${reason.maxLength} characters."
        }
    }
}