package breakbadhabits.logic

import breakbadhabits.database.AppDatabase
import breakbadhabits.entity.Habit
import breakbadhabits.entity.HabitTrack
import breakbadhabits.extension.datetime.millisToLocalDateTime
import com.squareup.sqldelight.runtime.coroutines.asFlow
import kotlinx.coroutines.flow.map

class HabitTrackProvider(private val appDatabase: AppDatabase) {

    fun provideFlow(id: HabitTrack.Id) = appDatabase.habitTrackQueries
        .selectById(id.value)
        .asFlow()
        .map {
            it.executeAsOneOrNull()?.toEntity()
        }

    suspend fun provide(id: HabitTrack.Id) = appDatabase.habitTrackQueries
        .selectById(id.value)
        .executeAsOneOrNull()
        ?.toEntity()

    private fun breakbadhabits.database.HabitTrack.toEntity() = HabitTrack(
        HabitTrack.Id(id),
        Habit.Id(habitId),
        HabitTrack.Range(rangeStart.millisToLocalDateTime()..rangeEnd.millisToLocalDateTime()),
        HabitTrack.DailyCount(dailyCount),
        comment?.let(HabitTrack::Comment)
    )
}