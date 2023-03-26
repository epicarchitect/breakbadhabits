package breakbadhabits.app.presentation.dashboard

import breakbadhabits.app.logic.habits.entity.Habit
import breakbadhabits.app.logic.habits.entity.HabitAbstinence

data class DashboardHabitItem(
    val habit: Habit,
    val abstinence: HabitAbstinence?
)