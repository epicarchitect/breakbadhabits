package epicarchitect.breakbadhabits.data.resources.strings

import epicarchitect.breakbadhabits.operation.habits.validation.HabitEventRecordDailyEventCountIncorrectReason
import epicarchitect.breakbadhabits.operation.habits.validation.HabitEventRecordTimeRangeIncorrectReason

interface HabitEventRecordEditingStrings {
    fun dailyEventCountError(reason: HabitEventRecordDailyEventCountIncorrectReason): String
    fun titleText(habitName: String): String
    fun commentDescription(): String
    fun commentTitle(): String
    fun finishDescription(): String
    fun finishButton(): String
    fun deleteConfirmation(): String
    fun yes(): String
    fun cancel(): String
    fun deleteDescription(): String
    fun deleteButton(): String
    fun dailyEventCountDescription(): String
    fun dailyEventCountLabel(): String
    fun timeRangeDescription(): String
    fun timeRangeError(reason: HabitEventRecordTimeRangeIncorrectReason): String
    fun timeRangeTitle(): String
}

class RussianHabitEventRecordEditingStrings : HabitEventRecordEditingStrings {
    override fun titleText(habitName: String) = "Редактирование записи — ${habitName}"
    override fun commentDescription() = "Вы можете написать комментарий, но это не обязательно."
    override fun commentTitle() = "Комментарий"
    override fun finishDescription() = "Вы всегда сможете изменить или удалить эту запись."
    override fun finishButton() = "Сохранить изменения"
    override fun deleteConfirmation() = "Вы уверены, что хотите удалить эту запись?"
    override fun yes() = "Да"
    override fun cancel() = "Отмена"
    override fun deleteDescription() = "Вы можете удалить эту запись."
    override fun deleteButton() = "Удалить запись"
    override fun dailyEventCountError(reason: HabitEventRecordDailyEventCountIncorrectReason) = when (reason) {
        HabitEventRecordDailyEventCountIncorrectReason.Empty -> {
            "Поле не может быть пустым"
        }
    }
    override fun timeRangeError(reason: HabitEventRecordTimeRangeIncorrectReason) = when (reason) {
        HabitEventRecordTimeRangeIncorrectReason.BiggestThenCurrentTime -> "Дата и время не могут быть больше текущего времени."
    }

    override fun timeRangeTitle() = "Временной диапазон"

    override fun dailyEventCountDescription() = "Укажите сколько примерно было событий привычки каждый день"
    override fun dailyEventCountLabel() = "Число событий в день"
    override fun timeRangeDescription() = "Укажите когда произошло событие"
}

class EnglishHabitEventRecordEditingStrings : HabitEventRecordEditingStrings {
    override fun titleText(habitName: String) = "Editing a record — $habitName"
    override fun commentDescription() = "You can write a comment, but you don't have to."
    override fun commentTitle() = "Comment"
    override fun finishDescription() = "You can always change or delete this record."
    override fun finishButton() = "Save changes"
    override fun deleteConfirmation() = "Are you sure you want to delete this record?"
    override fun deleteDescription() = "You can delete this record."
    override fun deleteButton() = "Delete this record"
    override fun yes() = "Yes"
    override fun cancel() = "Cancel"
    override fun dailyEventCountError(reason: HabitEventRecordDailyEventCountIncorrectReason) = when (reason) {
        HabitEventRecordDailyEventCountIncorrectReason.Empty -> {
            "Cant be empty"
        }
    }
    override fun timeRangeError(reason: HabitEventRecordTimeRangeIncorrectReason) = when (reason) {
        HabitEventRecordTimeRangeIncorrectReason.BiggestThenCurrentTime -> "The date and time cannot be greater than the current time."
    }
    override fun timeRangeTitle() = "Time range"
    override fun dailyEventCountDescription() = "Indicate approximately how many habit events there were each day"
    override fun dailyEventCountLabel() = "Number of events per day"
    override fun timeRangeDescription() = "Indicate when the event occurred"
}