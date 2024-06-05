package epicarchitect.breakbadhabits.data.resources.strings


interface RangeSelectionCalendarDialogStrings {
    fun start(): String
    fun end(): String
    fun cancel(): String
    fun apply(): String
}

class RussianRangeSelectionCalendarDialogStrings : RangeSelectionCalendarDialogStrings {
    override fun start() = "Начало"
    override fun end() = "Конец"
    override fun cancel() = "Отмена"
    override fun apply() = "Применить"
}

class EnglishRangeSelectionCalendarDialogStrings : RangeSelectionCalendarDialogStrings {
    override fun start() = "Start"
    override fun end() = "End"
    override fun cancel() = "Cancel"
    override fun apply() = "Apply"
}