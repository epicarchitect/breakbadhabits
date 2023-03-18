package breakbadhabits.app.logic.habits.validator

import breakbadhabits.app.entity.HabitTrack
import breakbadhabits.app.logic.datetime.provider.DateTimeProvider
import kotlinx.datetime.Instant

class HabitTrackRangeValidator(
    private val dateTimeProvider: DateTimeProvider
) {

    fun validate(data: HabitTrack.Range) = data.incorrectReason()?.let {
        IncorrectHabitTrackRange(data, it)
    } ?: CorrectHabitTrackRange(data)

    private fun HabitTrack.Range.incorrectReason() = when {
        dateTimeProvider.getCurrentTime() < value.endInclusive -> IncorrectHabitTrackRange.Reason.BiggestThenCurrentTime()
        else -> null
    }
}

sealed class ValidatedHabitTrackRange {
    abstract val data: HabitTrack.Range
}

data class CorrectHabitTrackRange internal constructor(
    override val data: HabitTrack.Range
) : ValidatedHabitTrackRange()

data class IncorrectHabitTrackRange internal constructor(
    override val data: HabitTrack.Range,
    val reason: Reason
) : ValidatedHabitTrackRange() {
    sealed class Reason {
        class BiggestThenCurrentTime : Reason()
    }
}