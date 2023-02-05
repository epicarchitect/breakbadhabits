package breakbadhabits.app.presentation.dashboard

import androidx.lifecycle.viewModelScope
import breakbadhabits.app.entity.Habit
import breakbadhabits.app.entity.HabitTrack
import breakbadhabits.app.logic.datetime.formatter.DateTimeFormatter
import breakbadhabits.app.logic.datetime.provider.DateTimeProvider
import breakbadhabits.app.logic.habit.provider.HabitProvider
import breakbadhabits.app.logic.habit.track.provider.HabitTrackProvider
import breakbadhabits.framework.viewmodel.ViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class DashboardViewModel(
    habitProvider: HabitProvider,
    private val habitTrackProvider: HabitTrackProvider,
    private val dateTimeProvider: DateTimeProvider,
    private val dateTimeFormatter: DateTimeFormatter
) : ViewModel() {

    val items = habitProvider.provideHabitsFlow().flatMapLatest { habits ->
        if (habits.isEmpty()) flowOf(emptyList())
        else combine(
            habits.map { habit ->
                habitTrackProvider.provideByHabitIdAndMaxRangeEnd(habit.id).asAbstinenceTimeFlow()
            }
        ) { tracks ->
            habits.mapIndexed { index, habit ->
                HabitItem(
                    habit,
                    tracks[index]
                )
            }
        }
    }.map {
        if (it.isEmpty()) ItemsState.NotExist()
        else ItemsState.Loaded(it)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = ItemsState.Loading()
    )

    sealed class ItemsState {
        class Loading : ItemsState()
        class NotExist : ItemsState()
        class Loaded(val items: List<HabitItem>) : ItemsState()
    }

    data class HabitItem(
        val habit: Habit,
        val abstinenceTime: String?
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun Flow<HabitTrack?>.asAbstinenceTimeFlow() = flatMapLatest { lastTrack ->
        lastTrack ?: return@flatMapLatest flowOf(null)
        dateTimeProvider.currentTimeFlow().map { currentTime ->
            dateTimeFormatter.formatDistance(
                range = lastTrack.range.value.endInclusive..currentTime
            )
        }
    }
}