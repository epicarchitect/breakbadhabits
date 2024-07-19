package epicarchitect.breakbadhabits.resources.strings.habits.records.dashboard

class RussianHabitEventRecordsDashboardStrings : HabitEventRecordsDashboardStrings {
    override fun newTrackButton() = "Добавить запись"
    override fun dailyEventCount(count: Int) = "Число событий в день: $count"
    override fun eventCount(count: Int) = "Всего событий: $count"
}