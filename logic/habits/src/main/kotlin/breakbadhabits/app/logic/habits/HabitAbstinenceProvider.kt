package breakbadhabits.app.logic.habits

import breakbadhabits.app.logic.datetime.provider.DateTimeProvider
import breakbadhabits.app.logic.habits.model.HabitAbstinence
import breakbadhabits.foundation.coroutines.CoroutineDispatchers
import breakbadhabits.foundation.math.ranges.combineIntersections
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
    fun currentAbstinenceFlow(
        habitId: Int
    ) = habitTrackProvider.habitTrackFlowByMaxEnd(habitId).flatMapLatest { lastTrack ->
        if (lastTrack == null) flowOf(null)
        else dateTimeProvider.currentTime.map { currentTime ->
            HabitAbstinence(
                habitId = habitId,
                instantRange = lastTrack.instantRange.endInclusive..currentTime
            )
        }
    }.flowOn(coroutineDispatchers.default)

    fun abstinenceListFlow(habitId: Int) = combine(
        habitTrackProvider.habitTracksFlow(habitId),
        dateTimeProvider.currentTime,
    ) { tracks, currentTime ->
        val ranges = tracks.map { it.instantRange }.combineIntersections()
        List(ranges.size) { index ->
            HabitAbstinence(
                habitId = habitId,
                instantRange = if (index == ranges.lastIndex) {
                    ranges[index].endInclusive..currentTime
                } else {
                    ranges[index].endInclusive..ranges[index + 1].start
                }
            )
        }
    }.flowOn(coroutineDispatchers.default)
}