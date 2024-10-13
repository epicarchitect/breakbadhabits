package epicarchitect.breakbadhabits.habits.validation

fun checkHabitEventCount(value: Int) = when {
    value <= 0 -> HabitEventCountError.Empty
    else -> null
}

sealed interface HabitEventCountError {
    object Empty : HabitEventCountError
}