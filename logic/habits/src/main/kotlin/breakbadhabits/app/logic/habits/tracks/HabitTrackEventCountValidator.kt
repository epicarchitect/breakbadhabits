package breakbadhabits.app.logic.habits.tracks

import breakbadhabits.app.logic.habits.entity.HabitTrack

class HabitTrackEventCountValidator {

    fun validate(data: HabitTrack.EventCount) = data.incorrectReason()?.let {
        IncorrectHabitTrackEventCount(data, it)
    } ?: CorrectHabitTrackEventCount(data)

    private fun HabitTrack.EventCount.incorrectReason() = when {
        dailyCount <= 0 -> IncorrectHabitTrackEventCount.Reason.Empty
        else -> null
    }
}

sealed class ValidatedHabitTrackEventCount {
    abstract val data: HabitTrack.EventCount
}

data class CorrectHabitTrackEventCount internal constructor(
    override val data: HabitTrack.EventCount
) : ValidatedHabitTrackEventCount()

data class IncorrectHabitTrackEventCount internal constructor(
    override val data: HabitTrack.EventCount,
    val reason: Reason
) : ValidatedHabitTrackEventCount() {
    sealed class Reason {
        object Empty : Reason()
    }
}