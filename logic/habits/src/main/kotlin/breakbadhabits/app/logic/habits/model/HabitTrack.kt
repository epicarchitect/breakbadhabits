package breakbadhabits.app.logic.habits.model

import breakbadhabits.foundation.datetime.InstantRange

data class HabitTrack(
    val id: Int,
    val habitId: Int,
    val instantRange: InstantRange,
    val eventCount: Int,
    val comment: String
)