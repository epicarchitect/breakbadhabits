package epicarchitect.breakbadhabits.logic.habits.validator

import epicarchitect.breakbadhabits.logic.datetime.provider.DateTimeProvider
import epicarchitect.breakbadhabits.foundation.datetime.InstantRange
import epicarchitect.breakbadhabits.foundation.datetime.ZonedDateTimeRange

class HabitTrackTimeValidator(
    private val dateTimeProvider: DateTimeProvider
) {
    fun validate(data: ZonedDateTimeRange) = data.incorrectReason()?.let {
        IncorrectHabitTrackTime(data, it)
    } ?: CorrectHabitTrackTime(data)

    private fun ZonedDateTimeRange.incorrectReason() = when {
        dateTimeProvider.getCurrentDateTime().let {
            it < start || it < endInclusive
        } -> IncorrectHabitTrackTime.Reason.BiggestThenCurrentTime
        else -> null
    }
}

sealed class ValidatedHabitTrackTime {
    abstract val data: ZonedDateTimeRange
}

data class CorrectHabitTrackTime internal constructor(
    override val data: ZonedDateTimeRange
) : ValidatedHabitTrackTime()

data class IncorrectHabitTrackTime internal constructor(
    override val data: ZonedDateTimeRange,
    val reason: Reason
) : ValidatedHabitTrackTime() {
    sealed class Reason {
        object BiggestThenCurrentTime : Reason()
    }
}