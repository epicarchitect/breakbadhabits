package breakbadhabits.app.logic.habits.config

import kotlinx.coroutines.flow.flowOf

class HabitsConfigProvider {

    fun configFlow() = flowOf(defaultConfig)

    fun getConfig() = defaultConfig

    companion object {
        private val defaultConfig = HabitsConfig(
            maxHabitNameLength = 30
        )
    }
}