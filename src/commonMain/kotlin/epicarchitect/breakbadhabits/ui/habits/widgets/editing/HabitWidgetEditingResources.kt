package epicarchitect.breakbadhabits.ui.habits.widgets.editing

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.text.intl.Locale

val LocalHabitWidgetEditingResources = compositionLocalOf {
    if (Locale.current.language == "ru") {
        RussianHabitWidgetEditingResources()
    } else {
        EnglishHabitWidgetEditingResources()
    }
}

interface HabitWidgetEditingResources {
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


class RussianHabitWidgetEditingResources : HabitWidgetEditingResources {
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

class EnglishHabitWidgetEditingResources : HabitWidgetEditingResources {
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