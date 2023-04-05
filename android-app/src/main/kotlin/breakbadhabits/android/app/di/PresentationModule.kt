package breakbadhabits.android.app.di

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
            localIconProvider = localIconProvider
        )
    }

    fun createHabitUpdatingViewModel(habitId: Int) = with(logicModule) {
        HabitUpdatingViewModel(
            habitProvider = habitProvider,
            habitUpdater = habitUpdater,
            habitDeleter = habitDeleter,
            habitNewNameValidator = habitNewNameValidator,
            localIconProvider = localIconProvider,
            habitId = habitId
        )
    }

    fun createHabitDetailsViewModel(habitId: Int) = with(logicModule) {
        HabitDetailsViewModel(
            habitProvider = habitProvider,
            habitTrackProvider = habitTrackProvider,
            habitAbstinenceProvider = habitAbstinenceProvider,
            habitStatisticsProvider = habitStatisticsProvider,
            habitId = habitId
        )
    }

    fun createHabitTrackCreationViewModel(habitId: Int) = with(logicModule) {
        HabitTrackCreationViewModel(
            habitProvider = habitProvider,
            habitTrackCreator = habitTrackCreator,
            trackRangeValidator = habitTrackTimeValidator,
            trackEventCountValidator = habitTrackEventCountValidator,
            dateTimeProvider = dateTimeProvider,
            habitId = habitId
        )
    }

    fun createHabitTracksViewModel(habitId: Int) = with(logicModule) {
        HabitTracksViewModel(
            habitProvider = habitProvider,
            habitTrackProvider = habitTrackProvider,
            habitId = habitId
        )
    }

    fun createHabitTrackUpdatingViewModel(habitTrackId: Int) = with(logicModule) {
        HabitTrackUpdatingViewModel(
            habitProvider = habitProvider,
            habitTrackProvider = habitTrackProvider,
            habitTrackUpdater = habitTrackUpdater,
            habitTrackDeleter = habitTrackDeleter,
            trackRangeValidator = habitTrackTimeValidator,
            trackEventCountValidator = habitTrackEventCountValidator,
            dateTimeProvider = dateTimeProvider,
            habitTrackId = habitTrackId
        )
    }

    fun createHabitAppWidgetsViewModel() = with(logicModule) {
        HabitAppWidgetsViewModel(
            habitProvider = habitProvider,
            habitWidgetProvider = habitWidgetProvider
        )
    }

    fun createHabitWidgetUpdatingViewModel(
        habitWidgetId: Int
    ) = with(logicModule) {
        HabitAppWidgetUpdatingViewModel(
            habitProvider = habitProvider,
            habitWidgetProvider = habitWidgetProvider,
            habitWidgetUpdater = habitWidgetUpdater,
            habitWidgetDeleter = habitWidgetDeleter,
            habitWidgetId = habitWidgetId
        )
    }

    fun createHabitWidgetCreationViewModel(
        widgetSystemId: Int
    ) = with(logicModule) {
        HabitAppWidgetCreationViewModel(
            habitProvider = habitProvider,
            habitWidgetCreator = habitWidgetCreator,
            widgetSystemId = widgetSystemId
        )
    }
}