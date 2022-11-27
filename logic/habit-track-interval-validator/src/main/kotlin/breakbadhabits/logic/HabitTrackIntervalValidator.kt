package breakbadhabits.logic

import breakbadhabits.entity.HabitTrack
import breakbadhabits.logic.dependecy.time.TimeProvider
import kolmachikhin.alexander.validation.Validator

class HabitTrackIntervalValidator(
    private val timeProvider: TimeProvider
) : Validator<HabitTrack.Interval, HabitTrackIntervalValidator.IncorrectReason>() {

    override suspend fun HabitTrack.Interval.incorrectReason() = when {
        timeProvider.getCurrentTime() < value.end -> IncorrectReason.BiggestThenCurrentTime()
        else -> null
    }

    sealed class IncorrectReason {
        class BiggestThenCurrentTime : IncorrectReason()
    }
}