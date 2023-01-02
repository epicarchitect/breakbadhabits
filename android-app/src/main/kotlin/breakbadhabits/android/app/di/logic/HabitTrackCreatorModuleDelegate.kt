package breakbadhabits.android.app.di.logic

import breakbadhabits.android.app.repository.HabitTracksRepository
import breakbadhabits.entity.Habit
import breakbadhabits.entity.HabitTrack
import breakbadhabits.logic.HabitTrackCreatorModule

class HabitTrackCreatorModuleDelegate(
    private val habitTracksRepository: HabitTracksRepository
) : HabitTrackCreatorModule.Delegate {

    override suspend fun insertHabitTrack(
        habitId: Habit.Id,
        interval: HabitTrack.Interval,
        dailyCount: HabitTrack.DailyCount,
        comment: HabitTrack.Comment?
    ) {
        habitTracksRepository.insertHabitTrack(
            habitId,
            interval,
            dailyCount,
            comment
        )
    }
}