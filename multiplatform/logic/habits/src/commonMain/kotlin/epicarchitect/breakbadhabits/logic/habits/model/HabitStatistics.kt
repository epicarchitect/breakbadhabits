package epicarchitect.breakbadhabits.logic.habits.model

import kotlin.time.Duration

data class HabitStatistics(
    val habitId: Int,
    val abstinence: Abstinence,
    val eventCount: EventCount
) {
    data class Abstinence(
        val averageDuration: Duration,
        val maxDuration: Duration,
        val minDuration: Duration,
        val durationSinceFirstTrack: Duration
    )

    data class EventCount(
        val currentMonthCount: Int,
        val previousMonthCount: Int,
        val totalCount: Int
    )
}
