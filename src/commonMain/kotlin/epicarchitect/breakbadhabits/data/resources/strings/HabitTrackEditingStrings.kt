package epicarchitect.breakbadhabits.data.resources.strings

import epicarchitect.breakbadhabits.entity.validator.HabitTrackEventCountInputValidation

interface HabitTrackEditingStrings {
    fun habitNameLabel(name: String): String
    fun trackEventCountError(reason: HabitTrackEventCountInputValidation.IncorrectReason): String
    fun titleText(): String
    fun commentDescription(): String
    fun commentLabel(): String
    fun finishDescription(): String
    fun finishButton(): String
}

class RussianHabitTrackEditingStrings : HabitTrackEditingStrings {
    override fun titleText() = "Новое событие"
    override fun commentDescription() = "Вы можете написать комментарий, но это не обязательно."
    override fun commentLabel() = "Комментарий"
    override fun finishDescription() = "Вы всегда сможете изменить или удалить это событие."
    override fun finishButton() = "Записать событие"
    override fun habitNameLabel(name: String) = "Привычка: $name"
    override fun trackEventCountError(reason: HabitTrackEventCountInputValidation.IncorrectReason) = when (reason) {
        HabitTrackEventCountInputValidation.IncorrectReason.Empty -> {
            "Поле не может быть пустым"
        }
    }
}

class EnglishHabitTrackEditingStrings : HabitTrackEditingStrings {
    override fun titleText() = "New event"
    override fun commentDescription() = "You can write a comment, but you don't have to."
    override fun commentLabel() = "Comment"
    override fun finishDescription() = "You can always change or delete this event."
    override fun finishButton() = "Save event"

    override fun habitNameLabel(name: String) = "Habit: $name"
    override fun trackEventCountError(reason: HabitTrackEventCountInputValidation.IncorrectReason) = when (reason) {
        HabitTrackEventCountInputValidation.IncorrectReason.Empty -> {
            "Cant be empty"
        }
    }
}