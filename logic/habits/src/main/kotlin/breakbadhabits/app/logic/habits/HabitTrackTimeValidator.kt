package breakbadhabits.app.logic.habits

import breakbadhabits.app.logic.datetime.provider.DateTimeProvider
import kotlinx.datetime.Instant

class HabitTrackTimeValidator(
    private val dateTimeProvider: DateTimeProvider
) {

    fun validate(data: ClosedRange<Instant>) = data.incorrectReason()?.let {
        IncorrectHabitTrackTime(data, it)
    } ?: CorrectHabitTrackTime(data)

    private fun ClosedRange<Instant>.incorrectReason() = when {
        dateTimeProvider.currentTime.value.let {
            it < start || it < endInclusive
        } -> IncorrectHabitTrackTime.Reason.BiggestThenCurrentTime
        else -> null
    }
}

sealed class ValidatedHabitTrackTime {
    abstract val data: ClosedRange<Instant>
}

data class CorrectHabitTrackTime internal constructor(
    override val data: ClosedRange<Instant>
) : ValidatedHabitTrackTime()

data class IncorrectHabitTrackTime internal constructor(
    override val data: ClosedRange<Instant>,
    val reason: Reason
) : ValidatedHabitTrackTime() {
    sealed class Reason {
        object BiggestThenCurrentTime : Reason()
    }
}