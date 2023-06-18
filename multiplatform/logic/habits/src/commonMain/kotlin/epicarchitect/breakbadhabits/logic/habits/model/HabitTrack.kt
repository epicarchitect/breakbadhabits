package epicarchitect.breakbadhabits.logic.habits.model

import epicarchitect.breakbadhabits.foundation.datetime.ZonedDateTimeRange

data class HabitTrack(
    val id: Int,
    val habitId: Int,
    val dateTimeRange: ZonedDateTimeRange,
    val eventCount: Int,
    val comment: String
)
