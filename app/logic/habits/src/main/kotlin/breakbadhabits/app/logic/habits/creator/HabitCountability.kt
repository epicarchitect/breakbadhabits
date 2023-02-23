package breakbadhabits.app.logic.habits.creator

import breakbadhabits.app.entity.HabitTrack

sealed class HabitCountability {
    class Countable(val averageDailyCount: HabitTrack.DailyCount) : HabitCountability()
    object Uncountable : HabitCountability()
}