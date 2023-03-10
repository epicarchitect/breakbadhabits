package breakbadhabits.app.logic.habits.validator

import breakbadhabits.app.database.AppDatabase
import breakbadhabits.app.entity.Habit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HabitNewNameValidator(
    private val appDatabase: AppDatabase,
    private val maxNameLength: Int
) {

    suspend fun validate(
        data: Habit.Name,
        initial: Habit.Name? = null
    ) = data.incorrectReason(initial)?.let {
        IncorrectHabitNewName(data, it)
    } ?: CorrectHabitNewName(data)

    private suspend fun Habit.Name.incorrectReason(initial: Habit.Name? = null) = when {
        initial == this -> null
        value.isEmpty() -> IncorrectHabitNewName.Reason.Empty()
        value.length > maxNameLength -> IncorrectHabitNewName.Reason.TooLong(maxNameLength)
        value.isAlreadyUsed() -> IncorrectHabitNewName.Reason.AlreadyUsed()
        else -> null
    }

    private suspend fun String.isAlreadyUsed() = withContext(Dispatchers.IO) {
        appDatabase.habitQueries.countWithName(this@isAlreadyUsed).executeAsOne() > 0
    }
}

sealed class ValidatedHabitNewName {
    abstract val data: Habit.Name
}

data class CorrectHabitNewName internal constructor(
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