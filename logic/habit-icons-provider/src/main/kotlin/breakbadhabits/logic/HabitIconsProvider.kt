package breakbadhabits.logic

import breakbadhabits.entity.Habit

class HabitIconsProvider {

    private val icons = List(28) {
        Habit.IconResource(it.toLong())
    }

    fun provide() = icons

}