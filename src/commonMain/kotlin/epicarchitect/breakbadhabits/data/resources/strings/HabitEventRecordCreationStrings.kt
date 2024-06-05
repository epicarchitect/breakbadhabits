package epicarchitect.breakbadhabits.data.resources.strings

import epicarchitect.breakbadhabits.operation.habits.validation.HabitEventRecordDailyEventCountIncorrectReason
import epicarchitect.breakbadhabits.operation.habits.validation.HabitEventRecordTimeRangeIncorrectReason

interface HabitEventRecordCreationStrings {
    fun titleText(habitName: String): String
    fun commentDescription(): String
    fun commentLabel(): String
    fun finishDescription(): String
    fun finishButton(): String
    fun dailyEventCountDescription(): String
    fun dailyEventCountLabel(): String
    fun dailyEventCountError(reason: HabitEventRecordDailyEventCountIncorrectReason): String
    fun timeRangeDescription(): String
    fun timeRangeError(reason: HabitEventRecordTimeRangeIncorrectReason): String
    fun now(): String
    fun yesterday(): String
    fun yourTimeRange(): String
}

class RussianHabitEventRecordCreationStrings : HabitEventRecordCreationStrings {
    override fun titleText(habitName: String) = "Новая запись ($habitName)"
    override fun commentDescription() = "Вы можете написать комментарий, но это не обязательно."
    override fun commentLabel() = "Комментарий"
    override fun finishDescription() = "Вы всегда сможете изменить или удалить эту запись."
    override fun finishButton() = "Готово"
    override fun dailyEventCountError(reason: HabitEventRecordDailyEventCountIncorrectReason) = when (reason) {
        HabitEventRecordDailyEventCountIncorrectReason.Empty -> {
            "Поле не может быть пустым"
        }
    }
    override fun timeRangeError(reason: HabitEventRecordTimeRangeIncorrectReason) = when (reason) {
        HabitEventRecordTimeRangeIncorrectReason.BiggestThenCurrentTime -> "Дата и время не могут быть больше текущего времени."
    }
    override fun dailyEventCountDescription() = "Укажите сколько примерно было событий привычки в день"
    override fun dailyEventCountLabel() = "Число событий в день"
    override fun timeRangeDescription() = "Укажите когда произошло событие"
    override fun now() = "Сейчас"
    override fun yesterday() = "Вчера"
    override fun yourTimeRange() = "Свой интервал"
}

class EnglishHabitEventRecordCreationStrings : HabitEventRecordCreationStrings {
    override fun titleText(habitName: String) = "New record ($habitName)"
    override fun commentDescription() = "You can write a comment, but you don't have to."
    override fun commentLabel() = "Comment"
    override fun finishDescription() = "You can always change or delete this record."
    override fun finishButton() = "Done"
    override fun dailyEventCountError(reason: HabitEventRecordDailyEventCountIncorrectReason) = when (reason) {
        HabitEventRecordDailyEventCountIncorrectReason.Empty -> {
            "Cant be empty"
        }
    }

    override fun timeRangeError(reason: HabitEventRecordTimeRangeIncorrectReason) = when (reason) {
        HabitEventRecordTimeRangeIncorrectReason.BiggestThenCurrentTime -> "The date and time cannot be greater than the current time."
    }

    override fun dailyEventCountDescription() = "Indicate approximately how many habit events per day"
    override fun dailyEventCountLabel() = "Number of events per day"
    override fun timeRangeDescription() = "Indicate when the event occurred"
    override fun now() = "Now"
    override fun yesterday() = "Yesterday"
    override fun yourTimeRange() = "Your interval"
}