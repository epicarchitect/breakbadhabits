package epicarchitect.breakbadhabits.di.declaration.presentation

import epicarchitect.breakbadhabits.presentation.habits.HabitAppWidgetCreationViewModel
import epicarchitect.breakbadhabits.presentation.habits.HabitAppWidgetUpdatingViewModel
import epicarchitect.breakbadhabits.presentation.habits.HabitAppWidgetsViewModel
import epicarchitect.breakbadhabits.presentation.habits.HabitCreationViewModel
import epicarchitect.breakbadhabits.presentation.habits.HabitDetailsViewModel
import epicarchitect.breakbadhabits.presentation.habits.HabitTrackCreationViewModel
import epicarchitect.breakbadhabits.presentation.habits.HabitTrackUpdatingViewModel
import epicarchitect.breakbadhabits.presentation.habits.HabitTracksViewModel
import epicarchitect.breakbadhabits.presentation.habits.HabitUpdatingViewModel

interface HabitsPresentationModule {
    val habitCreationViewModel: HabitCreationViewModel
    fun habitUpdatingViewModel(habitId: Int): HabitUpdatingViewModel
    fun habitDetailsViewModel(habitId: Int): HabitDetailsViewModel

    fun habitTrackCreationViewModel(habitId: Int): HabitTrackCreationViewModel
    fun habitTracksViewModel(habitId: Int): HabitTracksViewModel
    fun habitTrackUpdatingViewModel(habitTrackId: Int): HabitTrackUpdatingViewModel

    val habitAppWidgetsViewModel: HabitAppWidgetsViewModel
    fun habitWidgetUpdatingViewModel(habitWidgetId: Int): HabitAppWidgetUpdatingViewModel
    fun habitWidgetCreationViewModel(widgetSystemId: Int): HabitAppWidgetCreationViewModel
}