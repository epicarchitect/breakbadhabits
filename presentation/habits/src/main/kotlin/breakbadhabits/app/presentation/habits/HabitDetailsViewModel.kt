package breakbadhabits.app.presentation.habits

import androidx.lifecycle.viewModelScope
import breakbadhabits.app.entity.Habit
import breakbadhabits.app.logic.habits.provider.HabitAbstinenceProvider
import breakbadhabits.app.logic.habits.provider.HabitProvider
import breakbadhabits.app.logic.habits.provider.HabitStatisticsProvider
import breakbadhabits.app.logic.habits.provider.HabitTrackProvider
import breakbadhabits.foundation.controller.LoadingController
import breakbadhabits.foundation.viewmodel.ViewModel
import kotlinx.coroutines.flow.map

class HabitDetailsViewModel(
    habitProvider: HabitProvider,
    habitTrackProvider: HabitTrackProvider,
    habitAbstinenceProvider: HabitAbstinenceProvider,
    habitStatisticsProvider: HabitStatisticsProvider,
    habitId: Habit.Id
) : ViewModel() {

    val habitController = LoadingController(
        coroutineScope = viewModelScope,
        flow = habitProvider.provideHabitFlowById(habitId)
    )

    val habitTracksController = LoadingController(
        coroutineScope = viewModelScope,
        flow = habitTrackProvider.provideByHabitId(habitId)
    )

    val habitAbstinenceController = LoadingController(
        coroutineScope = viewModelScope,
        flow = habitAbstinenceProvider.provideCurrentAbstinenceFlowById(habitId)
    )

    val abstinenceListController = LoadingController(
        coroutineScope = viewModelScope,
        flow = habitAbstinenceProvider.provideAbstinenceListById(habitId)
    )

    val statisticsController = LoadingController(
        coroutineScope = viewModelScope,
        flow = habitStatisticsProvider.habitStatisticsFlowById(habitId)
    )
}