package breakbadhabits.app.di

import breakbadhabits.app.entity.Habit
import breakbadhabits.app.entity.HabitTrack
import breakbadhabits.app.presentation.dashboard.DashboardViewModel
import breakbadhabits.app.presentation.habit.creation.HabitCreationViewModel
import breakbadhabits.app.presentation.habit.deletion.HabitDeletionViewModel
import breakbadhabits.app.presentation.habit.details.HabitDetailsViewModel
import breakbadhabits.app.presentation.habit.track.details.HabitTrackViewModel
import breakbadhabits.presentation.HabitTrackCreationViewModel

class PresentationModule(private val logicModule: LogicModule) {
    fun createDashboardViewModel() = with(logicModule) {
        DashboardViewModel(
            habitProvider,
            habitTrackProvider,
            dateTimeProvider,
            dateTimeFormatter
        )
    }

    fun createHabitCreationViewModel() = with(logicModule) {
        HabitCreationViewModel(
            habitCreator,
            habitNewNameValidator,
            habitTrackIntervalValidator,
            habitIconProvider
        )
    }

    fun createHabitDeletionViewModel(habitId: Habit.Id) = with(logicModule) {
        HabitDeletionViewModel(
            habitDeleter,
            habitId
        )
    }

    fun createHabitDetailsViewModel(habitId: Habit.Id) = with(logicModule) {
        HabitDetailsViewModel(
            habitProvider,
            habitId
        )
    }

    fun createHabitTrackViewModel(habitTrackId: HabitTrack.Id) = with(logicModule) {
        HabitTrackViewModel(
            habitTrackProvider,
            habitTrackId
        )
    }

    fun createHabitTrackCreationViewModel(habitId: Habit.Id) = with(logicModule) {
        HabitTrackCreationViewModel(
            habitTrackCreator,
            habitId
        )
    }
}