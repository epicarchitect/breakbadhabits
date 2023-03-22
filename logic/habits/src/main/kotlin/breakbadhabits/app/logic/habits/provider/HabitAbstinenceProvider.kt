package breakbadhabits.app.logic.habits.provider

import breakbadhabits.app.entity.Habit
import breakbadhabits.app.entity.HabitAbstinence
import breakbadhabits.app.logic.datetime.provider.DateTimeProvider
import breakbadhabits.foundation.coroutines.CoroutineDispatchers
import breakbadhabits.foundation.math.ranges.normalized
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
        habitId: Habit.Id
    ) = habitTrackProvider.habitTrackFlowByMaxEnd(habitId).flatMapLatest { lastTrack ->
        if (lastTrack == null) flowOf(null)
        else dateTimeProvider.currentTimeFlow().map { currentTime ->
            HabitAbstinence(
                habitId = habitId,
                range = HabitAbstinence.Range(lastTrack.time.endInclusive..currentTime)
            )
        }
    }.flowOn(coroutineDispatchers.default)

    fun abstinenceListFlow(habitId: Habit.Id) = combine(
        habitTrackProvider.habitTracksFlow(habitId),
        dateTimeProvider.currentTimeFlow(),
    ) { tracks, currentTime ->
        tracks.map { it.time }.normalized().let { sortedList ->
            List(sortedList.size) { index ->
                HabitAbstinence(
                    habitId = habitId,
                    range = HabitAbstinence.Range(
                        if (index == sortedList.lastIndex) {
                            sortedList[index].endInclusive..currentTime
                        } else {
                            sortedList[index].endInclusive..sortedList[index + 1].start
                        }
                    )
                )
            }
        }
    }.flowOn(coroutineDispatchers.default)
}