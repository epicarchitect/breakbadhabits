package breakbadhabits.app.presentation.dashboard

import breakbadhabits.app.entity.Habit
import breakbadhabits.app.entity.HabitAbstinence

data class DashboardHabitItem(
    val habit: Habit,
    val abstinence: HabitAbstinence?
)