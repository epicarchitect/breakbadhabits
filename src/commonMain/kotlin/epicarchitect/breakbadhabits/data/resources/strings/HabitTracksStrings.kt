package epicarchitect.breakbadhabits.data.resources.strings

interface HabitTracksStrings {
    fun newTrackButton(): String
    fun dailyEventCount(count: Int): String
    fun eventCount(count: Int): String
}

class RussianHabitTracksStrings : HabitTracksStrings {
    override fun newTrackButton() = "Добавить события"
    override fun dailyEventCount(count: Int) = "Ежедневное количество событий: $count"
    override fun eventCount(count: Int) = "Всего событий: $count"
}

class EnglishHabitTracksStrings : HabitTracksStrings {
    override fun newTrackButton() = "Add events"
    override fun dailyEventCount(count: Int) = "Daily event count: $count"
    override fun eventCount(count: Int) = "Total events: $count"
}