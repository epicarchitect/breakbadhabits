package epicarchitect.breakbadhabits.defaultDependencies.habits.editing

import androidx.compose.ui.text.intl.Locale
import epicarchitect.breakbadhabits.features.habits.editing.HabitEditingResources
import epicarchitect.breakbadhabits.validator.IncorrectHabitNewName

class RussianHabitEditingResources : HabitEditingResources {
    override fun titleText() = "Новая привычка"
    override fun habitNameDescription() = "Введите название привычки, например курение."
    override fun habitNameLabel() = "Название привычки"
    override fun habitIconDescription() = "Выберите подходящую иконку для привычки."
    override fun finishButtonText() = "Сохранить изменения"
    override fun habitNameValidationError(reason: IncorrectHabitNewName.Reason) = when (reason) {
        IncorrectHabitNewName.Reason.AlreadyUsed -> "Это название уже используется."
        IncorrectHabitNewName.Reason.Empty -> "Название не может быть пустым."
        is IncorrectHabitNewName.Reason.TooLong -> {
            "Название не может быть длиннее чем ${reason.maxLength} символов."
        }
    }
    override fun deleteConfirmation() = "Вы уверены, что хотите удалить эту привычку?"
    override fun cancel() = "Отмена"
    override fun yes() = "Да"
    override fun deleteDescription() = "Вы можете удалить эту привычку."
    override fun deleteButton() = "Удалить эту привычку"
}

class EnglishHabitEditingResources : HabitEditingResources {
    override fun titleText() = "Editing a habit"
    override fun habitNameDescription() = "Enter a name for the habit, such as smoking."
    override fun habitNameLabel() = "Habit name"
    override fun habitIconDescription() = "Choose the appropriate icon for the habit."
    override fun finishButtonText() = "Save changes"
    override fun habitNameValidationError(reason: IncorrectHabitNewName.Reason) = when (reason) {
        IncorrectHabitNewName.Reason.AlreadyUsed -> "This name has already been used."
        IncorrectHabitNewName.Reason.Empty -> "The title cannot be empty."
        is IncorrectHabitNewName.Reason.TooLong -> {
            "The name cannot be longer than ${reason.maxLength} characters."
        }
    }
    override fun deleteConfirmation() = "Are you sure you want to remove this habit?"
    override fun cancel() = "Cancel"
    override fun yes() = "Yes"
    override fun deleteDescription() = "You can delete this habit."
    override fun deleteButton() = "Delete this habit"
}

class LocalizedHabitEditingResources(locale: Locale) : HabitEditingResources by resolve(locale)

private fun resolve(locale: Locale) = if (locale.language == "ru") {
    RussianHabitEditingResources()
} else {
    EnglishHabitEditingResources()
}