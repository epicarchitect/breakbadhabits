package breakbadhabits.logic

import breakbadhabits.entity.HabitTrack

class HabitTrackIntervalValidator internal constructor(
    private val delegate: HabitCreatorModule.Delegate
) {

    fun validate(data: HabitTrack.Interval) = data.incorrectReason()?.let {
        IncorrectHabitTrackInterval(data, it)
    } ?: CorrectHabitTrackInterval(data)

    private fun HabitTrack.Interval.incorrectReason() = when {
        delegate.getCurrentTime() < value.end -> IncorrectHabitTrackInterval.Reason.BiggestThenCurrentTime()
        else -> null
    }
}

sealed class ValidatedHabitTrackInterval {
    abstract val data: HabitTrack.Interval
}

data class CorrectHabitTrackInterval internal constructor(
    override val data: HabitTrack.Interval
) : ValidatedHabitTrackInterval()

data class IncorrectHabitTrackInterval internal constructor(
    override val data: HabitTrack.Interval,
    val reason: Reason
) : ValidatedHabitTrackInterval() {
    sealed class Reason {
        class BiggestThenCurrentTime : Reason()
    }
}