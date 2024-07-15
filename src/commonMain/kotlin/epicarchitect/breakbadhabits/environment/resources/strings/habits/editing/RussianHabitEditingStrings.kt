package epicarchitect.breakbadhabits.environment.resources.strings.habits.editing

import epicarchitect.breakbadhabits.operation.habits.validation.HabitNewNameError

class RussianHabitEditingStrings : HabitEditingStrings {
    override fun titleText() = "Редактирование привычки"
    override fun habitNameDescription() = "Введите название привычки, например курение."
    override fun habitNameTitle() = "Название привычки"
    override fun habitIconDescription() = "Выберите подходящую иконку для привычки."
    override fun finishButtonText() = "Сохранить изменения"
    override fun habitNameError(error: HabitNewNameError) = when (error) {
        HabitNewNameError.AlreadyUsed -> "Это название уже используется."
        HabitNewNameError.Empty -> "Название не может быть пустым."
        is HabitNewNameError.TooLong -> {
            "Название не может быть длиннее чем ${error.maxLength} символов."
        }
    }

    override fun deleteConfirmation() = "Вы уверены, что хотите удалить эту привычку?"
    override fun cancel() = "Отмена"
    override fun yes() = "Да"
    override fun deleteDescription() = "Вы можете удалить эту привычку."
    override fun deleteButton() = "Удалить эту привычку"
}