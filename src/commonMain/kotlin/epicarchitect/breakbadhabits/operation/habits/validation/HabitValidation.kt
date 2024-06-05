package epicarchitect.breakbadhabits.operation.habits.validation

fun String.habitNewNameIncorrectReason(
    initialName: String? = null,
    maxLength: Int,
    nameIsExists: (String) -> Boolean
) = when {
    initialName == this -> null
    isEmpty()           -> HabitNewNameIncorrectReason.Empty
    length > maxLength  -> HabitNewNameIncorrectReason.TooLong(maxLength)
    nameIsExists(this)  -> HabitNewNameIncorrectReason.AlreadyUsed
    else                -> null
}

sealed interface HabitNewNameIncorrectReason {
    object Empty : HabitNewNameIncorrectReason
    object AlreadyUsed : HabitNewNameIncorrectReason
    class TooLong(val maxLength: Int) : HabitNewNameIncorrectReason
}