package breakbadhabits.app.di

import breakbadhabits.app.entity.Habit
import breakbadhabits.app.entity.HabitTrack
import breakbadhabits.app.presentation.habit.creation.HabitCreationViewModel
import breakbadhabits.app.presentation.habit.deletion.HabitDeletionViewModel
import breakbadhabits.app.presentation.habit.details.HabitDetailsViewModel
import breakbadhabits.presentation.HabitTrackCreationViewModel
import breakbadhabits.app.presentation.habit.track.details.HabitTrackViewModel
import breakbadhabits.app.presentation.dashboard.DashboardViewModel

class PresentationModule(private val logicModule: LogicModule) {
    fun createDashboardViewModel() = DashboardViewModel(
        logicModule.habitProvider,
        logicModule.habitTrackProvider,
        logicModule.dateTimeProvider,
        logicModule.dateTimeFormatter
    )

    fun createHabitCreationViewModel() = HabitCreationViewModel(
        logicModule.habitCreator,
        logicModule.habitNewNameValidator,
        logicModule.habitTrackIntervalValidator,
        logicModule.habitIconProvider
    )

    fun createHabitDeletionViewModel(habitId: Habit.Id) = HabitDeletionViewModel(
        logicModule.habitDeleter,
        habitId
    )

    fun createHabitDetailsViewModel(habitId: Habit.Id) = HabitDetailsViewModel(
        logicModule.habitProvider,
        habitId
    )

    fun createHabitTrackViewModel(habitTrackId: HabitTrack.Id) = HabitTrackViewModel(
        logicModule.habitTrackProvider,
        habitTrackId
    )

    fun createHabitTrackCreationViewModel(habitId: Habit.Id) = HabitTrackCreationViewModel(
        logicModule.habitTrackCreator,
        habitId
    )
}