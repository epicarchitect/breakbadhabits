package breakbadhabits.logic

import breakbadhabits.entity.HabitTrack

class HabitTrackIntervalValidator internal constructor(
    private val delegate: HabitCreatorModule.Delegate
) {

    fun validate(data: HabitTrack.Range) = data.incorrectReason()?.let {
        IncorrectHabitTrackInterval(data, it)
    } ?: CorrectHabitTrackInterval(data)

    private fun HabitTrack.Range.incorrectReason() = when {
        delegate.getCurrentTime() < value.endInclusive -> IncorrectHabitTrackInterval.Reason.BiggestThenCurrentTime()
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