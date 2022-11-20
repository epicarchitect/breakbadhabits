package breakbadhabits.feature.habits.model

import breakbadhabits.entity.Habit
import breakbadhabits.entity.HabitTrack
import kolmachikhin.alexander.validation.Correct
import kotlinx.coroutines.flow.Flow

interface HabitTracksRepository {

    suspend fun insertHabitTrack(
        habitId: Habit.Id,
        interval: Correct<HabitTrack.Interval>,
        dailyCount: HabitTrack.DailyCount,
        comment: HabitTrack.Comment?
    )

    suspend fun updateHabitTrack(
        id: HabitTrack.Id,
        interval: Correct<HabitTrack.Interval>,
        dailyCount: HabitTrack.DailyCount,
        comment: HabitTrack.Comment?
    )

    suspend fun deleteHabitTracksByHabitId(id: Habit.Id)

    suspend fun deleteHabitTrackById(id: HabitTrack.Id)

    suspend fun getHabitTrackById(id: HabitTrack.Id): HabitTrack?

    fun habitTrackFlowById(id: HabitTrack.Id): Flow<HabitTrack?>

    fun habitTracksFlowByHabitId(id: Habit.Id): Flow<List<HabitTrack>>

    fun habitTrackFlowByHabitIdAndLastByTime(id: Habit.Id): Flow<HabitTrack?>

    fun habitTracksFlow(): Flow<List<HabitTrack>>

}