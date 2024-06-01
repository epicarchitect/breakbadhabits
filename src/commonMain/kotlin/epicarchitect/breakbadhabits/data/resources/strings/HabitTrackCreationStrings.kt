package epicarchitect.breakbadhabits.data.resources.strings

import epicarchitect.breakbadhabits.operation.habits.HabitTrackEventCountIncorrectReason

interface HabitTrackCreationStrings {
    fun titleText(): String
    fun commentDescription(): String
    fun commentLabel(): String
    fun finishDescription(): String
    fun finishButton(): String
    fun habitNameLabel(habitName: String): String
    fun trackEventCountError(reason: HabitTrackEventCountIncorrectReason): String
}

class RussianHabitTrackCreationStrings : HabitTrackCreationStrings {
    override fun titleText() = "Новое событие"
    override fun commentDescription() = "Вы можете написать комментарий, но это не обязательно."
    override fun commentLabel() = "Комментарий"
    override fun finishDescription() = "Вы всегда сможете изменить или удалить это событие."
    override fun finishButton() = "Записать событие"
    override fun habitNameLabel(habitName: String) = "Привычка: $habitName"
    override fun trackEventCountError(reason: HabitTrackEventCountIncorrectReason) = when (reason) {
        HabitTrackEventCountIncorrectReason.Empty -> {
            "Поле не может быть пустым"
        }
    }
}

class EnglishHabitTrackCreationStrings : HabitTrackCreationStrings {
    override fun titleText() = "New event"
    override fun commentDescription() = "You can write a comment, but you don't have to."
    override fun commentLabel() = "Comment"
    override fun finishDescription() = "You can always change or delete this event."
    override fun finishButton() = "Save event"
    override fun habitNameLabel(habitName: String) = "Habit: $habitName"
    override fun trackEventCountError(reason: HabitTrackEventCountIncorrectReason) = when (reason) {
        HabitTrackEventCountIncorrectReason.Empty -> {
            "Cant be empty"
        }
    }
}