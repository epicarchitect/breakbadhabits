package breakbadhabits.app.presentation.habits

import androidx.lifecycle.viewModelScope
import breakbadhabits.app.logic.habits.HabitProvider
import breakbadhabits.app.logic.habits.entity.Habit
import breakbadhabits.app.logic.habits.tracks.HabitTrackProvider
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