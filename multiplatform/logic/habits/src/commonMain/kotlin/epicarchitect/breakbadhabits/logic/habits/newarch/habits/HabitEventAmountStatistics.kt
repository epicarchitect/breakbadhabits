package epicarchitect.breakbadhabits.logic.habits.newarch.habits

interface HabitEventAmountStatistics {
    fun currentMonthCount(): Int
    fun previousMonthCount(): Int
    fun totalCount(): Int
}