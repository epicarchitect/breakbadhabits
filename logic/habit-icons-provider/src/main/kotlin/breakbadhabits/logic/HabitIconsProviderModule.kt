package breakbadhabits.logic

import breakbadhabits.entity.Habit

class HabitIconsProviderModule(private val delegate: Delegate) {

    fun createHabitIconsProvider() = HabitIconsProvider(delegate)

    interface Delegate {
        fun getHabitIcons(): List<Habit.IconResource>
    }
}