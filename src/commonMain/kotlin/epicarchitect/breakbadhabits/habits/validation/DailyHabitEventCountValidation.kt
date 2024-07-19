package epicarchitect.breakbadhabits.habits.validation

fun checkDailyHabitEventCount(value: Int) = when {
    value <= 0 -> DailyHabitEventCountError.Empty
    else       -> null
}

sealed interface DailyHabitEventCountError {
    object Empty : DailyHabitEventCountError
}