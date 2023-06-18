package epicarchitect.breakbadhabits.logic.habits.provider

import epicarchitect.breakbadhabits.logic.habits.model.HabitsConfig

class HabitsConfigProvider {

    fun getConfig() = defaultConfig

    companion object {
        private val defaultConfig = HabitsConfig(
            maxHabitNameLength = 30
        )
    }
}
