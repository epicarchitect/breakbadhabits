package breakbadhabits.app.di

import breakbadhabits.app.entity.Habit
import breakbadhabits.app.entity.HabitTrack
import breakbadhabits.app.presentation.dashboard.DashboardViewModel
import breakbadhabits.app.presentation.habits.HabitCreationViewModel
import breakbadhabits.app.presentation.habits.HabitDetailsViewModel
import breakbadhabits.app.presentation.habits.HabitTrackCreationViewModel
import breakbadhabits.app.presentation.habits.HabitTrackUpdatingViewModel
import breakbadhabits.app.presentation.habits.HabitTracksViewModel
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
            habitTrackProvider,
            habitAbstinenceProvider,
            habitStatisticsProvider,
            habitId
        )
    }

    fun createHabitTrackCreationViewModel(habitId: Habit.Id) = with(logicModule) {
        HabitTrackCreationViewModel(
            habitProvider,
            habitTrackCreator,
            habitTrackRangeValidator,
            habitTrackEventCountValidator,
            habitId
        )
    }

    fun createHabitTracksViewModel(habitId: Habit.Id) = with(logicModule) {
        HabitTracksViewModel(
            habitProvider,
            habitTrackProvider,
            habitId
        )
    }

    fun createHabitTrackUpdatingViewModel(id: HabitTrack.Id) = with(logicModule) {
        HabitTrackUpdatingViewModel(
            habitProvider,
            habitTrackProvider,
            habitTrackUpdater,
            habitTrackDeleter,
            habitTrackRangeValidator,
            habitTrackEventCountValidator,
            id
        )
    }
}