package breakbadhabits.app.logic.habits.entity

import kotlinx.datetime.Instant

data class HabitAbstinence(
    val habitId: Habit.Id,
    val range: Range
) {
    class Range(value: ClosedRange<Instant>) : ClosedRange<Instant> by value
}