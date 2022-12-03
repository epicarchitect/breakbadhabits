package breakbadhabits.logic

import breakbadhabits.entity.Habit

class HabitNewNameValidator internal constructor(
    private val delegate: HabitCreatorModule.Delegate
) {

    suspend fun validate(data: Habit.Name) = data.incorrectReason()?.let {
        IncorrectHabitNewName(data, it)
    } ?: CorrectHabitNewNewName(data)

    private suspend fun Habit.Name.incorrectReason(): IncorrectHabitNewName.Reason? {
        val maxLength = delegate.getMaxHabitNameLength()
        return when {
            value.isEmpty() -> IncorrectHabitNewName.Reason.Empty()
            value.length > maxLength -> IncorrectHabitNewName.Reason.TooLong(maxLength)
            delegate.habitNameExists(this) -> IncorrectHabitNewName.Reason.AlreadyUsed()
            else -> null
        }
    }
}

sealed class ValidatedHabitNewName {
    abstract val data: Habit.Name
}

data class CorrectHabitNewNewName internal constructor(
    override val data: Habit.Name
) : ValidatedHabitNewName()

data class IncorrectHabitNewName internal constructor(
    override val data: Habit.Name,
    val reason: Reason
) : ValidatedHabitNewName() {
    sealed class Reason {
        class Empty : Reason()
        class AlreadyUsed : Reason()
        class TooLong(val maxLength: Int) : Reason()
    }
}