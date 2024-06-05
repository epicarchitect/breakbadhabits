package epicarchitect.breakbadhabits.operation.habits.validation

fun Int.habitTrackEventCountIncorrectReason() = when {
    this <= 0 -> HabitTrackEventCountIncorrectReason.Empty
    else      -> null
}

sealed interface HabitTrackEventCountIncorrectReason {
    object Empty : HabitTrackEventCountIncorrectReason
}
