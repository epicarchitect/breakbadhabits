package epicarchitect.breakbadhabits.operation.habits

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun ClosedRange<LocalDateTime>.habitTrackTimeRangeIncorrectReason(
    currentTime: Instant,
    timeZone: TimeZone
) = when {
    currentTime.toLocalDateTime(timeZone).let {
        it < start || it < endInclusive
    }    -> HabitTrackTimeRangeIncorrectReason.BiggestThenCurrentTime

    else -> null
}

sealed interface HabitTrackTimeRangeIncorrectReason {
    object BiggestThenCurrentTime : HabitTrackTimeRangeIncorrectReason
}


fun habitNewNameIncorrectReason(
    input: String,
    initialInput: String? = null,
    maxLength: Int,
    nameIsExists: (String) -> Boolean
) = when {
    initialInput == input    -> null
    input.isEmpty()          -> HabitNewNameIncorrectReason.Empty
    input.length > maxLength -> HabitNewNameIncorrectReason.TooLong(maxLength)
    nameIsExists(input)  -> HabitNewNameIncorrectReason.AlreadyUsed
    else                     -> null
}

sealed interface HabitNewNameIncorrectReason {
    object Empty : HabitNewNameIncorrectReason
    object AlreadyUsed : HabitNewNameIncorrectReason
    class TooLong(val maxLength: Int) : HabitNewNameIncorrectReason
}


fun habitTrackEventCountIncorrectReason(
    eventCount: Int
) = when {
    eventCount <= 0 -> HabitTrackEventCountIncorrectReason.Empty
    else            -> null
}

sealed interface HabitTrackEventCountIncorrectReason {
    object Empty : HabitTrackEventCountIncorrectReason
}
