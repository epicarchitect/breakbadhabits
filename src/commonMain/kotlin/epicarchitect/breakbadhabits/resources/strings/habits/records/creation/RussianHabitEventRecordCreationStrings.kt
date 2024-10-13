package epicarchitect.breakbadhabits.resources.strings.habits.records.creation

import epicarchitect.breakbadhabits.habits.validation.HabitEventCountError
import epicarchitect.breakbadhabits.habits.validation.HabitEventRecordTimeRangeError

class RussianHabitEventRecordCreationStrings : HabitEventRecordCreationStrings {
    override fun titleText(habitName: String) = "Новая запись — $habitName"
    override fun commentDescription() = "Вы можете написать комментарий, но это не обязательно."
    override fun commentTitle() = "Комментарий"
    override fun finishDescription() = "Вы всегда сможете изменить или удалить эту запись."
    override fun finishButton() = "Готово"
    override fun eventCountError(error: HabitEventCountError) = when (error) {
        HabitEventCountError.Empty -> {
            "Поле не может быть пустым"
        }
    }

    override fun timeRangeTitle() = "Временной диапазон"
    override fun timeRangeError(error: HabitEventRecordTimeRangeError) = when (error) {
        HabitEventRecordTimeRangeError.BiggestThenCurrentTime -> "Дата и время не могут быть больше текущего времени."
    }

    override fun eventCountTitle() = "Число событий"
    override fun eventCountDescription() = "Введите сколько было событий привычки."
    override fun timeRangeDescription() =
        "Введите диапазон времени в котором происходили события привычки."

    override fun now() = "Сейчас"
    override fun yesterday() = "Вчера"
    override fun yourTimeRange() = "Свой интервал"
    override fun startDateTimeLabel() = "Начало"
    override fun endDateTimeLabel() = "Конец"
    override fun done() = "Готово"
}