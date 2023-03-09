package breakbadhabits.app.entity

import kotlinx.datetime.LocalDateTime

data class HabitTrack(
    val id: Id,
    val habitId: Habit.Id,
    val range: Range,
    val eventCount: EventCount,
    val comment: Comment?
) {
    data class Id(val value: Long)

    data class Range(val value: ClosedRange<LocalDateTime>)

    data class EventCount(
        val value: Int,
        val timeUnit: TimeUnit
    ) {
        enum class TimeUnit {
            MINUTES,
            HOURS,
            DAYS,
        }
    }

    data class Comment(val value: String)
}