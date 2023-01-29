package breakbadhabits.presentation

import breakbadhabits.extension.viewmodel.ViewModel
import androidx.lifecycle.viewModelScope
import breakbadhabits.entity.Habit
import breakbadhabits.entity.HabitTrack
import breakbadhabits.extension.coroutines.flow.mapItems
import breakbadhabits.logic.DateTimeProvider
import breakbadhabits.logic.DateTimeRangeFormatter
import breakbadhabits.logic.HabitProvider
import breakbadhabits.logic.HabitTrackProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlin.coroutines.coroutineContext

class HabitsDashboardViewModel(
    private val habitProvider: HabitProvider,
    private val habitTrackProvider: HabitTrackProvider,
    private val dateTimeProvider: DateTimeProvider,
    private val dateTimeRangeFormatter: DateTimeRangeFormatter
) : ViewModel() {

    val state = habitItemsFlow().map { habits ->
        if (habits.isEmpty()) State.NotExist()
        else State.Loaded(habits)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = State.Loading()
    )

    private fun habitItemsFlow() = habitProvider.provideHabitsFlow().mapItems { habit ->
        HabitItem(
            habit,
            habitTrackProvider.provideByHabitIdAndMaxRangeEnd(habit.id)
                .asAbstinenceTimeFlow()
                .stateIn(
                    scope = CoroutineScope(coroutineContext),
                    started = SharingStarted.WhileSubscribed(),
                    initialValue = null
                )
        )
    }

    sealed class State {
        class Loading : State()
        class NotExist : State()
        class Loaded(val items: List<HabitItem>) : State()
    }

    data class HabitItem(
        val habit: Habit,
        val abstinenceTime: StateFlow<String?>
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun Flow<HabitTrack?>.asAbstinenceTimeFlow() = flatMapLatest { lastTrack ->
        lastTrack ?: return@flatMapLatest flowOf(null)
        dateTimeProvider.currentTimeFlow().map { currentTime ->
            dateTimeRangeFormatter.formatDistance(
                range = lastTrack.range.value.endInclusive..currentTime
            )
        }
    }
}