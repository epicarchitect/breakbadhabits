package breakbadhabits.app.dependecies

import breakbadhabits.entity.Habit
import breakbadhabits.entity.HabitTrack
import breakbadhabits.presentation.HabitCreationViewModel
import breakbadhabits.presentation.HabitDeletionViewModel
import breakbadhabits.presentation.HabitDetailsViewModel
import breakbadhabits.presentation.HabitTrackCreationViewModel
import breakbadhabits.presentation.HabitTrackViewModel
import breakbadhabits.presentation.HabitsDashboardViewModel

class PresentationModule(private val logicModule: LogicModule) {
    fun createHabitsDashboardViewModel() = HabitsDashboardViewModel(
        logicModule.habitProvider,
        logicModule.habitTrackProvider,
        logicModule.dateTimeProvider,
        logicModule.dateTimeRangeFormatter
    )

    fun createHabitCreationViewModel() = HabitCreationViewModel(
        logicModule.habitCreator,
        logicModule.habitNewNameValidator,
        logicModule.habitTrackIntervalValidator,
        logicModule.habitIconsProvider
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