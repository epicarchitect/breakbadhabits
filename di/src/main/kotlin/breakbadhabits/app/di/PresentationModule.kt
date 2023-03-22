package breakbadhabits.app.di

import breakbadhabits.app.entity.Habit
import breakbadhabits.app.entity.HabitAppWidgetConfig
import breakbadhabits.app.entity.HabitTrack
import breakbadhabits.app.presentation.dashboard.DashboardViewModel
import breakbadhabits.app.presentation.habits.HabitAppWidgetCreationViewModel
import breakbadhabits.app.presentation.habits.HabitAppWidgetUpdatingViewModel
import breakbadhabits.app.presentation.habits.HabitAppWidgetsViewModel
import breakbadhabits.app.presentation.habits.HabitCreationViewModel
import breakbadhabits.app.presentation.habits.HabitDetailsViewModel
import breakbadhabits.app.presentation.habits.HabitTrackCreationViewModel
import breakbadhabits.app.presentation.habits.HabitTrackUpdatingViewModel
import breakbadhabits.app.presentation.habits.HabitTracksViewModel
import breakbadhabits.app.presentation.habits.HabitUpdatingViewModel

class PresentationModule(val logicModule: LogicModule) {
    fun createDashboardViewModel() = with(logicModule) {
        DashboardViewModel(
            habitProvider = habitProvider,
            habitAbstinenceProvider = habitAbstinenceProvider
        )
    }

    fun createHabitCreationViewModel() = with(logicModule) {
        HabitCreationViewModel(
            habitCreator = habitCreator,
            habitNewNameValidator = habitNewNameValidator,
            trackTimeValidator = habitTrackTimeValidator,
            trackEventCountValidator = habitTrackEventCountValidator,
            dateTimeProvider = dateTimeProvider,
            dateTimeConfigProvider = dateTimeConfigProvider,
            habitIconProvider = habitIconProvider
        )
    }

    fun createHabitUpdatingViewModel(habitId: Habit.Id) = with(logicModule) {
        HabitUpdatingViewModel(
            habitProvider = habitProvider,
            habitUpdater = habitUpdater,
            habitDeleter = habitDeleter,
            habitNewNameValidator = habitNewNameValidator,
            habitIconProvider = habitIconProvider,
            habitId = habitId
        )
    }

    fun createHabitDetailsViewModel(habitId: Habit.Id) = with(logicModule) {
        HabitDetailsViewModel(
            habitProvider = habitProvider,
            habitTrackProvider = habitTrackProvider,
            habitAbstinenceProvider = habitAbstinenceProvider,
            habitStatisticsProvider = habitStatisticsProvider,
            habitId = habitId
        )
    }

    fun createHabitTrackCreationViewModel(habitId: Habit.Id) = with(logicModule) {
        HabitTrackCreationViewModel(
            habitProvider = habitProvider,
            habitTrackCreator = habitTrackCreator,
            trackRangeValidator = habitTrackTimeValidator,
            trackEventCountValidator = habitTrackEventCountValidator,
            dateTimeProvider = dateTimeProvider,
            dateTimeConfigProvider = dateTimeConfigProvider,
            habitId = habitId
        )
    }

    fun createHabitTracksViewModel(habitId: Habit.Id) = with(logicModule) {
        HabitTracksViewModel(
            habitProvider = habitProvider,
            habitTrackProvider = habitTrackProvider,
            habitId = habitId
        )
    }

    fun createHabitTrackUpdatingViewModel(id: HabitTrack.Id) = with(logicModule) {
        HabitTrackUpdatingViewModel(
            habitProvider = habitProvider,
            habitTrackProvider = habitTrackProvider,
            habitTrackUpdater = habitTrackUpdater,
            habitTrackDeleter = habitTrackDeleter,
            trackRangeValidator = habitTrackTimeValidator,
            trackEventCountValidator = habitTrackEventCountValidator,
            dateTimeConfigProvider = dateTimeConfigProvider,
            habitTrackId = id
        )
    }

    fun createHabitAppWidgetsViewModel() = with(logicModule) {
        HabitAppWidgetsViewModel(
            habitAppWidgetConfigProvider = habitAppWidgetConfigProvider
        )
    }

    fun createHabitAppWidgetUpdatingViewModel(
        id: HabitAppWidgetConfig.Id
    ) = with(logicModule) {
        HabitAppWidgetUpdatingViewModel(
            habitProvider = habitProvider,
            habitAppWidgetConfigUpdater = habitAppWidgetConfigUpdater,
            id = id
        )
    }

    fun createHabitAppWidgetCreationViewModel(
        appWidgetId: HabitAppWidgetConfig.AppWidgetId
    ) = with(logicModule) {
        HabitAppWidgetCreationViewModel(
            habitProvider = habitProvider,
            habitAppWidgetConfigCreator = habitAppWidgetConfigCreator,
            appWidgetId = appWidgetId
        )
    }
}