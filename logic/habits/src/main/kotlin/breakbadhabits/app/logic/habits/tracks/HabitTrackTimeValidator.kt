package breakbadhabits.app.logic.habits.tracks

import breakbadhabits.app.entity.HabitTrack
import breakbadhabits.app.logic.datetime.DateTimeProvider

class HabitTrackTimeValidator(
    private val dateTimeProvider: DateTimeProvider
) {

    fun validate(data: HabitTrack.Time) = data.incorrectReason()?.let {
        IncorrectHabitTrackTime(data, it)
    } ?: CorrectHabitTrackTime(data)

    private fun HabitTrack.Time.incorrectReason() = when {
        dateTimeProvider.getCurrentTime().let {
            it < start || it < endInclusive
        }  -> IncorrectHabitTrackTime.Reason.BiggestThenCurrentTime
        else -> null
    }
}

sealed class ValidatedHabitTrackTime {
    abstract val data: HabitTrack.Time
}

data class CorrectHabitTrackTime internal constructor(
    override val data: HabitTrack.Time
) : ValidatedHabitTrackTime()

data class IncorrectHabitTrackTime internal constructor(
    override val data: HabitTrack.Time,
    val reason: Reason
) : ValidatedHabitTrackTime() {
    sealed class Reason {
        object BiggestThenCurrentTime : Reason()
    }
}