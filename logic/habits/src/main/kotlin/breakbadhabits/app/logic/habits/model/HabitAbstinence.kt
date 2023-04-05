package breakbadhabits.app.logic.habits.model

import kotlinx.datetime.Instant

data class HabitAbstinence(
    val habitId: Int,
    val instantRange: ClosedRange<Instant>
)