package epicarchitect.breakbadhabits.defaultDependencies.habits.widgets.creation

import androidx.compose.ui.text.intl.Locale
import epicarchitect.breakbadhabits.features.habits.widgets.creation.HabitWidgetCreationResources

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

class LocalizedHabitWidgetCreationResources(locale: Locale) : HabitWidgetCreationResources by (
    if (locale.language == "ru") {
        RussianHabitWidgetCreationResources()
    } else {
        EnglishHabitWidgetCreationResources()
    }
    )