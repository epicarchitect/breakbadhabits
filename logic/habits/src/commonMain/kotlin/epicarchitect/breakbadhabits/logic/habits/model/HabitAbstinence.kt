package epicarchitect.breakbadhabits.logic.habits.model

import epicarchitect.breakbadhabits.foundation.datetime.ZonedDateTimeRange

data class HabitAbstinence(
    val habitId: Int,
    val dateTimeRange: ZonedDateTimeRange
)