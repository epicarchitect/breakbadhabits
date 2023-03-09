package breakbadhabits.app.logic.habits.provider

import app.cash.sqldelight.coroutines.asFlow
import breakbadhabits.app.database.AppDatabase
import breakbadhabits.app.entity.Habit
import breakbadhabits.app.entity.HabitTrack
import breakbadhabits.app.logic.habits.serializer.HabitTrackSerializer
import breakbadhabits.foundation.datetime.millisToLocalDateTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import breakbadhabits.app.database.HabitTrack as DatabaseHabitTrack

class HabitTrackProvider(
    private val appDatabase: AppDatabase,
    private val habitTrackSerializer: HabitTrackSerializer
) {

    fun provideById(id: HabitTrack.Id) = appDatabase.habitTrackQueries
        .selectById(id.value)
        .asFlow()
        .map {
            it.executeAsOneOrNull()?.toEntity()
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
        HabitTrack.EventCount(
            value = eventCount.toInt(),
            timeUnit = habitTrackSerializer.decodeEventCountTimeUnit(eventCountTimeUnit)
        ),
        comment?.let(HabitTrack::Comment)
    )
}