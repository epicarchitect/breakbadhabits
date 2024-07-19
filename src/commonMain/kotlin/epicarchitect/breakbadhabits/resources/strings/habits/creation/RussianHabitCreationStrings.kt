package epicarchitect.breakbadhabits.resources.strings.habits.creation

import epicarchitect.breakbadhabits.habits.validation.DailyHabitEventCountError
import epicarchitect.breakbadhabits.habits.validation.HabitNewNameError
import epicarchitect.breakbadhabits.screens.habits.creation.HabitDuration

class RussianHabitCreationStrings : HabitCreationStrings {
    override fun titleText() = "Новая привычка"
    override fun habitNameDescription() = "Введите название привычки, например курение."
    override fun habitNameTitle() = "Название"
    override fun habitIconTitle() = "Иконка"
    override fun habitEventCountTitle() = "Частота"
    override fun habitDurationTitle() = "Продолжительность"
    override fun habitIconDescription() = "Выберите подходящую иконку для привычки."
    override fun finishButtonText() = "Готово"
    override fun habitNameError(error: HabitNewNameError) = when (error) {
        HabitNewNameError.AlreadyUsed -> "Это название уже используется."
        HabitNewNameError.Empty       -> "Название не может быть пустым."
        is HabitNewNameError.TooLong  -> {
            "Название не может быть длиннее чем ${error.maxLength} символов."
        }
    }

    override fun habitDuration(duration: HabitDuration) = when (duration) {
        HabitDuration.MONTH_1 -> "1 месяц"
        HabitDuration.MONTH_3 -> "3 месяца"
        HabitDuration.MONTH_6 -> "6 месяцев"
        HabitDuration.YEAR_1  -> "1 год"
        HabitDuration.YEAR_2  -> "2 года"
        HabitDuration.YEAR_3  -> "3 года"
        HabitDuration.YEAR_4  -> "4 года"
        HabitDuration.YEAR_5  -> "5 лет"
        HabitDuration.YEAR_6  -> "6 лет"
        HabitDuration.YEAR_7  -> "7 лет"
        HabitDuration.YEAR_8  -> "8 лет"
        HabitDuration.YEAR_9  -> "9 лет"
        HabitDuration.YEAR_10 -> "10 лет"
    }
    override fun habitDurationDescription() = "Укажите примерно как давно у вас эта привычка."
    override fun trackEventCountError(reason: DailyHabitEventCountError) = when (reason) {
        DailyHabitEventCountError.Empty -> {
            "Поле не может быть пустым"
        }
    }
    override fun trackEventCountDescription() = "Укажите сколько примерно было событий привычки в день."
}