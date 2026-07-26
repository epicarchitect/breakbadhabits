package epicarchitect.breakbadhabits.resources.strings.habits.eventRecords.editing

import epicarchitect.breakbadhabits.habits.HabitEventCountError
import epicarchitect.breakbadhabits.habits.HabitEventRecordTimeRangeError

class RussianHabitEventRecordEditingStrings : HabitEventRecordEditingStrings {
    override fun titleText(
        isNewRecord: Boolean,
        habitName: String
    ) = if (isNewRecord) {
        "Новая запись — $habitName"
    } else {
        "Редактирование записи — $habitName"
    }

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
    override fun inputDateTimeAsRangeCheckbox() = "Указать как временной диапазон"

    override fun eventCountError(error: HabitEventCountError) = when (error) {
        HabitEventCountError.Empty -> {
            "Поле не может быть пустым"
        }
    }

    override fun timeRangeTitle() = "Дата и время"
    override fun timeRangeError(error: HabitEventRecordTimeRangeError) = when (error) {
        HabitEventRecordTimeRangeError.BiggestThenCurrentTime -> "Дата и время не могут быть больше текущего времени."
    }

    override fun eventCountTitle() = "Число событий"
    override fun eventCountDescription() = "Введите сколько было событий привычки."
    override fun timeRangeDescription() = "Выберите время когда происходили события привычки."
}