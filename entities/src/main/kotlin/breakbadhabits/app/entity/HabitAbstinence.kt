package breakbadhabits.app.entity

import kotlinx.datetime.Instant

data class HabitAbstinence(
    val habitId: Habit.Id,
    val range: Range
) {
    data class Range(val value: ClosedRange<Instant>)
}