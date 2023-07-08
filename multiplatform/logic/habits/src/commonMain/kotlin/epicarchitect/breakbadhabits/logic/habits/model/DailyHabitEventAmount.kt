package epicarchitect.breakbadhabits.logic.habits.model

import kotlinx.datetime.LocalDate

class DailyHabitEventAmount(
    val tracks: List<HabitTrack>,
    val datesToAmount: Map<LocalDate, Int>
)