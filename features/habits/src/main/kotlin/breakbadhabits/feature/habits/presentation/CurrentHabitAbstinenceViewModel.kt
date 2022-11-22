package breakbadhabits.feature.habits.presentation

import breakbadhabits.entity.Habit
import breakbadhabits.entity.HabitAbstinence
import breakbadhabits.extension.datetime.LocalDateTimeInterval
import breakbadhabits.feature.habits.model.HabitTracksRepository
import breakbadhabits.feature.habits.model.TimeProvider
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class CurrentHabitAbstinenceViewModel internal constructor(
    habitTracksRepository: HabitTracksRepository,
    timeProvider: TimeProvider,
    private val habitId: Habit.Id
) : EpicViewModel() {

    val state = combine(
        habitTracksRepository.habitTrackFlowByHabitIdAndLastByTime(habitId),
        timeProvider.currentTimeFlow()
    ) { lastTrack, currentDateTime ->
        lastTrack ?: return@combine State.NotExist()

        State.Loaded(
            HabitAbstinence(
                habitId = habitId,
                interval = HabitAbstinence.Interval(
                    LocalDateTimeInterval(
                        start = lastTrack.interval.value.end,
                        end = currentDateTime
                    )
                )
            )
        )
    }.stateIn(coroutineScope, SharingStarted.Eagerly, State.Loading())

    sealed class State {
        class Loading : State()
        class NotExist : State()
        class Loaded(val abstinence: HabitAbstinence) : State()
    }
}