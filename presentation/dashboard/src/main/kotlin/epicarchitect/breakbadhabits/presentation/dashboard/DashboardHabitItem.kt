package epicarchitect.breakbadhabits.presentation.dashboard

import epicarchitect.breakbadhabits.logic.habits.model.Habit
import epicarchitect.breakbadhabits.logic.habits.model.HabitAbstinence

data class DashboardHabitItem(
    val habit: Habit,
    val abstinence: HabitAbstinence?
)