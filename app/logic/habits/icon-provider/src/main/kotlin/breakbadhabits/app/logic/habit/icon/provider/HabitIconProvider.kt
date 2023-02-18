package breakbadhabits.app.logic.habit.icon.provider

import breakbadhabits.app.entity.Habit

class HabitIconProvider {

    private val icons = List(28) {
        Habit.IconResource(it.toLong())
    }

    fun provide() = icons

}