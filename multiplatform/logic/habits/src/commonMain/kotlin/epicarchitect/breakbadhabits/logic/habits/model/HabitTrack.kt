package epicarchitect.breakbadhabits.logic.habits.model

import kotlinx.datetime.LocalDateTime

data class HabitTrack(
    val id: Int,
    val habitId: Int,
    val dateTimeRange: ClosedRange<LocalDateTime>,
    val eventCount: Int,
    val comment: String
)