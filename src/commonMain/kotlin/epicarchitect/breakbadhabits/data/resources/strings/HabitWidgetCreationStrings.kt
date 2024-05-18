package epicarchitect.breakbadhabits.data.resources.strings

interface HabitWidgetCreationStrings {
    fun title(): String
    fun nameTitle(): String
    fun habitsDescription(): String
    fun nameDescription(): String
    fun finishButton(): String
}

class RussianHabitWidgetCreationStrings : HabitWidgetCreationStrings {
    override fun title() = "Создание виджета"
    override fun habitsDescription() = "Выберите привычки для виджета."
    override fun nameDescription() = "Введите название виджета."
    override fun nameTitle() = "Название виджета"
    override fun finishButton() = "Готово"
}

class EnglishHabitWidgetCreationStrings : HabitWidgetCreationStrings {
    override fun title() = "Create a widget"
    override fun habitsDescription() = "Choose habits for the widget."
    override fun nameDescription() = "Enter a name for the widget."
    override fun nameTitle() = "Widget name"
    override fun finishButton() = "Done"
}