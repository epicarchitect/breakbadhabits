package breakbadhabits.logic

import breakbadhabits.entity.HabitTrack

sealed class HabitCountability {
    class Countable(val averageDailyCount: HabitTrack.DailyCount) : HabitCountability()
    class Uncountable : HabitCountability()
}