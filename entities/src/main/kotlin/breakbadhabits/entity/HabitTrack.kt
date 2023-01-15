package breakbadhabits.entity

import kotlinx.datetime.LocalDateTime

data class HabitTrack(
    val id: Id,
    val habitId: Habit.Id,
    val range: Range,
    val dailyCount: DailyCount,
    val comment: Comment?
) {
    @JvmInline
    value class Id(val value: Long)

    @JvmInline
    value class Range(val value: ClosedRange<LocalDateTime>)

    @JvmInline
    value class DailyCount(val value: Double)

    @JvmInline
    value class Comment(val value: String)
}