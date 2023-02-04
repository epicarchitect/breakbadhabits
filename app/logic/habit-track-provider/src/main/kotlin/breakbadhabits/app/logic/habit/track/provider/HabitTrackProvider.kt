package breakbadhabits.app.logic.habit.track.provider

import breakbadhabits.app.database.AppDatabase
import breakbadhabits.app.entity.Habit
import breakbadhabits.app.entity.HabitTrack
import breakbadhabits.framework.datetime.millisToLocalDateTime
import com.squareup.sqldelight.runtime.coroutines.asFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class HabitTrackProvider(private val appDatabase: AppDatabase) {

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

    suspend fun provide(id: HabitTrack.Id) = appDatabase.habitTrackQueries
        .selectById(id.value)
        .executeAsOneOrNull()
        ?.toEntity()

    private fun breakbadhabits.app.database.HabitTrack.toEntity() = HabitTrack(
        HabitTrack.Id(id),
        Habit.Id(habitId),
        HabitTrack.Range(rangeStart.millisToLocalDateTime()..rangeEnd.millisToLocalDateTime()),
        HabitTrack.DailyCount(dailyCount),
        comment?.let(HabitTrack::Comment)
    )
}