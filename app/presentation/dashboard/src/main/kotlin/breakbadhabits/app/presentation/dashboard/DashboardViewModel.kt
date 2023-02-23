package breakbadhabits.app.presentation.dashboard

import androidx.lifecycle.viewModelScope
import breakbadhabits.app.entity.Habit
import breakbadhabits.app.entity.HabitTrack
import breakbadhabits.app.logic.datetime.formatter.DateTimeFormatter
import breakbadhabits.app.logic.datetime.provider.DateTimeProvider
import breakbadhabits.app.logic.habits.provider.HabitProvider
import breakbadhabits.app.logic.habits.provider.HabitTrackProvider
import breakbadhabits.foundation.controller.DataFlowController
import breakbadhabits.foundation.viewmodel.ViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class DashboardViewModel(
    habitProvider: HabitProvider,
    private val habitTrackProvider: HabitTrackProvider,
    private val dateTimeProvider: DateTimeProvider,
    private val dateTimeFormatter: DateTimeFormatter
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val habitItemsController = DataFlowController(
        coroutineScope = viewModelScope,
        flow = habitProvider.provideHabitsFlow().flatMapLatest { habits ->
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
        }
    )

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