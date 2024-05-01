package epicarchitect.breakbadhabits

class HabitsConfigProvider {

    fun getConfig() = defaultConfig

    companion object {
        private val defaultConfig = HabitsConfig(
            maxHabitNameLength = 30
        )
    }
}

data class HabitsConfig(
    val maxHabitNameLength: Int
)