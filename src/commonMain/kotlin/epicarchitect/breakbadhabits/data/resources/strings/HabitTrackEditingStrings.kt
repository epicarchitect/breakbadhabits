package epicarchitect.breakbadhabits.data.resources.strings

import epicarchitect.breakbadhabits.operation.habits.validation.HabitTrackEventCountIncorrectReason
import epicarchitect.breakbadhabits.operation.habits.validation.HabitTrackTimeRangeIncorrectReason

interface HabitTrackEditingStrings {
    fun trackEventCountError(reason: HabitTrackEventCountIncorrectReason): String
    fun titleText(habitName: String): String
    fun commentDescription(): String
    fun commentLabel(): String
    fun finishDescription(): String
    fun finishButton(): String
    fun deleteConfirmation(): String
    fun yes(): String
    fun cancel(): String
    fun deleteDescription(): String
    fun deleteButton(): String
    fun trackEventCountDescription(): String
    fun trackEventCountLabel(): String
    fun trackTimeDescription(): String
    fun trackTimeRangeError(reason: HabitTrackTimeRangeIncorrectReason): String
}

class RussianHabitTrackEditingStrings : HabitTrackEditingStrings {
    override fun titleText(habitName: String) = "Редактирование событий (${habitName})"
    override fun commentDescription() = "Вы можете написать комментарий, но это не обязательно."
    override fun commentLabel() = "Комментарий"
    override fun finishDescription() = "Вы всегда сможете изменить или удалить это событие."
    override fun finishButton() = "Сохранить изменения"
    override fun deleteConfirmation() = "Вы уверены, что хотите удалить это событие?"
    override fun yes() = "Да"
    override fun cancel() = "Отмена"
    override fun deleteDescription() = "Вы можете удалить эту запись."
    override fun deleteButton() = "Удалить события"
    override fun trackEventCountError(reason: HabitTrackEventCountIncorrectReason) = when (reason) {
        HabitTrackEventCountIncorrectReason.Empty -> {
            "Поле не может быть пустым"
        }
    }
    override fun trackTimeRangeError(reason: HabitTrackTimeRangeIncorrectReason) = when (reason) {
        HabitTrackTimeRangeIncorrectReason.BiggestThenCurrentTime -> "Дата и время не могут быть больше текущего времени."
    }
    override fun trackEventCountDescription() = "Укажите сколько примерно было событий привычки каждый день"
    override fun trackEventCountLabel() = "Число событий в день"
    override fun trackTimeDescription() = "Укажите когда произошло событие"
}

class EnglishHabitTrackEditingStrings : HabitTrackEditingStrings {
    override fun titleText(habitName: String) = "Editing an events ($habitName)"
    override fun commentDescription() = "You can write a comment, but you don't have to."
    override fun commentLabel() = "Comment"
    override fun finishDescription() = "You can always change or delete this event."
    override fun finishButton() = "Save changes"
    override fun deleteConfirmation() = "Are you sure you want to delete this event?"
    override fun deleteDescription() = "You can delete this events."
    override fun deleteButton() = "Delete this events"
    override fun yes() = "Yes"
    override fun cancel() = "Cancel"
    override fun trackEventCountError(reason: HabitTrackEventCountIncorrectReason) = when (reason) {
        HabitTrackEventCountIncorrectReason.Empty -> {
            "Cant be empty"
        }
    }
    override fun trackTimeRangeError(reason: HabitTrackTimeRangeIncorrectReason) = when (reason) {
        HabitTrackTimeRangeIncorrectReason.BiggestThenCurrentTime -> "The date and time cannot be greater than the current time."
    }
    override fun trackEventCountDescription() = "Indicate approximately how many habit events there were each day"
    override fun trackEventCountLabel() = "Number of events per day"
    override fun trackTimeDescription() = "Indicate when the event occurred"
}