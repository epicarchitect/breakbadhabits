package breakbadhabits.android.app.validator

class HabitValidator(
    private val habitNameExists: suspend (String) -> Boolean,
    val maxHabitNameLength: Int
) {

    fun nameNotEmpty(habitName: String) = habitName.isNotEmpty()

    fun nameNotLongerThenMaxNameLength(habitName: String) = habitName.length <= maxHabitNameLength

    suspend fun nameNotUsed(habitName: String) = !habitNameExists(habitName)

}