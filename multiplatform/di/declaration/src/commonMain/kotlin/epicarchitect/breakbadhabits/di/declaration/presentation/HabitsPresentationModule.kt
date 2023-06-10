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
    fun createHabitCreationViewModel(): HabitCreationViewModel
    fun createHabitUpdatingViewModel(habitId: Int): HabitUpdatingViewModel
    fun createHabitDetailsViewModel(habitId: Int): HabitDetailsViewModel
    fun createHabitTrackCreationViewModel(habitId: Int): HabitTrackCreationViewModel
    fun createHabitTracksViewModel(habitId: Int): HabitTracksViewModel
    fun createHabitTrackUpdatingViewModel(habitTrackId: Int): HabitTrackUpdatingViewModel
    fun createHabitAppWidgetsViewModel(): HabitAppWidgetsViewModel
    fun createHabitWidgetUpdatingViewModel(habitWidgetId: Int): HabitAppWidgetUpdatingViewModel
    fun createHabitWidgetCreationViewModel(widgetSystemId: Int): HabitAppWidgetCreationViewModel
}