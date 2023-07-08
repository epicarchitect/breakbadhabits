package epicarchitect.breakbadhabits.logic.habits.provider

import epicarchitect.breakbadhabits.foundation.coroutines.CoroutineDispatchers
import epicarchitect.breakbadhabits.foundation.datetime.rangeTo
import epicarchitect.breakbadhabits.foundation.math.ranges.combineIntersections
import epicarchitect.breakbadhabits.logic.datetime.provider.DateTimeProvider
import epicarchitect.breakbadhabits.logic.habits.model.HabitAbstinence
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class HabitAbstinenceProvider(
    private val habitTrackProvider: HabitTrackProvider,
    private val dateTimeProvider: DateTimeProvider,
    private val coroutineDispatchers: CoroutineDispatchers
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    fun currentAbstinenceFlow(
        habitId: Int
    ) = habitTrackProvider.habitTrackFlowByMaxEnd(habitId).flatMapLatest { lastTrack ->
        if (lastTrack == null) {
            flowOf(null)
        } else {
            dateTimeProvider.currentDateTimeFlow().map { currentTime ->
                HabitAbstinence(
                    habitId = habitId,
                    dateTimeRange = lastTrack.dateTimeRange.endInclusive..currentTime
                )
            }
        }
    }.flowOn(coroutineDispatchers.default)

    fun abstinenceListFlow(habitId: Int) = combine(
        habitTrackProvider.habitTracksFlow(habitId),
        dateTimeProvider.currentDateTimeFlow()
    ) { tracks, currentTime ->
        val ranges = tracks.map { it.dateTimeRange }.combineIntersections()
        List(ranges.size) { index ->
            HabitAbstinence(
                habitId = habitId,
                dateTimeRange = if (index == ranges.lastIndex) {
                    ranges[index].endInclusive..currentTime
                } else {
                    ranges[index].endInclusive..ranges[index + 1].start
                }
            )
        }
    }.flowOn(coroutineDispatchers.default)
}