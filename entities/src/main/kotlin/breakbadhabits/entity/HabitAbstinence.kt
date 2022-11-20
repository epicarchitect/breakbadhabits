package breakbadhabits.entity

import breakbadhabits.extension.datetime.LocalDateTimeInterval

data class HabitAbstinence(
    val habitId: Habit.Id,
    val interval: Interval
) {
    data class Interval(val value: LocalDateTimeInterval)
}