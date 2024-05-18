package epicarchitect.breakbadhabits.data.resources.strings

interface HabitWidgetEditingStrings {
    fun title(): String
    fun nameTitle(): String
    fun habitsDescription(): String
    fun nameDescription(): String
    fun deleteConfirmation(): String
    fun yes(): String
    fun cancel(): String
    fun deleteDescription(): String
    fun deleteButtonText(): String
    fun finishButton(): String
}


class RussianHabitWidgetEditingStrings : HabitWidgetEditingStrings {
    override fun title() = "Редактирование виджета"
    override fun habitsDescription() = "Выберите привычки для виджета."
    override fun nameDescription() = "Введите название виджета."
    override fun nameTitle() = "Название виджета"
    override fun deleteConfirmation() =
        "Вы уверены что хотите удалить этот виджет? Вам придется удалить виджет с главного экрана вручную."

    override fun yes() = "Да"
    override fun cancel() = "Отмена"
    override fun deleteDescription() = "Вы можете удалить этот виджет."
    override fun deleteButtonText() = "Удалить этот виджет"
    override fun finishButton() = "Сохранить изменения"
}

class EnglishHabitWidgetEditingStrings : HabitWidgetEditingStrings {
    override fun title() = "Editing a widget"
    override fun habitsDescription() = "Choose habits for the widget."
    override fun nameDescription() = "Enter a name for the widget."
    override fun nameTitle() = "Widget name"
    override fun deleteConfirmation() =
        "Are you sure you want to delete this widget? You will have to manually remove the widget from the home screen."

    override fun yes() = "Yes"
    override fun cancel() = "Cancel"
    override fun deleteDescription() = "You can delete this widget."
    override fun deleteButtonText() = "Delete this widget"
    override fun finishButton() = "Save changes"
}