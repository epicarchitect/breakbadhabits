package epicarchitect.breakbadhabits.entity.validator

import epicarchitect.breakbadhabits.data.AppData

class HabitNewNameValidation(
    private val input: String,
    private val initialInput: String? = null
) {

    fun incorrectReason(): IncorrectReason? {
        val maxLength = AppData.habitsConfig.maxHabitNameLength()
        return when {
            initialInput == input                                                 -> null
            input.isEmpty()                                                       -> IncorrectReason.Empty
            input.length > maxLength                                              -> IncorrectReason.TooLong(maxLength)
            AppData.database.habitQueries.countWithName(input).executeAsOne() > 0 -> IncorrectReason.AlreadyUsed
            else                                                                  -> null
        }
    }

    sealed class IncorrectReason {
        object Empty : IncorrectReason()
        object AlreadyUsed : IncorrectReason()
        class TooLong(val maxLength: Int) : IncorrectReason()
    }
}