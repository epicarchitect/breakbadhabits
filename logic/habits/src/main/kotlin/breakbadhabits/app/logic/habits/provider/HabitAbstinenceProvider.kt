package breakbadhabits.app.logic.habits.provider

import breakbadhabits.app.entity.Habit
import breakbadhabits.app.entity.HabitAbstinence
import breakbadhabits.app.entity.HabitTrack
import breakbadhabits.app.logic.datetime.provider.DateTimeProvider
import breakbadhabits.foundation.datetime.toMillis
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDateTime

class HabitAbstinenceProvider(
    private val habitTrackProvider: HabitTrackProvider,
    private val dateTimeProvider: DateTimeProvider
) {
    fun provideCurrentAbstinenceFlowById(
        habitId: Habit.Id
    ) = habitTrackProvider.provideByHabitIdAndMaxRangeEnd(habitId).flatMapLatest { lastTrack ->
        if (lastTrack == null) flowOf(null)
        else dateTimeProvider.currentTimeFlow().map { currentTime ->
            HabitAbstinence(
                habitId = habitId,
                range = HabitAbstinence.Range(lastTrack.range.value.endInclusive..currentTime)
            )
        }
    }

    fun provideAbstinenceListById(
        habitId: Habit.Id
    ) = combine(
        habitTrackProvider.provideByHabitId(habitId),
        dateTimeProvider.currentTimeFlow(),
    ) { tracks, currentTime ->
        tracks.toAbstinenceList(habitId, currentTime)
    }
}

fun List<HabitTrack>.toAbstinenceList(
    habitId: Habit.Id,
    currentTime: LocalDateTime
) = sortedBy { it.range.value.start }.let { sortedList ->
    List(sortedList.size) { index ->
        HabitAbstinence(
            habitId = habitId,
            range = HabitAbstinence.Range(
                if (index == indices.last) {
                    sortedList[index].range.value.endInclusive..currentTime
                } else {
                    sortedList[index].range.value.endInclusive..sortedList[index + 1].range.value.start
                }
            )
        )
    }
}