package epicarchitect.breakbadhabits.entity.habits

class CachedHabitEventAmountStatistics(
    private val base: HabitEventAmountStatistics
) : HabitEventAmountStatistics {
    private val currentMonthCount by lazy(base::totalCount)
    private val previousMonthCount by lazy(base::previousMonthCount)
    private val totalCount by lazy(base::totalCount)
    override fun currentMonthCount() = currentMonthCount
    override fun previousMonthCount() = previousMonthCount
    override fun totalCount() = totalCount
}