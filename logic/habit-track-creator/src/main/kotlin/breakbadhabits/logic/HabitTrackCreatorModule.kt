package breakbadhabits.logic

import breakbadhabits.entity.Habit
import breakbadhabits.entity.HabitTrack

class HabitTrackCreatorModule(private val delegate: Delegate) {

    fun createHabitTrackCreator() = HabitTrackCreator(delegate)

    interface Delegate {
        suspend fun insertHabitTrack(
            habitId: Habit.Id,
            interval: HabitTrack.Interval,
            dailyCount: HabitTrack.DailyCount,
            comment: HabitTrack.Comment?,
        )
    }
}