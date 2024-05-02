package epicarchitect.breakbadhabits.entity.validator

class HabitTrackEventCountValidator {

    fun validate(data: Int) = data.incorrectReason()?.let {
        IncorrectHabitTrackEventCount(data, it)
    } ?: CorrectHabitTrackEventCount(data)

    private fun Int.incorrectReason() = when {
        this <= 0 -> IncorrectHabitTrackEventCount.Reason.Empty
        else -> null
    }
}

sealed class ValidatedHabitTrackEventCount {
    abstract val data: Int
}

data class CorrectHabitTrackEventCount internal constructor(
    override val data: Int
) : ValidatedHabitTrackEventCount()

data class IncorrectHabitTrackEventCount internal constructor(
    override val data: Int,
    val reason: Reason
) : ValidatedHabitTrackEventCount() {
    sealed class Reason {
        object Empty : Reason()
    }
}