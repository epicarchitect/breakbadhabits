package epicarchitect.breakbadhabits.resources.strings.habits.records.editing

import epicarchitect.breakbadhabits.habits.validation.DailyHabitEventCountError
import epicarchitect.breakbadhabits.habits.validation.HabitEventRecordTimeRangeError

class RussianHabitEventRecordEditingStrings : HabitEventRecordEditingStrings {
    override fun titleText(habitName: String) = "Редактирование записи — $habitName"
    override fun commentDescription() = "Вы можете написать комментарий, но это не обязательно."
    override fun commentTitle() = "Комментарий"
    override fun finishDescription() = "Вы всегда сможете изменить или удалить эту запись."
    override fun finishButton() = "Сохранить изменения"
    override fun deleteConfirmation() = "Вы уверены, что хотите удалить эту запись?"
    override fun yes() = "Да"
    override fun cancel() = "Отмена"
    override fun deleteDescription() = "Вы можете удалить эту запись."
    override fun deleteButton() = "Удалить запись"
    override fun dailyEventCountError(error: DailyHabitEventCountError) = when (error) {
        DailyHabitEventCountError.Empty -> {
            "Поле не может быть пустым"
        }
    }
    override fun timeRangeError(error: HabitEventRecordTimeRangeError) = when (error) {
        HabitEventRecordTimeRangeError.BiggestThenCurrentTime -> "Дата и время не могут быть больше текущего времени."
    }

    override fun timeRangeTitle() = "Временной диапазон"
    override fun startDateTimeLabel() = "Первое событие"
    override fun endDateTimeLabel() = "Последнее событие"
    override fun done() = "Готово"

    override fun dailyEventCountDescription() = "Укажите сколько примерно было событий привычки каждый день."
    override fun dailyEventCountLabel() = "Число событий в день"
    override fun timeRangeDescription() = "Укажите время первого и последнего события привычки."
}