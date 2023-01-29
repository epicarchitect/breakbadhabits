package breakbadhabits.app.logic.habit.creator

import breakbadhabits.app.entity.HabitTrack
import kotlinx.datetime.LocalDateTime

class HabitTrackIntervalValidator(
    private val getCurrentTime: () -> LocalDateTime
) {

    fun validate(data: HabitTrack.Range) = data.incorrectReason()?.let {
        IncorrectHabitTrackInterval(data, it)
    } ?: CorrectHabitTrackInterval(data)

    private fun HabitTrack.Range.incorrectReason() = when {
        getCurrentTime() < value.endInclusive -> IncorrectHabitTrackInterval.Reason.BiggestThenCurrentTime()
        else -> null
    }
}

sealed class ValidatedHabitTrackInterval {
    abstract val data: HabitTrack.Range
}

data class CorrectHabitTrackInterval internal constructor(
    override val data: HabitTrack.Range
) : ValidatedHabitTrackInterval()

data class IncorrectHabitTrackInterval internal constructor(
    override val data: HabitTrack.Range,
    val reason: Reason
) : ValidatedHabitTrackInterval() {
    sealed class Reason {
        class BiggestThenCurrentTime : Reason()
    }
}