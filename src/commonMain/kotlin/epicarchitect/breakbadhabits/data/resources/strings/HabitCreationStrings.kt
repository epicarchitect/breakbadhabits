package epicarchitect.breakbadhabits.data.resources.strings

import epicarchitect.breakbadhabits.operation.habits.validation.HabitNewNameIncorrectReason
import epicarchitect.breakbadhabits.operation.habits.validation.HabitTrackEventCountIncorrectReason
import epicarchitect.breakbadhabits.ui.screen.habits.creation.HabitCreationTime

interface HabitCreationStrings {
    fun titleText(): String
    fun habitNameDescription(): String
    fun habitNameLabel(): String
    fun habitIconDescription(): String
    fun finishButtonText(): String
    fun habitNameValidationError(reason: HabitNewNameIncorrectReason): String
    fun habitTime(time: HabitCreationTime): String // move to duration formatting?
    fun habitTimeDescription(): String
    fun trackEventCountError(reason: HabitTrackEventCountIncorrectReason): String
    fun trackEventCountDescription(): String
    fun trackEventCountLabel(): String
}

class RussianHabitCreationStrings : HabitCreationStrings {
    override fun titleText() = "Новая привычка"
    override fun habitNameDescription() = "Введите название привычки, например курение."
    override fun habitNameLabel() = "Название привычки"
    override fun habitIconDescription() = "Выберите подходящую иконку для привычки."
    override fun finishButtonText() = "Готово"
    override fun habitNameValidationError(reason: HabitNewNameIncorrectReason) = when (reason) {
        HabitNewNameIncorrectReason.AlreadyUsed -> "Это название уже используется."
        HabitNewNameIncorrectReason.Empty       -> "Название не может быть пустым."
        is HabitNewNameIncorrectReason.TooLong  -> {
            "Название не может быть длиннее чем ${reason.maxLength} символов."
        }
    }

    override fun habitTime(time: HabitCreationTime) = when (time) {
        HabitCreationTime.MONTH_1 -> "1 месяц"
        HabitCreationTime.MONTH_3 -> "3 месяца"
        HabitCreationTime.MONTH_6 -> "6 месяцев"
        HabitCreationTime.YEAR_1  -> "1 год"
        HabitCreationTime.YEAR_2  -> "2 года"
        HabitCreationTime.YEAR_3  -> "3 года"
        HabitCreationTime.YEAR_4  -> "4 года"
        HabitCreationTime.YEAR_5  -> "5 лет"
        HabitCreationTime.YEAR_6  -> "6 лет"
        HabitCreationTime.YEAR_7  -> "7 лет"
        HabitCreationTime.YEAR_8  -> "8 лет"
        HabitCreationTime.YEAR_9  -> "9 лет"
        HabitCreationTime.YEAR_10 -> "10 лет"
    }

    override fun habitTimeDescription() = "Укажите примерно как давно у вас эта привычка:"

    override fun trackEventCountError(reason: HabitTrackEventCountIncorrectReason) = when (reason) {
        HabitTrackEventCountIncorrectReason.Empty -> {
            "Поле не может быть пустым"
        }
    }

    override fun trackEventCountDescription() = "Укажите сколько примерно было событий привычки в день:"
    override fun trackEventCountLabel() = "Число событий в день"
}

class EnglishHabitCreationStrings : HabitCreationStrings {
    override fun titleText() = "New habit"
    override fun habitNameDescription() = "Enter a name for the habit, such as smoking."
    override fun habitNameLabel() = "Habit name"
    override fun habitIconDescription() = "Choose the appropriate icon for the habit."
    override fun finishButtonText() = "Done"
    override fun habitNameValidationError(reason: HabitNewNameIncorrectReason) = when (reason) {
        HabitNewNameIncorrectReason.AlreadyUsed -> "This name has already been used."
        HabitNewNameIncorrectReason.Empty       -> "The title cannot be empty."
        is HabitNewNameIncorrectReason.TooLong  -> {
            "The name cannot be longer than ${reason.maxLength} characters."
        }
    }

    override fun habitTime(time: HabitCreationTime) = when (time) {
        HabitCreationTime.MONTH_1 -> "1 month"
        HabitCreationTime.MONTH_3 -> "3 months"
        HabitCreationTime.MONTH_6 -> "6 month"
        HabitCreationTime.YEAR_1  -> "1 year"
        HabitCreationTime.YEAR_2  -> "2 years"
        HabitCreationTime.YEAR_3  -> "3 years"
        HabitCreationTime.YEAR_4  -> "4 years"
        HabitCreationTime.YEAR_5  -> "5 years"
        HabitCreationTime.YEAR_6  -> "6 years"
        HabitCreationTime.YEAR_7  -> "7 years"
        HabitCreationTime.YEAR_8  -> "8 years"
        HabitCreationTime.YEAR_9  -> "9 years"
        HabitCreationTime.YEAR_10 -> "10 years"
    }

    override fun habitTimeDescription() = "Please indicate approximately how long you have had this habit:"

    override fun trackEventCountError(reason: HabitTrackEventCountIncorrectReason) = when (reason) {
        HabitTrackEventCountIncorrectReason.Empty -> {
            "Cant be empty"
        }
    }

    override fun trackEventCountDescription() = "Please indicate approximately how many habit events occurred per day:"
    override fun trackEventCountLabel() = "Number of events per day"
}