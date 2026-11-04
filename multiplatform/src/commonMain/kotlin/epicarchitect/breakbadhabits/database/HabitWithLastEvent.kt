package epicarchitect.breakbadhabits.database

import androidx.room3.Embedded
import kotlin.time.Instant

data class HabitWithLastEvent(
    @Embedded val habit: Habit,
    val lastEventEndTime: Instant?
)
