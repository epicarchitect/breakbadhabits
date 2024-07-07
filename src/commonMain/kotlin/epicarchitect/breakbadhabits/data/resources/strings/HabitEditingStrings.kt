package epicarchitect.breakbadhabits.data.resources.strings

import epicarchitect.breakbadhabits.operation.habits.validation.HabitNewNameError

interface HabitEditingStrings {
    fun titleText(): String
    fun habitNameDescription(): String
    fun habitNameTitle(): String
    fun habitIconDescription(): String
    fun finishButtonText(): String
    fun habitNameError(error: HabitNewNameError): String
    fun deleteConfirmation(): String
    fun cancel(): String
    fun yes(): String
    fun deleteDescription(): String
    fun deleteButton(): String
}

class RussianHabitEditingStrings : HabitEditingStrings {
    override fun titleText() = "Редактирование привычки"
    override fun habitNameDescription() = "Введите название привычки, например курение."
    override fun habitNameTitle() = "Название привычки"
    override fun habitIconDescription() = "Выберите подходящую иконку для привычки."
    override fun finishButtonText() = "Сохранить изменения"
    override fun habitNameError(error: HabitNewNameError) = when (error) {
        HabitNewNameError.AlreadyUsed -> "Это название уже используется."
        HabitNewNameError.Empty       -> "Название не может быть пустым."
        is HabitNewNameError.TooLong  -> {
            "Название не может быть длиннее чем ${error.maxLength} символов."
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
    override fun habitNameTitle() = "Habit name"
    override fun habitIconDescription() = "Choose the appropriate icon for the habit."
    override fun finishButtonText() = "Save changes"
    override fun habitNameError(error: HabitNewNameError) = when (error) {
        HabitNewNameError.AlreadyUsed -> "This name has already been used."
        HabitNewNameError.Empty       -> "The title cannot be empty."
        is HabitNewNameError.TooLong  -> {
            "The name cannot be longer than ${error.maxLength} characters."
        }
    }

    override fun deleteConfirmation() = "Are you sure you want to remove this habit?"
    override fun cancel() = "Cancel"
    override fun yes() = "Yes"
    override fun deleteDescription() = "You can delete this habit."
    override fun deleteButton() = "Delete this habit"
}