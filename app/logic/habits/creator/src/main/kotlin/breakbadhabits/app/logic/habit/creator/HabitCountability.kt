package breakbadhabits.app.logic.habit.creator

import breakbadhabits.app.entity.HabitTrack

sealed class HabitCountability {
    class Countable(val averageDailyCount: HabitTrack.DailyCount) : HabitCountability()
    class Uncountable : HabitCountability()
}