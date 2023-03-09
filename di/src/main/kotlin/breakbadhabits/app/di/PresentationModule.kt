package breakbadhabits.app.di

import breakbadhabits.app.entity.Habit
import breakbadhabits.app.entity.HabitTrack
import breakbadhabits.app.presentation.dashboard.DashboardViewModel
import breakbadhabits.app.presentation.habits.HabitCreationViewModel
import breakbadhabits.app.presentation.habits.HabitDeletionViewModel
import breakbadhabits.app.presentation.habits.HabitDetailsViewModel
import breakbadhabits.app.presentation.habits.HabitTrackCreationViewModel
import breakbadhabits.app.presentation.habits.HabitTrackViewModel

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
            habitTrackValueValidator,
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