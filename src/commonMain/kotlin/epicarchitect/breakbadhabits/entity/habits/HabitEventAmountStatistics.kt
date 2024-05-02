package epicarchitect.breakbadhabits.entity.habits

interface HabitEventAmountStatistics {
    fun currentMonthCount(): Int
    fun previousMonthCount(): Int
    fun totalCount(): Int
}