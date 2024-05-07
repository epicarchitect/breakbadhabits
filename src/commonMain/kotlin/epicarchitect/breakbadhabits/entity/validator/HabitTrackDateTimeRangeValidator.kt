package epicarchitect.breakbadhabits.entity.validator

import epicarchitect.breakbadhabits.data.AppData
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toLocalDateTime

class HabitTrackDateTimeRangeValidator {
    fun validate(data: ClosedRange<LocalDateTime>) = data.incorrectReason()?.let {
        IncorrectHabitTrackDateTimeRange(data, it)
    } ?: CorrectHabitTrackDateTimeRange(data)

    private fun ClosedRange<LocalDateTime>.incorrectReason() = when {
        AppData.userDateTime.instant().toLocalDateTime(AppData.userDateTime.timeZone()).let {
            it < start || it < endInclusive
        }    -> IncorrectHabitTrackDateTimeRange.Reason.BiggestThenCurrentTime

        else -> null
    }
}

sealed class ValidatedHabitTrackDateTimeRange {
    abstract val data: ClosedRange<LocalDateTime>
}

data class CorrectHabitTrackDateTimeRange constructor(
    override val data: ClosedRange<LocalDateTime>
) : ValidatedHabitTrackDateTimeRange()

data class IncorrectHabitTrackDateTimeRange internal constructor(
    override val data: ClosedRange<LocalDateTime>,
    val reason: Reason
) : ValidatedHabitTrackDateTimeRange() {
    sealed class Reason {
        object BiggestThenCurrentTime : Reason()
    }
}