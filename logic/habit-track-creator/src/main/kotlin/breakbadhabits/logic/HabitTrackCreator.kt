package breakbadhabits.logic

import breakbadhabits.entity.Habit
import breakbadhabits.entity.HabitTrack

class HabitTrackCreator internal constructor(
    private val delegate: HabitTrackCreatorModule.Delegate
) {

    suspend fun createHabitTrack(
        habitId: Habit.Id,
        range: HabitTrack.Range,
        dailyCount: HabitTrack.DailyCount,
        comment: HabitTrack.Comment?,
    ) {
        delegate.insertHabitTrack(
            habitId,
            range,
            dailyCount,
            comment
        )
    }
}