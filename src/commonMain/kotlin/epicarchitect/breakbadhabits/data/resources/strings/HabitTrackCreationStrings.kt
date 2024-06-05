package epicarchitect.breakbadhabits.data.resources.strings

import epicarchitect.breakbadhabits.operation.habits.validation.HabitTrackEventCountIncorrectReason
import epicarchitect.breakbadhabits.operation.habits.validation.HabitTrackTimeRangeIncorrectReason

interface HabitTrackCreationStrings {
    fun titleText(habitName: String): String
    fun commentDescription(): String
    fun commentLabel(): String
    fun finishDescription(): String
    fun finishButton(): String
    fun habitNameLabel(habitName: String): String
    fun trackEventCountError(reason: HabitTrackEventCountIncorrectReason): String
    fun trackTimeRangeError(reason: HabitTrackTimeRangeIncorrectReason): String
    fun trackEventCountDescription(): String
    fun trackEventCountLabel(): String
    fun trackTimeDescription(): String
    fun now(): String
    fun yesterday(): String
    fun yourInterval(): String
}

class RussianHabitTrackCreationStrings : HabitTrackCreationStrings {
    override fun titleText(habitName: String) = "Новые события ($habitName)"
    override fun commentDescription() = "Вы можете написать комментарий, но это не обязательно."
    override fun commentLabel() = "Комментарий"
    override fun finishDescription() = "Вы всегда сможете изменить или удалить это событие."
    override fun finishButton() = "Готово"
    override fun habitNameLabel(habitName: String) = "Привычка: $habitName"
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
    override fun now() = "Сейчас"
    override fun yesterday() = "Вчера"
    override fun yourInterval() = "Свой интервал"
}

class EnglishHabitTrackCreationStrings : HabitTrackCreationStrings {
    override fun titleText(habitName: String) = "New events ($habitName)"
    override fun commentDescription() = "You can write a comment, but you don't have to."
    override fun commentLabel() = "Comment"
    override fun finishDescription() = "You can always change or delete this event."
    override fun finishButton() = "Done"
    override fun habitNameLabel(habitName: String) = "Habit: $habitName"
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
    override fun now() = "Now"
    override fun yesterday() = "Yesterday"
    override fun yourInterval() = "Your interval"
}