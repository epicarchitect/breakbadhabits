package epicarchitect.breakbadhabits.logic.habits.provider

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import epicarchitect.breakbadhabits.foundation.coroutines.CoroutineDispatchers
import epicarchitect.breakbadhabits.foundation.datetime.MonthOfYear
import epicarchitect.breakbadhabits.foundation.datetime.monthOfYear
import epicarchitect.breakbadhabits.foundation.datetime.mountsBetween
import epicarchitect.breakbadhabits.foundation.datetime.numberOfDays
import epicarchitect.breakbadhabits.logic.datetime.provider.DateTimeProvider
import epicarchitect.breakbadhabits.logic.datetime.provider.toInstantBy
import epicarchitect.breakbadhabits.logic.habits.model.DailyHabitEventAmount
import epicarchitect.breakbadhabits.logic.habits.model.HabitTrack
import epicarchitect.breakbadhabits.sqldelight.main.MainDatabase
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
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
        list.map { mapToHabitTrack(it, timeZone) }
    }

    fun habitTracksFlow(habitId: Int) = combine(
        mainDatabase.habitTrackQueries
            .selectByHabitId(habitId)
            .asFlow()
            .mapToList(coroutineDispatchers.io),
        dateTimeProvider.currentTimeZoneFlow()
    ) { list, timeZone ->
        list.map { mapToHabitTrack(it, timeZone) }
    }

    fun habitTrackWithMaxEndFlow(habitId: Int) = combine(
        mainDatabase.habitTrackQueries
            .selectByHabitIdAndMaxEndTime(habitId)
            .asFlow()
            .mapToOneOrNull(coroutineDispatchers.io),
        dateTimeProvider.currentTimeZoneFlow()
    ) { entity, timeZone ->
        entity?.let { mapToHabitTrack(it, timeZone) }
    }.flowOn(coroutineDispatchers.default)

    fun monthsToHabitTracksFlow(habitId: Int) = habitTracksFlow(habitId).map { tracks ->
        val map = mutableMapOf<MonthOfYear, MutableSet<HabitTrack>>()
        tracks.forEach { track ->
            val startMonth = track.dateTimeRange.start.date.monthOfYear
            val endMonth = track.dateTimeRange.endInclusive.date.monthOfYear
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
        range: ClosedRange<LocalDateTime>
    ) = combine(
        mainDatabase.habitTrackQueries
            .selectByRange(
                habitId = habitId,
                startTime = range.start.toInstantBy(dateTimeProvider),
                endTime = range.endInclusive.toInstantBy(dateTimeProvider)
            )
            .asFlow()
            .mapToList(coroutineDispatchers.io),
        dateTimeProvider.currentTimeZoneFlow()
    ) { list, timeZone ->
        list.map { mapToHabitTrack(it, timeZone) }
    }

    private fun provideTracksToDailyAmount(
        habitId: Int,
        range: ClosedRange<LocalDateTime>
    ) = combine(
        provideHabitTracksByRange(habitId, range),
        dateTimeProvider.currentTimeZoneFlow()
    ) { tracks, timeZone ->
        tracks.associateWith {
            it.eventCount.toFloat() / it.dateTimeRange.numberOfDays(timeZone)
        }
    }

    fun provideDailyEventCountByRange(
        habitId: Int,
        range: ClosedRange<LocalDateTime>
    ) = provideTracksToDailyAmount(habitId, range).map { dailyCountsToTrack ->
        DailyHabitEventAmount(
            tracks = dailyCountsToTrack.keys.toList(),
            datesToAmount = emptyMap()
//            datesToAmount = range.split(step = 1.days).associateWith { range ->
//                dailyCountsToTrack.filter {
//                    range in it.key.dateTimeRange
//                }.entries.fold(0f) { total, entry ->
//                    total + entry.value
//                }.roundToInt()
//            }
        )
    }.flowOn(coroutineDispatchers.default)

    fun habitTrackFlow(id: Int) = combine(
        mainDatabase.habitTrackQueries
            .selectById(id)
            .asFlow()
            .mapToOneOrNull(coroutineDispatchers.io),
        dateTimeProvider.currentTimeZoneFlow()
    ) { entity, timeZone ->
        entity?.let { mapToHabitTrack(it, timeZone) }
    }.flowOn(coroutineDispatchers.default)

    private fun mapToHabitTrack(
        value: DatabaseHabitTrack,
        timeZone: TimeZone
    ) = with(value) {
        HabitTrack(
            id = id,
            habitId = habitId,
            dateTimeRange = startTime.toLocalDateTime(timeZone)..endTime.toLocalDateTime(timeZone),
            eventCount = eventCount,
            comment = comment
        )
    }
}