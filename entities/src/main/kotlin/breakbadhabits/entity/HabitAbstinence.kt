package breakbadhabits.entity

import kotlinx.datetime.LocalDateTime

data class HabitAbstinence(
    val habitId: Habit.Id,
    val range: Range
) {
    @JvmInline
    value class Range(val value: ClosedRange<LocalDateTime>)
}