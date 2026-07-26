package epicarchitect.breakbadhabits.habits

fun checkHabitNewName(
    newName: String,
    initialName: String? = null,
    maxLength: Int,
    nameIsExists: (String) -> Boolean
) = when {
    initialName == newName -> null
    newName.isEmpty() -> HabitNewNameError.Empty
    newName.length > maxLength -> HabitNewNameError.TooLong(maxLength)
    nameIsExists(newName) -> HabitNewNameError.AlreadyUsed
    else -> null
}

sealed interface HabitNewNameError {
    data object Empty : HabitNewNameError
    data object AlreadyUsed : HabitNewNameError
    data class TooLong(val maxLength: Int) : HabitNewNameError
}