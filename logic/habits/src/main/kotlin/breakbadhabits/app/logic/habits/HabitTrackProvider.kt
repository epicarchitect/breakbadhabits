package breakbadhabits.app.logic.habits

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import breakbadhabits.app.database.AppDatabase
import breakbadhabits.app.logic.datetime.provider.DateTimeConfigProvider
import breakbadhabits.app.logic.habits.model.HabitTrack
import breakbadhabits.foundation.coroutines.CoroutineDispatchers
import breakbadhabits.foundation.coroutines.flow.mapItems
import breakbadhabits.foundation.datetime.MonthOfYear
import breakbadhabits.foundation.datetime.monthOfYear
import breakbadhabits.foundation.datetime.mountsBetween
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import breakbadhabits.app.database.HabitTrack as DatabaseHabitTrack

class HabitTrackProvider(
    private val appDatabase: AppDatabase,
    private val dateTimeConfigProvider: DateTimeConfigProvider,
    private val coroutineDispatchers: CoroutineDispatchers
) {

    fun habitTracksFlow() = appDatabase.habitTrackQueries
        .selectAll()
        .asFlow()
        .mapToList(coroutineDispatchers.io)
        .mapItems {
            it.toEntity()
        }.flowOn(coroutineDispatchers.default)

    fun habitTracksFlow(id: Int) = appDatabase.habitTrackQueries
        .selectByHabitId(id)
        .asFlow()
        .mapToList(coroutineDispatchers.io)
        .mapItems {
            it.toEntity()
        }.flowOn(coroutineDispatchers.default)

    fun habitTrackFlowByMaxEnd(id: Int) = appDatabase.habitTrackQueries
        .selectByHabitIdAndMaxEndTime(id)
        .asFlow()
        .mapToOneOrNull(coroutineDispatchers.io)
        .map {
            it?.toEntity()
        }.flowOn(coroutineDispatchers.default)

    fun monthsToHabitTracksFlow(id: Int) = combine(
        habitTracksFlow(id),
        dateTimeConfigProvider.configFlow()
    ) { tracks, dateTimeConfig ->
        val timeZone = dateTimeConfig.appTimeZone
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

    suspend fun getHabitTrack(id: Int) = withContext(coroutineDispatchers.default) {
        appDatabase.habitTrackQueries.selectById(id).executeAsOneOrNull()?.toEntity()
    }

    private fun DatabaseHabitTrack.toEntity() = HabitTrack(
        id = id,
        habitId = habitId,
        instantRange = startTime..endTime,
        eventCount = eventCount,
        comment = comment
    )
}