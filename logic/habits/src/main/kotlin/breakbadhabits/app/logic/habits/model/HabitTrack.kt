package breakbadhabits.app.logic.habits.model

import kotlinx.datetime.Instant

data class HabitTrack(
    val id: Int,
    val habitId: Int,
    val instantRange: ClosedRange<Instant>,
    val eventCount: Int,
    val comment: String
)