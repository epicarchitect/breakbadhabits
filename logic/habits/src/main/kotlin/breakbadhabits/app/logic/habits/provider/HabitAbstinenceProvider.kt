package breakbadhabits.app.logic.habits.provider

import breakbadhabits.app.entity.Habit
import breakbadhabits.app.entity.HabitAbstinence
import breakbadhabits.app.logic.datetime.provider.DateTimeProvider
import breakbadhabits.foundation.datetime.toMillis
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

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
        List(tracks.size) { index ->
            HabitAbstinence(
                habitId = habitId,
                range = HabitAbstinence.Range(
                    if (index == tracks.indices.last) {
                        tracks[index].range.value.endInclusive..currentTime
                    } else {
                        tracks[index].range.value.endInclusive..tracks[index + 1].range.value.start
                    }
                )
            )
        }
    }
}