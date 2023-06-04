package epicarchitect.breakbadhabits.logic.habits.provider

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import epicarchitect.breakbadhabits.foundation.coroutines.CoroutineDispatchers
import epicarchitect.breakbadhabits.foundation.datetime.MonthOfYear
import epicarchitect.breakbadhabits.foundation.datetime.ZonedDateTimeRange
import epicarchitect.breakbadhabits.foundation.datetime.countDays
import epicarchitect.breakbadhabits.foundation.datetime.monthOfYear
import epicarchitect.breakbadhabits.foundation.datetime.mountsBetween
import epicarchitect.breakbadhabits.foundation.datetime.toDateList
import epicarchitect.breakbadhabits.foundation.datetime.toDateRange
import epicarchitect.breakbadhabits.logic.datetime.provider.DateTimeProvider
import epicarchitect.breakbadhabits.logic.habits.model.DailyHabitEventCount
import epicarchitect.breakbadhabits.logic.habits.model.HabitTrack
import epicarchitect.breakbadhabits.sqldelight.main.MainDatabase
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.datetime.TimeZone
import kotlin.math.roundToInt
import epicarchitect.breakbadhabits.sqldelight.main.HabitTrack as DatabaseHabitTrack

class HabitTrackProvider(
    private val mainDatabase: MainDatabase,
    private val dateTimeProvider: DateTimeProvider,
    private val coroutineDispatchers: CoroutineDispatchers
) {

    fun habitTracksFlow() = combine(
        mainDatabase.habitTrackQueries
            .selectAll()
            .asFlow()
            .mapToList(coroutineDispatchers.io),
        dateTimeProvider.currentTimeZoneFlow()
    ) { list, timeZone ->
        list.map { asHabitTrack(it, timeZone) }
    }

    fun habitTracksFlow(habitId: Int) = combine(
        mainDatabase.habitTrackQueries
            .selectByHabitId(habitId)
            .asFlow()
            .mapToList(coroutineDispatchers.io),
        dateTimeProvider.currentTimeZoneFlow()
    ) { list, timeZone ->
        list.map { asHabitTrack(it, timeZone) }
    }

    fun habitTrackFlowByMaxEnd(habitId: Int) = combine(
        mainDatabase.habitTrackQueries
            .selectByHabitIdAndMaxEndTime(habitId)
            .asFlow()
            .mapToOneOrNull(coroutineDispatchers.io),
        dateTimeProvider.currentTimeZoneFlow()
    ) { entity, timeZone ->
        entity?.let { asHabitTrack(it, timeZone) }
    }.flowOn(coroutineDispatchers.default)

    fun monthsToHabitTracksFlow(habitId: Int) = habitTracksFlow(habitId).map { tracks ->
        val map = mutableMapOf<MonthOfYear, MutableSet<HabitTrack>>()
        tracks.forEach { track ->
            val startMonth = track.dateTimeRange.start.dateTime.date.monthOfYear
            val endMonth = track.dateTimeRange.endInclusive.dateTime.date.monthOfYear
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
        range: ZonedDateTimeRange
    ) = combine(
        mainDatabase.habitTrackQueries
            .selectByRange(habitId, range.start.instant, range.endInclusive.instant)
            .asFlow()
            .mapToList(coroutineDispatchers.io),
        dateTimeProvider.currentTimeZoneFlow()
    ) { list, timeZone ->
        list.map { asHabitTrack(it, timeZone) }
    }

    private fun provideTracksToDailyCount(
        habitId: Int,
        range: ZonedDateTimeRange
    ) = provideHabitTracksByRange(habitId, range).map { tracks ->
        tracks.associateWith {
            it.eventCount.toFloat() / it.dateTimeRange.countDays()
        }
    }

    fun provideDailyEventCountByRange(
        habitId: Int,
        range: ZonedDateTimeRange
    ) = provideTracksToDailyCount(habitId, range).map { dailyCountsToTrack ->
        DailyHabitEventCount(
            tracks = dailyCountsToTrack.keys.toList(),
            dateToCount = range.toDateList().associateWith { date ->
                dailyCountsToTrack.filter {
                    date in it.key.dateTimeRange.toDateRange()
                }.entries.fold(0f) { total, entry ->
                    total + entry.value
                }.roundToInt()
            }
        )
    }.flowOn(coroutineDispatchers.default)

    fun habitTrackFlow(id: Int) = combine(
        mainDatabase.habitTrackQueries
            .selectById(id)
            .asFlow()
            .mapToOneOrNull(coroutineDispatchers.io),
        dateTimeProvider.currentTimeZoneFlow()
    ) { entity, timeZone ->
        entity?.let { asHabitTrack(it, timeZone) }
    }.flowOn(coroutineDispatchers.default)

    private fun asHabitTrack(
        value: DatabaseHabitTrack,
        timeZone: TimeZone
    ) = with(value) {
        HabitTrack(
            id = id,
            habitId = habitId,
            dateTimeRange = ZonedDateTimeRange(
                start = startTime,
                endInclusive = endTime,
                timeZone = timeZone
            ),
            eventCount = eventCount,
            comment = comment
        )
    }
}
