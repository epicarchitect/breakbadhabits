package epicarchitect.breakbadhabits.logic.habits.model

import kotlinx.datetime.LocalDateTime
import kotlin.time.Duration

data class HabitAbstinence(
    val habitId: Int,
    val dateTimeRange: ClosedRange<LocalDateTime>,
    val duration: Duration
)