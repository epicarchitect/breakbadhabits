package epicarchitect.breakbadhabits.logic.habits.validator

import epicarchitect.breakbadhabits.logic.datetime.provider.DateTimeProvider
import epicarchitect.breakbadhabits.logic.datetime.provider.getCurrentDateTime
import kotlinx.datetime.LocalDateTime

class HabitTrackDateTimeRangeValidator(
    private val dateTimeProvider: DateTimeProvider
) {
    fun validate(data: ClosedRange<LocalDateTime>) = data.incorrectReason()?.let {
        IncorrectHabitTrackDateTimeRange(data, it)
    } ?: CorrectHabitTrackDateTimeRange(data)

    private fun ClosedRange<LocalDateTime>.incorrectReason() = when {
        dateTimeProvider.getCurrentDateTime().let {
            it < start || it < endInclusive
        } -> IncorrectHabitTrackDateTimeRange.Reason.BiggestThenCurrentTime

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