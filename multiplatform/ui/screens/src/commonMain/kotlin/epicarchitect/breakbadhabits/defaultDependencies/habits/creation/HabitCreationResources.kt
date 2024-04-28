package epicarchitect.breakbadhabits.defaultDependencies.habits.creation

import androidx.compose.ui.text.intl.Locale
import epicarchitect.breakbadhabits.features.habits.creation.HabitCreationResources
import epicarchitect.breakbadhabits.newarch.habits.DefaultHabitCreationOperation
import epicarchitect.breakbadhabits.validator.IncorrectHabitNewName
import epicarchitect.breakbadhabits.validator.IncorrectHabitTrackEventCount

class RussianHabitCreationResources : HabitCreationResources {
    override fun titleText() = "Новая привычка"
    override fun habitNameDescription() = "Введите название привычки, например курение."
    override fun habitNameLabel() = "Название привычки"
    override fun habitIconDescription() = "Выберите подходящую иконку для привычки."
    override fun finishButtonText() = "Создать привычку"
    override fun habitNameValidationError(reason: IncorrectHabitNewName.Reason) = when (reason) {
        IncorrectHabitNewName.Reason.AlreadyUsed -> "Это название уже используется."
        IncorrectHabitNewName.Reason.Empty -> "Название не может быть пустым."
        is IncorrectHabitNewName.Reason.TooLong -> {
            "Название не может быть длиннее чем ${reason.maxLength} символов."
        }
    }

    override fun habitTime(habitTime: DefaultHabitCreationOperation.HabitTime) = when (habitTime) {
        DefaultHabitCreationOperation.HabitTime.MONTH_1 -> "1 месяц"
        DefaultHabitCreationOperation.HabitTime.MONTH_3 -> "3 месяца"
        DefaultHabitCreationOperation.HabitTime.MONTH_6 -> "6 месяцев"
        DefaultHabitCreationOperation.HabitTime.YEAR_1 -> "1 год"
        DefaultHabitCreationOperation.HabitTime.YEAR_2 -> "2 года"
        DefaultHabitCreationOperation.HabitTime.YEAR_3 -> "3 года"
        DefaultHabitCreationOperation.HabitTime.YEAR_4 -> "4 года"
        DefaultHabitCreationOperation.HabitTime.YEAR_5 -> "5 лет"
        DefaultHabitCreationOperation.HabitTime.YEAR_6 -> "6 лет"
        DefaultHabitCreationOperation.HabitTime.YEAR_7 -> "7 лет"
        DefaultHabitCreationOperation.HabitTime.YEAR_8 -> "8 лет"
        DefaultHabitCreationOperation.HabitTime.YEAR_9 -> "9 лет"
        DefaultHabitCreationOperation.HabitTime.YEAR_10 -> "10 лет"
    }

    override fun trackEventCountError(reason: IncorrectHabitTrackEventCount.Reason) = when (reason) {
        IncorrectHabitTrackEventCount.Reason.Empty -> {
            "Поле не может быть пустым"
        }
    }
}

class EnglishHabitCreationResources : HabitCreationResources {
    override fun titleText() = "New habit"
    override fun habitNameDescription() = "Enter a name for the habit, such as smoking."
    override fun habitNameLabel() = "Habit name"
    override fun habitIconDescription() = "Choose the appropriate icon for the habit."
    override fun finishButtonText() = "Create a habit"
    override fun habitNameValidationError(reason: IncorrectHabitNewName.Reason) = when (reason) {
        IncorrectHabitNewName.Reason.AlreadyUsed -> "This name has already been used."
        IncorrectHabitNewName.Reason.Empty -> "The title cannot be empty."
        is IncorrectHabitNewName.Reason.TooLong -> {
            "The name cannot be longer than ${reason.maxLength} characters."
        }
    }

    override fun habitTime(habitTime: DefaultHabitCreationOperation.HabitTime) = when (habitTime) {
        DefaultHabitCreationOperation.HabitTime.MONTH_1 -> "1 month"
        DefaultHabitCreationOperation.HabitTime.MONTH_3 -> "3 months"
        DefaultHabitCreationOperation.HabitTime.MONTH_6 -> "6 month"
        DefaultHabitCreationOperation.HabitTime.YEAR_1 -> "1 year"
        DefaultHabitCreationOperation.HabitTime.YEAR_2 -> "2 years"
        DefaultHabitCreationOperation.HabitTime.YEAR_3 -> "3 years"
        DefaultHabitCreationOperation.HabitTime.YEAR_4 -> "4 years"
        DefaultHabitCreationOperation.HabitTime.YEAR_5 -> "5 years"
        DefaultHabitCreationOperation.HabitTime.YEAR_6 -> "6 years"
        DefaultHabitCreationOperation.HabitTime.YEAR_7 -> "7 years"
        DefaultHabitCreationOperation.HabitTime.YEAR_8 -> "8 years"
        DefaultHabitCreationOperation.HabitTime.YEAR_9 -> "9 years"
        DefaultHabitCreationOperation.HabitTime.YEAR_10 -> "10 years"
    }

    override fun trackEventCountError(reason: IncorrectHabitTrackEventCount.Reason) = when (reason) {
        IncorrectHabitTrackEventCount.Reason.Empty -> {
            "Cant be empty"
        }
    }
}

class LocalizedHabitCreationResources(locale: Locale) : HabitCreationResources by resolve(locale)

private fun resolve(locale: Locale) = if (locale.language == "ru") {
    RussianHabitCreationResources()
} else {
    EnglishHabitCreationResources()
}