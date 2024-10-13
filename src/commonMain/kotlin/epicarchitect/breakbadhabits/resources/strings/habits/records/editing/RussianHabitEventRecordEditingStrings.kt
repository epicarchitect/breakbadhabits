package epicarchitect.breakbadhabits.resources.strings.habits.records.editing

import epicarchitect.breakbadhabits.habits.validation.HabitEventCountError
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
    override fun startDateTimeLabel() = "Начало"
    override fun endDateTimeLabel() = "Конец"
    override fun done() = "Готово"
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
}