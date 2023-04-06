package breakbadhabits.app.logic.habits.model

import breakbadhabits.foundation.datetime.InstantRange

data class HabitAbstinence(
    val habitId: Int,
    val instantRange: InstantRange
)