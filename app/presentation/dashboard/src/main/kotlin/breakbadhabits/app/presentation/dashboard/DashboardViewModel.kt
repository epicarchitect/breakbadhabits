package breakbadhabits.app.presentation.dashboard

import breakbadhabits.framework.viewmodel.ViewModel
import androidx.lifecycle.viewModelScope
import breakbadhabits.app.entity.Habit
import breakbadhabits.app.entity.HabitTrack
import breakbadhabits.framework.coroutines.flow.mapItems
import breakbadhabits.app.logic.datetime.provider.DateTimeProvider
import breakbadhabits.app.logic.datetime.formatter.DateTimeFormatter
import breakbadhabits.app.logic.habit.provider.HabitProvider
import breakbadhabits.app.logic.habit.track.provider.HabitTrackProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlin.coroutines.coroutineContext

class DashboardViewModel(
    private val habitProvider: HabitProvider,
    private val habitTrackProvider: HabitTrackProvider,
    private val dateTimeProvider: DateTimeProvider,
    private val dateTimeFormatter: DateTimeFormatter
) : ViewModel() {

    val state = habitItemsFlow().map { habits ->
        if (habits.isEmpty()) State.NotExist()
        else State.Loaded(habits)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = State.Loading()
    )

    private fun habitItemsFlow() = habitProvider.provideHabitsFlow().flatMapLatest { habits ->
        combine(
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
    }

//    private fun habitItemsFlow() = habitProvider.provideHabitsFlow().mapItems { habit ->
//        HabitItem(
//            habit,
//            habitTrackProvider.provideByHabitIdAndMaxRangeEnd(habit.id)
//                .asAbstinenceTimeFlow()
//                .stateIn(
//                    scope = CoroutineScope(coroutineContext),
//                    started = SharingStarted.WhileSubscribed(),
//                    initialValue = null
//                )
//        )
//    }

    sealed class State {
        class Loading : State()
        class NotExist : State()
        class Loaded(val items: List<HabitItem>) : State()
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