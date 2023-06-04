package epicarchitect.breakbadhabits.logic.habits.model

import epicarchitect.breakbadhabits.foundation.datetime.ZonedDate

class DailyHabitEventCount(
    val tracks: List<HabitTrack>,
    val dateToCount: Map<ZonedDate, Int>
)