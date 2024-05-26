package epicarchitect.breakbadhabits.data.resources.strings

import epicarchitect.breakbadhabits.entity.validator.HabitNewNameValidation

interface HabitEditingStrings {
    fun titleText(): String
    fun habitNameDescription(): String
    fun habitNameLabel(): String
    fun habitIconDescription(): String
    fun finishButtonText(): String
    fun habitNameValidationError(reason: HabitNewNameValidation.IncorrectReason): String
    fun deleteConfirmation(): String
    fun cancel(): String
    fun yes(): String
    fun deleteDescription(): String
    fun deleteButton(): String
}

class RussianHabitEditingStrings : HabitEditingStrings {
    override fun titleText() = "Новая привычка"
    override fun habitNameDescription() = "Введите название привычки, например курение."
    override fun habitNameLabel() = "Название привычки"
    override fun habitIconDescription() = "Выберите подходящую иконку для привычки."
    override fun finishButtonText() = "Сохранить изменения"
    override fun habitNameValidationError(reason: HabitNewNameValidation.IncorrectReason) = when (reason) {
        HabitNewNameValidation.IncorrectReason.AlreadyUsed -> "Это название уже используется."
        HabitNewNameValidation.IncorrectReason.Empty       -> "Название не может быть пустым."
        is HabitNewNameValidation.IncorrectReason.TooLong  -> {
            "Название не может быть длиннее чем ${reason.maxLength} символов."
        }
    }

    override fun deleteConfirmation() = "Вы уверены, что хотите удалить эту привычку?"
    override fun cancel() = "Отмена"
    override fun yes() = "Да"
    override fun deleteDescription() = "Вы можете удалить эту привычку."
    override fun deleteButton() = "Удалить эту привычку"
}

class EnglishHabitEditingStrings : HabitEditingStrings {
    override fun titleText() = "Editing a habit"
    override fun habitNameDescription() = "Enter a name for the habit, such as smoking."
    override fun habitNameLabel() = "Habit name"
    override fun habitIconDescription() = "Choose the appropriate icon for the habit."
    override fun finishButtonText() = "Save changes"
    override fun habitNameValidationError(reason: HabitNewNameValidation.IncorrectReason) = when (reason) {
        HabitNewNameValidation.IncorrectReason.AlreadyUsed -> "This name has already been used."
        HabitNewNameValidation.IncorrectReason.Empty       -> "The title cannot be empty."
        is HabitNewNameValidation.IncorrectReason.TooLong  -> {
            "The name cannot be longer than ${reason.maxLength} characters."
        }
    }

    override fun deleteConfirmation() = "Are you sure you want to remove this habit?"
    override fun cancel() = "Cancel"
    override fun yes() = "Yes"
    override fun deleteDescription() = "You can delete this habit."
    override fun deleteButton() = "Delete this habit"
}