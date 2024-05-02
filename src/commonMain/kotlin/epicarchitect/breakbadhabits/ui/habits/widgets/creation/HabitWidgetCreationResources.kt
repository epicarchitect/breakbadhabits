package epicarchitect.breakbadhabits.ui.habits.widgets.creation

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.text.intl.Locale

val LocalHabitWidgetCreationResources = compositionLocalOf {
    if (Locale.current.language == "ru") {
        RussianHabitWidgetCreationResources()
    } else {
        EnglishHabitWidgetCreationResources()
    }
}

interface HabitWidgetCreationResources {
    fun title(): String
    fun nameTitle(): String
    fun habitsDescription(): String
    fun nameDescription(): String
    fun finishButton(): String
}

class RussianHabitWidgetCreationResources : HabitWidgetCreationResources {
    override fun title() = "Создание виджета"
    override fun habitsDescription() = "Выберите привычки для виджета."
    override fun nameDescription() = "Введите название виджета."
    override fun nameTitle() = "Название виджета"
    override fun finishButton() = "Готово"
}

class EnglishHabitWidgetCreationResources : HabitWidgetCreationResources {
    override fun title() = "Create a widget"
    override fun habitsDescription() = "Choose habits for the widget."
    override fun nameDescription() = "Enter a name for the widget."
    override fun nameTitle() = "Widget name"
    override fun finishButton() = "Done"
}