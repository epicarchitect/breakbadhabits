package breakbadhabits.app.logic.habits.provider

import app.cash.sqldelight.coroutines.asFlow
import breakbadhabits.app.database.AppDatabase
import breakbadhabits.app.entity.Habit
import breakbadhabits.app.entity.HabitTrack
import breakbadhabits.foundation.datetime.MonthOfYear
import breakbadhabits.foundation.datetime.secondsToInstantRange
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.time.YearMonth
import breakbadhabits.app.database.HabitTrack as DatabaseHabitTrack

class HabitTrackProvider(
    private val appDatabase: AppDatabase
) {

    fun habitTracksFlow(id: Habit.Id) = appDatabase.habitTrackQueries
        .selectByHabitId(id.value)
        .asFlow()
        .map {
            it.executeAsList().map {
                it.toEntity()
            }
        }

    fun monthsToHabitTracksFlow(id: Habit.Id) = habitTracksFlow(id).map {
        val month = YearMonth.now().let {
            MonthOfYear(it.year, it.month)
        }
        mapOf(month to it)
    }

    fun habitTrackFlowByMaxEnd(id: Habit.Id) = appDatabase.habitTrackQueries
        .selectByHabitIdAndMaxRangeEnd(id.value)
        .asFlow()
        .map {
            it.executeAsOneOrNull()?.toEntity()
        }

    private fun DatabaseHabitTrack.toEntity() = HabitTrack(
        HabitTrack.Id(id),
        Habit.Id(habitId),
        HabitTrack.Range((startTimeInSeconds..endTimeInSeconds).secondsToInstantRange()),
        HabitTrack.EventCount(dailyCount.toInt()),
        comment?.let(HabitTrack::Comment)
    )
}