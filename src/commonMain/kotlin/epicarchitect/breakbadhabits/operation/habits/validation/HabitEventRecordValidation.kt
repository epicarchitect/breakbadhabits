package epicarchitect.breakbadhabits.operation.habits.validation

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun Int.habitEventRecordDailyEventCountIncorrectReason() = when {
    this <= 0 -> HabitEventRecordDailyEventCountIncorrectReason.Empty
    else      -> null
}

sealed interface HabitEventRecordDailyEventCountIncorrectReason {
    object Empty : HabitEventRecordDailyEventCountIncorrectReason
}

fun ClosedRange<LocalDateTime>.habitEventRecordTimeRangeIncorrectReason(
    currentTime: Instant,
    timeZone: TimeZone
) = when {
    currentTime.toLocalDateTime(timeZone).let {
        it < start || it < endInclusive
    }    -> HabitEventRecordTimeRangeIncorrectReason.BiggestThenCurrentTime

    else -> null
}

sealed interface HabitEventRecordTimeRangeIncorrectReason {
    object BiggestThenCurrentTime : HabitEventRecordTimeRangeIncorrectReason
}