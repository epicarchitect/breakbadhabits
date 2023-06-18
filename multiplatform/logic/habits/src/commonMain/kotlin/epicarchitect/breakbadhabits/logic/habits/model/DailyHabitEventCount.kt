package epicarchitect.breakbadhabits.logic.habits.model

import epicarchitect.breakbadhabits.foundation.datetime.ZonedDateTimeRange

class DailyHabitEventCount(
    val tracks: List<HabitTrack>,
    val rangeToCount: Map<ZonedDateTimeRange, Int>
)
