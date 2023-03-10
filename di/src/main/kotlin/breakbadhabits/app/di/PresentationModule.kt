package breakbadhabits.app.di

import breakbadhabits.app.entity.Habit
import breakbadhabits.app.presentation.dashboard.DashboardViewModel
import breakbadhabits.app.presentation.habits.HabitCreationViewModel
import breakbadhabits.app.presentation.habits.HabitDetailsViewModel
import breakbadhabits.app.presentation.habits.HabitTrackCreationViewModel
import breakbadhabits.app.presentation.habits.HabitUpdatingViewModel

class PresentationModule(val logicModule: LogicModule) {
    fun createDashboardViewModel() = with(logicModule) {
        DashboardViewModel(
            habitProvider,
            habitAbstinenceProvider
        )
    }

    fun createHabitCreationViewModel() = with(logicModule) {
        HabitCreationViewModel(
            habitCreator,
            habitNewNameValidator,
            habitTrackRangeValidator,
            habitTrackEventCountValidator,
            habitIconProvider
        )
    }

    fun createHabitUpdatingViewModel(habitId: Habit.Id) = with(logicModule) {
        HabitUpdatingViewModel(
            habitProvider,
            habitUpdater,
            habitDeleter,
            habitNewNameValidator,
            habitIconProvider,
            habitId
        )
    }

    fun createHabitDetailsViewModel(habitId: Habit.Id) = with(logicModule) {
        HabitDetailsViewModel(
            habitProvider,
            habitAbstinenceProvider,
            habitStatisticsProvider,
            habitId
        )
    }

    fun createHabitTrackCreationViewModel(habitId: Habit.Id) = with(logicModule) {
        HabitTrackCreationViewModel(
            habitTrackCreator,
            habitTrackRangeValidator,
            habitTrackEventCountValidator,
            habitId
        )
    }
}