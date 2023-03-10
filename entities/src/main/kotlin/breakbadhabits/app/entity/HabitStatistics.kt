package breakbadhabits.app.entity

data class HabitStatistics(
    val habitId: Habit.Id,
    val abstinence: Abstinence?,
    val eventCount: EventCount
) {
    data class Abstinence(
        val averageTime: Long,
        val maxTime: Long,
        val minTime: Long,
        val timeSinceFirstTrack: Long
    )

    data class EventCount(
        val currentMonthCount: Long,
        val previousMonthCount: Long,
        val totalCount: Long
    )
}