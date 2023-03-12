package breakbadhabits.app.entity

import kotlinx.datetime.Instant

data class HabitTrack(
    val id: Id,
    val habitId: Habit.Id,
    val range: Range,
    val eventCount: EventCount,
    val comment: Comment?
) {
    data class Id(val value: Long)

    data class Range(val value: ClosedRange<Instant>)

    data class EventCount(val dailyCount: Int)

    data class Comment(val value: String)
}