package breakbadhabits.app.presentation.habits

import androidx.lifecycle.viewModelScope
import breakbadhabits.app.entity.Habit
import breakbadhabits.app.logic.habits.provider.HabitAbstinenceProvider
import breakbadhabits.app.logic.habits.provider.HabitProvider
import breakbadhabits.app.logic.habits.provider.HabitStatisticsProvider
import breakbadhabits.app.logic.habits.provider.HabitTrackProvider
import breakbadhabits.foundation.controller.LoadingController
import breakbadhabits.foundation.viewmodel.ViewModel

class HabitTracksViewModel(
    habitProvider: HabitProvider,
    habitTrackProvider: HabitTrackProvider,
    habitId: Habit.Id
) : ViewModel() {

    val habitController = LoadingController(
        coroutineScope = viewModelScope,
        flow = habitProvider.habitFlow(habitId)
    )

    val habitTracksController = LoadingController(
        coroutineScope = viewModelScope,
        flow = habitTrackProvider.monthsToHabitTracksFlow(habitId)
    )
}