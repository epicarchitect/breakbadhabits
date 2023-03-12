package breakbadhabits.app.entity

import kotlin.time.Duration

data class HabitStatistics(
    val habitId: Habit.Id,
    val abstinence: Abstinence,
    val eventCount: EventCount
) {
    data class Abstinence(
        val averageTime: Duration,
        val maxTime: Duration,
        val minTime: Duration,
        val timeSinceFirstTrack: Duration
    )

    data class EventCount(
        val currentMonthCount: Int,
        val previousMonthCount: Int,
        val totalCount: Int
    )
}