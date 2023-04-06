package breakbadhabits.app.logic.habits.provider

import android.util.Log
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import breakbadhabits.app.database.AppDatabase
import breakbadhabits.app.logic.datetime.provider.DateTimeProvider
import breakbadhabits.app.logic.habits.model.DailyHabitEventCount
import breakbadhabits.app.logic.habits.model.HabitTrack
import breakbadhabits.foundation.coroutines.CoroutineDispatchers
import breakbadhabits.foundation.coroutines.flow.mapItems
import breakbadhabits.foundation.datetime.InstantRange
import breakbadhabits.foundation.datetime.MonthOfYear
import breakbadhabits.foundation.datetime.countDays
import breakbadhabits.foundation.datetime.monthOfYear
import breakbadhabits.foundation.datetime.mountsBetween
import breakbadhabits.foundation.datetime.toLocalDateList
import breakbadhabits.foundation.datetime.toLocalDateRange
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt
import breakbadhabits.app.database.HabitTrack as DatabaseHabitTrack

class HabitTrackProvider(
    private val appDatabase: AppDatabase,
    private val dateTimeProvider: DateTimeProvider,
    private val coroutineDispatchers: CoroutineDispatchers
) {

    fun habitTracksFlow() = appDatabase.habitTrackQueries
        .selectAll()
        .asFlow()
        .mapToList(coroutineDispatchers.io)
        .mapItems {
            it.toEntity()
        }.flowOn(coroutineDispatchers.default)

    fun habitTracksFlow(habitId: Int) = appDatabase.habitTrackQueries
        .selectByHabitId(habitId)
        .asFlow()
        .mapToList(coroutineDispatchers.io)
        .mapItems {
            it.toEntity()
        }.flowOn(coroutineDispatchers.default)

    fun habitTrackFlowByMaxEnd(habitId: Int) = appDatabase.habitTrackQueries
        .selectByHabitIdAndMaxEndTime(habitId)
        .asFlow()
        .mapToOneOrNull(coroutineDispatchers.io)
        .map {
            it?.toEntity()
        }.flowOn(coroutineDispatchers.default)

    fun monthsToHabitTracksFlow(habitId: Int) = combine(
        habitTracksFlow(habitId),
        dateTimeProvider.timeZone
    ) { tracks, timeZone ->
        val map = mutableMapOf<MonthOfYear, MutableSet<HabitTrack>>()
        tracks.forEach { track ->
            val startMonth = track.instantRange.start.monthOfYear(timeZone)
            val endMonth = track.instantRange.endInclusive.monthOfYear(timeZone)
            val monthRange = startMonth..endMonth
            map.getOrPut(startMonth, ::mutableSetOf).add(track)
            map.getOrPut(endMonth, ::mutableSetOf).add(track)
            monthRange.mountsBetween().forEach {
                map.getOrPut(it, ::mutableSetOf).add(track)
            }
        }
        map.mapValues { it.value.toList() }
    }.flowOn(coroutineDispatchers.default)

    private fun provideHabitTracksByRange(
        habitId: Int,
        range: InstantRange
    ) = appDatabase.habitTrackQueries
        .selectByRange(habitId, range.start, range.endInclusive)
        .asFlow()
        .mapToList(coroutineDispatchers.io).mapItems {
            it.toEntity()
        }.flowOn(coroutineDispatchers.default)

    private fun provideTracksToDailyCount(habitId: Int, range: InstantRange) = combine(
        provideHabitTracksByRange(habitId, range),
        dateTimeProvider.timeZone
    ) { tracks, timeZone ->
        tracks.associateWith {
            eventCountToDailyCount(
                it.eventCount,
                it.instantRange.countDays(timeZone)
            )
        }.also {
            Log.d("test123", it.toString())
        }
    }

    fun provideDailyEventCountByRange(
        habitId: Int,
        range: InstantRange
    ) = combine(
        provideTracksToDailyCount(habitId, range),
        dateTimeProvider.timeZone
    ) { dailyCountsToTrack, timeZone ->
        DailyHabitEventCount(
            tracks = dailyCountsToTrack.keys.toList(),
            dateToCount = range.toLocalDateList(timeZone).associateWith { date ->
                dailyCountsToTrack.filter {
                    date in it.key.instantRange.toLocalDateRange(timeZone)
                }.entries.fold(0f) { total, entry ->
                    total + entry.value
                }.roundToInt()
            }
        )
    }.flowOn(coroutineDispatchers.default)

    suspend fun getHabitTrack(id: Int) = withContext(coroutineDispatchers.default) {
        appDatabase.habitTrackQueries.selectById(id).executeAsOneOrNull()?.toEntity()
    }

    private fun eventCountToDailyCount(
        eventCount: Int,
        countDays: Int
    ) = eventCount.toFloat() / countDays

    private fun DatabaseHabitTrack.toEntity() = HabitTrack(
        id = id,
        habitId = habitId,
        instantRange = startTime..endTime,
        eventCount = eventCount,
        comment = comment
    )
}
