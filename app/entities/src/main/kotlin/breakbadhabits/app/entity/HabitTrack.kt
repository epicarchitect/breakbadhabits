package breakbadhabits.app.entity

import kotlinx.datetime.LocalDateTime
import java.time.format.DateTimeFormatter

data class HabitTrack(
    val id: Id,
    val habitId: Habit.Id,
    val range: Range,
    val dailyCount: DailyCount,
    val comment: Comment?
) {
    data class Id(val value: Long)

    data class Range(val value: ClosedRange<LocalDateTime>)

    data class DailyCount(val value: Double)

    data class Comment(val value: String)
}