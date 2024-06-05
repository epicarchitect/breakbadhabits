package epicarchitect.breakbadhabits.operation.habits.validation

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