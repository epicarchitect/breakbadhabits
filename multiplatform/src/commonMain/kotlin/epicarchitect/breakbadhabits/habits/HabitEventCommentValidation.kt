package epicarchitect.breakbadhabits.habits

fun checkHabitEventComment(
    comment: String,
    maxLength: Int
) = when {
    comment.length > maxLength -> HabitEventCommentError.TooLong(maxLength)
    else -> null
}

sealed interface HabitEventCommentError {
    data class TooLong(val maxLength: Int) : HabitEventCommentError
}

fun limitHabitEventComment(
    comment: String,
    maxLength: Int
): String = comment.take(maxLength)
