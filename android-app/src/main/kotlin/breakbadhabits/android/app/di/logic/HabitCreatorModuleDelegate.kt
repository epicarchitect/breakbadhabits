package breakbadhabits.android.app.di.logic

import breakbadhabits.android.app.repository.HabitTracksRepository
import breakbadhabits.android.app.repository.HabitsRepository
import breakbadhabits.android.app.utils.TimeProvider
import breakbadhabits.entity.Habit
import breakbadhabits.entity.HabitTrack
import breakbadhabits.logic.HabitCreatorModule

class HabitCreatorModuleDelegate(
    private val habitsRepository: HabitsRepository,
    private val habitTracksRepository: HabitTracksRepository,
    private val timeProvider: TimeProvider
) : HabitCreatorModule.Delegate {
    override suspend fun insertHabit(
        name: Habit.Name,
        iconResource: Habit.IconResource,
        countability: Habit.Countability
    ) = habitsRepository.insertHabit(name, iconResource, countability)

    override suspend fun insertHabitTrack(
        habitId: Habit.Id,
        range: HabitTrack.Range,
        dailyCount: HabitTrack.DailyCount,
        comment: HabitTrack.Comment?
    ) = habitTracksRepository.insertHabitTrack(habitId, range, dailyCount, comment)

    override suspend fun habitNameExists(name: Habit.Name) = habitsRepository.habitNameExists(name)

    override fun getCurrentTime() = timeProvider.getCurrentTime()

    override fun getMaxHabitNameLength() = 30
}