package breakbadhabits.app.logic.habits.provider

import app.cash.sqldelight.coroutines.asFlow
import breakbadhabits.app.database.AppDatabase
import breakbadhabits.app.entity.Habit
import breakbadhabits.app.entity.HabitTrack
import breakbadhabits.app.logic.datetime.config.DateTimeConfigProvider
import breakbadhabits.foundation.coroutines.CoroutineDispatchers
import breakbadhabits.foundation.datetime.MonthOfYear
import breakbadhabits.foundation.datetime.monthOfYear
import breakbadhabits.foundation.datetime.mountsBetween
import breakbadhabits.foundation.datetime.secondsToInstant
import breakbadhabits.foundation.datetime.secondsToInstantRange
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.datetime.TimeZone
import breakbadhabits.app.database.HabitTrack as DatabaseHabitTrack

class HabitTrackProvider(
    private val appDatabase: AppDatabase,
    private val dateTimeConfigProvider: DateTimeConfigProvider,
    private val coroutineDispatchers: CoroutineDispatchers
) {

    fun habitTracksFlow(id: Habit.Id) = appDatabase.habitTrackQueries
        .selectByHabitId(id.value)
        .asFlow()
        .map {
            it.executeAsList().map {
                it.toEntity()
            }
        }.flowOn(coroutineDispatchers.io)

    fun habitTrackFlowByMaxEnd(id: Habit.Id) = appDatabase.habitTrackQueries
        .selectByHabitIdAndMaxRangeEnd(id.value)
        .asFlow()
        .map {
            it.executeAsOneOrNull()?.toEntity()
        }.flowOn(coroutineDispatchers.io)

    fun monthsToHabitTracksFlow(id: Habit.Id) = combine(
        habitTracksFlow(id),
        dateTimeConfigProvider.configFlow()
    ) { tracks, dateTimeConfig ->
        val timeZone = dateTimeConfig.appTimeZone
        val map = mutableMapOf<MonthOfYear, MutableSet<HabitTrack>>()
        tracks.forEach { track ->
            val startMonth = track.time.start.monthOfYear(timeZone)
            val endMonth = track.time.endInclusive.monthOfYear(timeZone)
            val monthRange = startMonth..endMonth
            map.getOrPut(startMonth, ::mutableSetOf).add(track)
            map.getOrPut(endMonth, ::mutableSetOf).add(track)
            monthRange.mountsBetween().forEach {
                map.getOrPut(it, ::mutableSetOf).add(track)
            }
        }
        map.mapValues { it.value.toList() }
    }.flowOn(coroutineDispatchers.default)

    suspend fun getHabitTrack(id: HabitTrack.Id) = withContext(coroutineDispatchers.default) {
        appDatabase.habitTrackQueries.selectById(id.value).executeAsOneOrNull()?.toEntity()
    }

    private fun DatabaseHabitTrack.toEntity() = HabitTrack(
        id = HabitTrack.Id(id),
        habitId = Habit.Id(habitId),
        time = HabitTrack.Time.of((startTimeInSecondsUtc..endTimeInSecondsUtc).secondsToInstantRange()),
        eventCount = HabitTrack.EventCount(dailyCount.toInt()),
        comment = comment?.let(HabitTrack::Comment),
        creationTime = HabitTrack.CreationTime(
            time = createdAtTimeInSecondsUtc.secondsToInstant(),
            timeZone = TimeZone.of(createdInTimeZone)
        )
    )
}