package breakbadhabits.app.logic.habits.validator

import breakbadhabits.app.entity.HabitTrack

class HabitTrackValueValidator {

    fun validate(data: HabitTrack.EventCount) = data.incorrectReason()?.let {
        IncorrectHabitTrackValue(data, it)
    } ?: CorrectHabitTrackValue(data)

    private fun HabitTrack.EventCount.incorrectReason() = when {
        value <= 0 -> IncorrectHabitTrackValue.Reason.Empty()
        else -> null
    }
}

sealed class ValidatedHabitTrackValue {
    abstract val data: HabitTrack.EventCount
}

data class CorrectHabitTrackValue internal constructor(
    override val data: HabitTrack.EventCount
) : ValidatedHabitTrackValue()

data class IncorrectHabitTrackValue internal constructor(
    override val data: HabitTrack.EventCount,
    val reason: Reason
) : ValidatedHabitTrackValue() {
    sealed class Reason {
        class Empty : Reason()
    }
}