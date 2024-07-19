package epicarchitect.breakbadhabits.resources.strings.habits.records.creation

import epicarchitect.breakbadhabits.habits.validation.DailyHabitEventCountError
import epicarchitect.breakbadhabits.habits.validation.HabitEventRecordTimeRangeError

class RussianHabitEventRecordCreationStrings : HabitEventRecordCreationStrings {
    override fun titleText(habitName: String) = "Новая запись — $habitName"
    override fun commentDescription() = "Вы можете написать комментарий, но это не обязательно."
    override fun commentTitle() = "Комментарий"
    override fun finishDescription() = "Вы всегда сможете изменить или удалить эту запись."
    override fun finishButton() = "Готово"
    override fun dailyEventCountError(error: DailyHabitEventCountError) = when (error) {
        DailyHabitEventCountError.Empty -> {
            "Поле не может быть пустым"
        }
    }
    override fun timeRangeTitle() = "Временной диапазон"
    override fun timeRangeError(error: HabitEventRecordTimeRangeError) = when (error) {
        HabitEventRecordTimeRangeError.BiggestThenCurrentTime -> "Дата и время не могут быть больше текущего времени."
    }
    override fun dailyEventCountDescription() = "Укажите сколько примерно было событий привычки в день."
    override fun dailyEventCountTitle() = "Частота"
    override fun timeRangeDescription() = "Укажите время первого и последнего события привычки."
    override fun now() = "Сейчас"
    override fun yesterday() = "Вчера"
    override fun yourTimeRange() = "Свой интервал"
    override fun startDateTimeLabel() = "Первое событие"
    override fun endDateTimeLabel() = "Последнее событие"
    override fun done() = "Готово"
}