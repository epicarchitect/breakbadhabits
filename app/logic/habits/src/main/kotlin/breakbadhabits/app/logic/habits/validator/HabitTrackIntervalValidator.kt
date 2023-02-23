package breakbadhabits.app.logic.habits.validator

import breakbadhabits.app.entity.HabitTrack
import kotlinx.datetime.LocalDateTime

class HabitTrackIntervalValidator(
    private val getCurrentTime: () -> LocalDateTime
) {

    fun validate(data: HabitTrack.Range) = data.incorrectReason()?.let {
        IncorrectHabitTrackRange(data, it)
    } ?: CorrectHabitTrackRange(data)

    private fun HabitTrack.Range.incorrectReason() = when {
        getCurrentTime() < value.endInclusive -> IncorrectHabitTrackRange.Reason.BiggestThenCurrentTime()
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