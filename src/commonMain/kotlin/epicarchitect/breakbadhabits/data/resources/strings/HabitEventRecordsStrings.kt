package epicarchitect.breakbadhabits.data.resources.strings

interface HabitEventRecordsStrings {
    fun newTrackButton(): String
    fun dailyEventCount(count: Int): String
    fun eventCount(count: Int): String
}

class RussianHabitEventRecordsStrings : HabitEventRecordsStrings {
    override fun newTrackButton() = "Добавить запись"
    override fun dailyEventCount(count: Int) = "Ежедневное количество событий: $count"
    override fun eventCount(count: Int) = "Всего событий: $count"
}

class EnglishHabitEventRecordsStrings : HabitEventRecordsStrings {
    override fun newTrackButton() = "Add record"
    override fun dailyEventCount(count: Int) = "Daily event count: $count"
    override fun eventCount(count: Int) = "Total events: $count"
}