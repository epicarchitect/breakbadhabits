package breakbadhabits.app.logic.habit.creator

import breakbadhabits.app.database.AppDatabase
import breakbadhabits.app.entity.Habit

class HabitNewNameValidator(
    private val appDatabase: AppDatabase,
    private val maxNameLength: Int
) {

    suspend fun validate(data: Habit.Name) = data.incorrectReason()?.let {
        IncorrectHabitNewName(data, it)
    } ?: CorrectHabitNewNewName(data)

    private suspend fun Habit.Name.incorrectReason() = when {
        value.isEmpty() -> IncorrectHabitNewName.Reason.Empty()
        value.length > maxNameLength -> IncorrectHabitNewName.Reason.TooLong(maxNameLength)
        value.isAlreadyUsed() -> IncorrectHabitNewName.Reason.AlreadyUsed()
        else -> null
    }

    private fun String.isAlreadyUsed() =
        appDatabase.habitQueries.countWithName(this).executeAsOne() > 0
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