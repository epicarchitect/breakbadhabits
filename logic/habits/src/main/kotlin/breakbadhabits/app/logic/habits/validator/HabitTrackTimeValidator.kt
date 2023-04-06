package breakbadhabits.app.logic.habits.validator

import breakbadhabits.app.logic.datetime.provider.DateTimeProvider
import breakbadhabits.foundation.datetime.InstantRange

class HabitTrackTimeValidator(
    private val dateTimeProvider: DateTimeProvider
) {
    fun validate(data: InstantRange) = data.incorrectReason()?.let {
        IncorrectHabitTrackTime(data, it)
    } ?: CorrectHabitTrackTime(data)

    private fun InstantRange.incorrectReason() = when {
        dateTimeProvider.currentTime.value.let {
            it < start || it < endInclusive
        } -> IncorrectHabitTrackTime.Reason.BiggestThenCurrentTime
        else -> null
    }
}

sealed class ValidatedHabitTrackTime {
    abstract val data: InstantRange
}

data class CorrectHabitTrackTime internal constructor(
    override val data: InstantRange
) : ValidatedHabitTrackTime()

data class IncorrectHabitTrackTime internal constructor(
    override val data: InstantRange,
    val reason: Reason
) : ValidatedHabitTrackTime() {
    sealed class Reason {
        object BiggestThenCurrentTime : Reason()
    }
}