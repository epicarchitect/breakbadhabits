package breakbadhabits.app.logic.habits.provider

import breakbadhabits.app.entity.Habit
import breakbadhabits.app.entity.HabitAbstinence
import breakbadhabits.app.logic.datetime.provider.DateTimeProvider
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class HabitAbstinenceProvider(
    private val habitTrackProvider: HabitTrackProvider,
    private val dateTimeProvider: DateTimeProvider
) {
    fun currentAbstinenceFlow(
        habitId: Habit.Id
    ) = habitTrackProvider.habitTrackFlowByMaxEnd(habitId).flatMapLatest { lastTrack ->
        if (lastTrack == null) flowOf(null)
        else dateTimeProvider.currentTimeFlow().map { currentTime ->
            HabitAbstinence(
                habitId = habitId,
                range = HabitAbstinence.Range(lastTrack.range.value.endInclusive..currentTime)
            )
        }
    }

    fun abstinenceListFlow(habitId: Habit.Id) = combine(
        habitTrackProvider.habitTracksFlow(habitId),
        dateTimeProvider.currentTimeFlow(),
    ) { tracks, currentTime ->
        tracks.sortedBy { it.range.value.start }.let { sortedList ->
            List(sortedList.size) { index ->
                HabitAbstinence(
                    habitId = habitId,
                    range = HabitAbstinence.Range(
                        if (index == sortedList.lastIndex) {
                            sortedList[index].range.value.endInclusive..currentTime
                        } else {
                            sortedList[index].range.value.endInclusive..sortedList[index + 1].range.value.start
                        }
                    )
                )
            }
        }
    }
}