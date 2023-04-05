package breakbadhabits.app.presentation.dashboard

import breakbadhabits.app.logic.habits.model.Habit
import breakbadhabits.app.logic.habits.model.HabitAbstinence

data class DashboardHabitItem(
    val habit: Habit,
    val abstinence: HabitAbstinence?
)