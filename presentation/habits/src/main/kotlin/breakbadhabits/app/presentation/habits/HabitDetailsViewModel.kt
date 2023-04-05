package breakbadhabits.app.presentation.habits

import androidx.lifecycle.viewModelScope
import breakbadhabits.app.logic.habits.HabitAbstinenceProvider
import breakbadhabits.app.logic.habits.HabitProvider
import breakbadhabits.app.logic.habits.HabitStatisticsProvider
import breakbadhabits.app.logic.habits.model.Habit
import breakbadhabits.app.logic.habits.HabitTrackProvider
import breakbadhabits.foundation.controller.LoadingController
import breakbadhabits.foundation.viewmodel.ViewModel

class HabitDetailsViewModel(
    habitProvider: HabitProvider,
    habitTrackProvider: HabitTrackProvider,
    habitAbstinenceProvider: HabitAbstinenceProvider,
    habitStatisticsProvider: HabitStatisticsProvider,
    habitId: Int
) : ViewModel() {

    val habitController = LoadingController(
        coroutineScope = viewModelScope,
        flow = habitProvider.habitFlow(habitId)
    )

    val habitTracksController = LoadingController(
        coroutineScope = viewModelScope,
        flow = habitTrackProvider.habitTracksFlow(habitId)
    )

    val habitAbstinenceController = LoadingController(
        coroutineScope = viewModelScope,
        flow = habitAbstinenceProvider.currentAbstinenceFlow(habitId)
    )

    val abstinenceListController = LoadingController(
        coroutineScope = viewModelScope,
        flow = habitAbstinenceProvider.abstinenceListFlow(habitId)
    )

    val statisticsController = LoadingController(
        coroutineScope = viewModelScope,
        flow = habitStatisticsProvider.statisticsFlow(habitId)
    )
}