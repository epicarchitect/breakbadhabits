package breakbadhabits.app.logic.habits

import breakbadhabits.app.entity.Habit

class HabitIconProvider {

    private val icons = List(28) {
        Habit.Icon(it.toLong())
    }

    fun provide() = icons

}