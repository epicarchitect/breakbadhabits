package breakbadhabits.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import breakbadhabits.entity.Habit
import breakbadhabits.entity.HabitTrack
import breakbadhabits.logic.DateTimeRangeFormatter
import breakbadhabits.logic.HabitProvider
import breakbadhabits.logic.HabitTrackProvider
import breakbadhabits.logic.DateTimeProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HabitsDashboardViewModel(
    private val habitProvider: HabitProvider,
    private val habitTrackProvider: HabitTrackProvider,
    private val dateTimeProvider: DateTimeProvider,
    private val dateTimeRangeFormatter: DateTimeRangeFormatter
) : ViewModel() {

    val state = habitItemsFlow().map { habits ->
        if (habits.isEmpty()) State.NotExist()
        else State.Loaded(habits)
    }.stateIn(viewModelScope, SharingStarted.Eagerly, State.Loading())

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun habitItemsFlow() = habitProvider.provideHabitsFlow().flatMapLatest { habits ->
        if (habits.isEmpty()) flowOf(emptyList())
        else combine(
            habits.map { habit ->
                habitTrackProvider.provideLastHabitTrackFlowByHabitId(habit.id)
            }
        ) { tracks ->
            habits.mapIndexed { index, habit ->
                HabitItem(
                    habit,
                    tracks[index]
                )
            }
        }
    }

    sealed class State {
        class Loading : State()
        class NotExist : State()
        class Loaded(val items: List<HabitItem>) : State()
    }

    inner class HabitItem(
        val habit: Habit,
        private val lastTrack: HabitTrack?
    ) {
        fun abstinenceTimeFlow() = dateTimeProvider.currentTimeFlow().map { currentTime ->
            lastTrack?.let { track ->
                dateTimeRangeFormatter.formatDistance(
                    range = track.range.value.endInclusive..currentTime
                )
            }
        }
    }
}