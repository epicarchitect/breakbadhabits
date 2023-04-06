package breakbadhabits.app.logic.habits.model

import kotlinx.datetime.LocalDate

class DailyHabitEventCount(
    val tracks: List<HabitTrack>,
    val dateToCount: Map<LocalDate, Int>
)