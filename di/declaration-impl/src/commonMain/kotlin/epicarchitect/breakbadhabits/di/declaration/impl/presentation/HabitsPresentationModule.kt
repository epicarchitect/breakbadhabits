package epicarchitect.breakbadhabits.di.declaration.impl.presentation

import epicarchitect.breakbadhabits.di.declaration.logic.DateTimeLogicModule
import epicarchitect.breakbadhabits.di.declaration.logic.HabitsLogicModule
import epicarchitect.breakbadhabits.di.declaration.presentation.HabitsPresentationModule
import epicarchitect.breakbadhabits.presentation.habits.HabitAppWidgetCreationViewModel
import epicarchitect.breakbadhabits.presentation.habits.HabitAppWidgetUpdatingViewModel
import epicarchitect.breakbadhabits.presentation.habits.HabitAppWidgetsViewModel
import epicarchitect.breakbadhabits.presentation.habits.HabitCreationViewModel
import epicarchitect.breakbadhabits.presentation.habits.HabitDetailsViewModel
import epicarchitect.breakbadhabits.presentation.habits.HabitTrackCreationViewModel
import epicarchitect.breakbadhabits.presentation.habits.HabitTrackUpdatingViewModel
import epicarchitect.breakbadhabits.presentation.habits.HabitTracksViewModel
import epicarchitect.breakbadhabits.presentation.habits.HabitUpdatingViewModel

class HabitsPresentationModuleImpl(
    private val habitsLogicModule: HabitsLogicModule,
    private val dateTimeLogicModule: DateTimeLogicModule
) : HabitsPresentationModule {
    override fun createHabitCreationViewModel() = HabitCreationViewModel(
        habitCreator = habitsLogicModule.habitCreator,
        habitNewNameValidator = habitsLogicModule.habitNewNameValidator,
        trackTimeValidator = habitsLogicModule.habitTrackTimeValidator,
        trackEventCountValidator = habitsLogicModule.habitTrackEventCountValidator,
        iconProvider = habitsLogicModule.habitIconProvider,
        dateTimeProvider = dateTimeLogicModule.dateTimeProvider
    )

    override fun createHabitUpdatingViewModel(habitId: Int) = HabitUpdatingViewModel(
        habitProvider = habitsLogicModule.habitProvider,
        habitUpdater = habitsLogicModule.habitUpdater,
        habitDeleter = habitsLogicModule.habitDeleter,
        habitNewNameValidator = habitsLogicModule.habitNewNameValidator,
        iconProvider = habitsLogicModule.habitIconProvider,
        habitId = habitId
    )

    override fun createHabitDetailsViewModel(habitId: Int) = HabitDetailsViewModel(
        habitProvider = habitsLogicModule.habitProvider,
        habitTrackProvider = habitsLogicModule.habitTrackProvider,
        habitAbstinenceProvider = habitsLogicModule.habitAbstinenceProvider,
        habitStatisticsProvider = habitsLogicModule.habitStatisticsProvider,
        dateTimeProvider = dateTimeLogicModule.dateTimeProvider,
        habitId = habitId
    )

    override fun createHabitTrackCreationViewModel(habitId: Int) = HabitTrackCreationViewModel(
        habitProvider = habitsLogicModule.habitProvider,
        habitTrackCreator = habitsLogicModule.habitTrackCreator,
        trackRangeValidator = habitsLogicModule.habitTrackTimeValidator,
        trackEventCountValidator = habitsLogicModule.habitTrackEventCountValidator,
        dateTimeProvider = dateTimeLogicModule.dateTimeProvider,
        habitId = habitId
    )

    override fun createHabitTracksViewModel(habitId: Int) = HabitTracksViewModel(
        habitProvider = habitsLogicModule.habitProvider,
        habitTrackProvider = habitsLogicModule.habitTrackProvider,
        habitId = habitId
    )

    override fun createHabitTrackUpdatingViewModel(habitTrackId: Int) = HabitTrackUpdatingViewModel(
        habitProvider = habitsLogicModule.habitProvider,
        habitTrackProvider = habitsLogicModule.habitTrackProvider,
        habitTrackUpdater = habitsLogicModule.habitTrackUpdater,
        habitTrackDeleter = habitsLogicModule.habitTrackDeleter,
        trackRangeValidator = habitsLogicModule.habitTrackTimeValidator,
        trackEventCountValidator = habitsLogicModule.habitTrackEventCountValidator,
        dateTimeProvider = dateTimeLogicModule.dateTimeProvider,
        habitTrackId = habitTrackId
    )

    override fun createHabitAppWidgetsViewModel() = HabitAppWidgetsViewModel(
        habitWidgetProvider = habitsLogicModule.habitWidgetProvider,
        habitProvider = habitsLogicModule.habitProvider
    )

    override fun createHabitWidgetUpdatingViewModel(
        habitWidgetId: Int
    ) = HabitAppWidgetUpdatingViewModel(
        habitProvider = habitsLogicModule.habitProvider,
        habitWidgetProvider = habitsLogicModule.habitWidgetProvider,
        habitWidgetUpdater = habitsLogicModule.habitWidgetUpdater,
        habitWidgetDeleter = habitsLogicModule.habitWidgetDeleter,
        habitWidgetId = habitWidgetId
    )

    override fun createHabitWidgetCreationViewModel(
        widgetSystemId: Int
    ) = HabitAppWidgetCreationViewModel(
        habitProvider = habitsLogicModule.habitProvider,
        habitWidgetCreator = habitsLogicModule.habitWidgetCreator,
        widgetSystemId = widgetSystemId
    )
}