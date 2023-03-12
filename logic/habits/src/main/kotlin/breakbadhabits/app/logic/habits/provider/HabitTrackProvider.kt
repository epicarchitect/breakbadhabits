package breakbadhabits.app.logic.habits.provider

import app.cash.sqldelight.coroutines.asFlow
import breakbadhabits.app.database.AppDatabase
import breakbadhabits.app.entity.Habit
import breakbadhabits.app.entity.HabitTrack
import breakbadhabits.foundation.datetime.millisToLocalDateTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import breakbadhabits.app.database.HabitTrack as DatabaseHabitTrack

class HabitTrackProvider(
    private val appDatabase: AppDatabase
) {

    fun provideById(id: HabitTrack.Id) = appDatabase.habitTrackQueries
        .selectById(id.value)
        .asFlow()
        .map {
            it.executeAsOneOrNull()?.toEntity()
        }

    fun provideByHabitId(id: Habit.Id) = appDatabase.habitTrackQueries
        .selectByHabitId(id.value)
        .asFlow()
        .map {
            it.executeAsList().map {
                it.toEntity()
            }
        }

    fun provideByHabitIdAndMaxRangeEnd(id: Habit.Id) = appDatabase.habitTrackQueries
        .selectByHabitIdAndMaxRangeEnd(id.value)
        .asFlow()
        .map {
            it.executeAsOneOrNull()?.toEntity()
        }

    suspend fun provide(id: HabitTrack.Id) = withContext(Dispatchers.IO) {
        appDatabase.habitTrackQueries
            .selectById(id.value)
            .executeAsOneOrNull()
            ?.toEntity()
    }

    private fun DatabaseHabitTrack.toEntity() = HabitTrack(
        HabitTrack.Id(id),
        Habit.Id(habitId),
        HabitTrack.Range(rangeStart.millisToLocalDateTime()..rangeEnd.millisToLocalDateTime()),
        HabitTrack.EventCount(dailyCount.toInt()),
        comment?.let(HabitTrack::Comment)
    )
}