package breakbadhabits.app.logic.habits.provider

import breakbadhabits.app.database.AppDatabase
import breakbadhabits.app.database.asFlowOfList
import breakbadhabits.app.database.asFlowOfOneOrNull
import breakbadhabits.app.logic.datetime.provider.DateTimeProvider
import breakbadhabits.app.logic.habits.model.DailyHabitEventCount
import breakbadhabits.app.logic.habits.model.HabitTrack
import breakbadhabits.foundation.coroutines.CoroutineDispatchers
import breakbadhabits.foundation.datetime.InstantRange
import breakbadhabits.foundation.datetime.MonthOfYear
import breakbadhabits.foundation.datetime.countDays
import breakbadhabits.foundation.datetime.monthOfYear
import breakbadhabits.foundation.datetime.mountsBetween
import breakbadhabits.foundation.datetime.toLocalDateList
import breakbadhabits.foundation.datetime.toLocalDateRange
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlin.math.roundToInt
import breakbadhabits.app.database.HabitTrack as DatabaseHabitTrack

class HabitTrackProvider(
    private val appDatabase: AppDatabase,
    private val dateTimeProvider: DateTimeProvider,
    private val coroutineDispatchers: CoroutineDispatchers
) {

    fun habitTracksFlow() = appDatabase.habitTrackQueries
        .selectAll()
        .asFlowOfList(coroutineDispatchers, ::asHabitTrack)

    fun habitTracksFlow(habitId: Int) = appDatabase.habitTrackQueries
        .selectByHabitId(habitId)
        .asFlowOfList(coroutineDispatchers, ::asHabitTrack)

    fun habitTrackFlowByMaxEnd(habitId: Int) = appDatabase.habitTrackQueries
        .selectByHabitIdAndMaxEndTime(habitId)
        .asFlowOfOneOrNull(coroutineDispatchers, ::asHabitTrack)

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
        .asFlowOfList(coroutineDispatchers, ::asHabitTrack)

    private fun provideTracksToDailyCount(habitId: Int, range: InstantRange) = combine(
        provideHabitTracksByRange(habitId, range),
        dateTimeProvider.timeZone
    ) { tracks, timeZone ->
        tracks.associateWith {
            it.eventCount.toFloat() / it.instantRange.countDays(timeZone)
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

    fun habitTrackFlow(id: Int) = appDatabase.habitTrackQueries
        .selectById(id)
        .asFlowOfOneOrNull(coroutineDispatchers, ::asHabitTrack)

    private fun asHabitTrack(value: DatabaseHabitTrack) = with(value) {
        HabitTrack(
            id = id,
            habitId = habitId,
            instantRange = startTime..endTime,
            eventCount = eventCount,
            comment = comment
        )
    }
}
