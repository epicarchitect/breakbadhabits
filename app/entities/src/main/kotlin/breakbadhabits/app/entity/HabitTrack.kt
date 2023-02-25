package breakbadhabits.app.entity

import kotlinx.datetime.LocalDateTime

data class HabitTrack(
    val id: Id,
    val habitId: Habit.Id,
    val range: Range,
    val value: Value,
    val comment: Comment?
) {
    data class Id(val value: Long)

    data class Range(val value: ClosedRange<LocalDateTime>)

    data class Value(val minutelyValue: Double)

    data class Comment(val value: String)
}