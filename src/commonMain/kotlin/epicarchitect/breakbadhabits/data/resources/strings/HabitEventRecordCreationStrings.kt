package epicarchitect.breakbadhabits.data.resources.strings

import epicarchitect.breakbadhabits.operation.habits.validation.DailyHabitEventCountError
import epicarchitect.breakbadhabits.operation.habits.validation.HabitEventRecordTimeRangeError

interface HabitEventRecordCreationStrings {
    fun titleText(habitName: String): String
    fun commentTitle(): String
    fun commentDescription(): String
    fun finishDescription(): String
    fun finishButton(): String
    fun dailyEventCountTitle(): String
    fun dailyEventCountDescription(): String
    fun dailyEventCountError(error: DailyHabitEventCountError): String
    fun timeRangeTitle(): String
    fun timeRangeDescription(): String
    fun timeRangeError(error: HabitEventRecordTimeRangeError): String
    fun now(): String
    fun yesterday(): String
    fun yourTimeRange(): String
}

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
    override fun dailyEventCountDescription() = "Укажите сколько примерно было событий привычки в день"
    override fun dailyEventCountTitle() = "Частота"
    override fun timeRangeDescription() = "Укажите когда произошло событие"
    override fun now() = "Сейчас"
    override fun yesterday() = "Вчера"
    override fun yourTimeRange() = "Свой интервал"
}

class EnglishHabitEventRecordCreationStrings : HabitEventRecordCreationStrings {
    override fun titleText(habitName: String) = "New record — $habitName"
    override fun commentDescription() = "You can write a comment, but you don't have to."
    override fun commentTitle() = "Comment"
    override fun finishDescription() = "You can always change or delete this record."
    override fun finishButton() = "Done"
    override fun dailyEventCountError(error: DailyHabitEventCountError) = when (error) {
        DailyHabitEventCountError.Empty -> {
            "Cant be empty"
        }
    }
    override fun timeRangeTitle() = "Time range"
    override fun timeRangeError(error: HabitEventRecordTimeRangeError) = when (error) {
        HabitEventRecordTimeRangeError.BiggestThenCurrentTime -> "The date and time cannot be greater than the current time."
    }
    override fun dailyEventCountDescription() = "Indicate approximately how many habit events per day"
    override fun dailyEventCountTitle() = "Frequency"
    override fun timeRangeDescription() = "Indicate when the event occurred"
    override fun now() = "Now"
    override fun yesterday() = "Yesterday"
    override fun yourTimeRange() = "Your interval"
}