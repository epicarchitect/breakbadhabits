package epicarchitect.breakbadhabits.resources.strings.habits.records.dashboard

class RussianHabitEventRecordsDashboardStrings : HabitEventRecordsDashboardStrings {
    override fun newTrackButton() = "Добавить запись"
    override fun dailyEventCount(count: Int) = "Ежедневное количество событий: $count"
    override fun eventCount(count: Int) = "Всего событий: $count"
}