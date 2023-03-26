package breakbadhabits.app.logic.habits

import breakbadhabits.app.database.AppDatabase
import breakbadhabits.app.logic.habits.config.HabitsConfigProvider
import breakbadhabits.app.logic.habits.entity.Habit
import breakbadhabits.foundation.coroutines.CoroutineDispatchers
import kotlinx.coroutines.withContext

class HabitNewNameValidator(
    private val appDatabase: AppDatabase,
    private val configProvider: HabitsConfigProvider,
    private val coroutineDispatchers: CoroutineDispatchers
) {

    private val maxLength get() = configProvider.getConfig().maxHabitNameLength

    suspend fun validate(
        data: Habit.Name,
        initial: Habit.Name? = null
    ) = data.incorrectReason(initial)?.let {
        IncorrectHabitNewName(data, it)
    } ?: CorrectHabitNewName(data)

    private suspend fun Habit.Name.incorrectReason(initial: Habit.Name? = null) = when {
        initial == this -> null
        value.isEmpty() -> IncorrectHabitNewName.Reason.Empty
        value.length > maxLength -> IncorrectHabitNewName.Reason.TooLong(maxLength)
        value.isAlreadyUsed() -> IncorrectHabitNewName.Reason.AlreadyUsed
        else -> null
    }

    private suspend fun String.isAlreadyUsed() = withContext(coroutineDispatchers.io) {
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
        object Empty : Reason()
        object AlreadyUsed : Reason()
        class TooLong(val maxLength: Int) : Reason()
    }
}