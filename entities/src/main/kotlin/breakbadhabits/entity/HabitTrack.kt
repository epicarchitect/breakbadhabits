package breakbadhabits.entity

import kotlinx.datetime.LocalDateTime

data class HabitTrack(
    val id: Id,
    val habitId: Habit.Id,
    val range: Range,
    val dailyCount: DailyCount,
    val comment: Comment?
) {
    data class Id(val value: Int)
    data class Range(val value: ClosedRange<LocalDateTime>)
    data class DailyCount(val value: Double)
    data class Comment(val value: String)
}