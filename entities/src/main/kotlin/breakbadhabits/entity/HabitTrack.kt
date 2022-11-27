package breakbadhabits.entity

import breakbadhabits.extension.datetime.LocalDateTimeInterval

data class HabitTrack(
    val id: Id,
    val habitId: Habit.Id,
    val interval: Interval,
    val dailyCount: DailyCount,
    val comment: Comment?
) {
    data class Id(val value: Int)
    data class Interval(val value: LocalDateTimeInterval)
    data class DailyCount(val value: Double)
    data class Comment(val value: String)
}