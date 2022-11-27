package breakbadhabits.logic.dependecy.repository

import breakbadhabits.entity.Habit

interface HabitIconsRepository {
    fun getHabitIcons(): List<Habit.IconResource>
}