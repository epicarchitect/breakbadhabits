package breakbadhabits.app.logic.habits.validator

import breakbadhabits.app.database.AppDatabase
import breakbadhabits.app.logic.habits.provider.HabitsConfigProvider
import breakbadhabits.foundation.coroutines.CoroutineDispatchers
import kotlinx.coroutines.withContext

class HabitNewNameValidator(
    private val appDatabase: AppDatabase,
    private val configProvider: HabitsConfigProvider,
    private val coroutineDispatchers: CoroutineDispatchers
) {

    private val maxLength get() = configProvider.getConfig().maxHabitNameLength

    suspend fun validate(
        data: String,
        initial: String? = null
    ) = data.incorrectReason(initial)?.let {
        IncorrectHabitNewName(data, it)
    } ?: CorrectHabitNewName(data)

    private suspend fun String.incorrectReason(initial: String? = null) = when {
        initial == this -> null
        isEmpty() -> IncorrectHabitNewName.Reason.Empty
        length > maxLength -> IncorrectHabitNewName.Reason.TooLong(maxLength)
        isAlreadyUsed() -> IncorrectHabitNewName.Reason.AlreadyUsed
        else -> null
    }

    private suspend fun String.isAlreadyUsed() = withContext(coroutineDispatchers.io) {
        appDatabase.habitQueries.countWithName(this@isAlreadyUsed).executeAsOne() > 0
    }
}

sealed class ValidatedHabitNewName {
    abstract val data: String
}

data class CorrectHabitNewName internal constructor(
    override val data: String
) : ValidatedHabitNewName()

data class IncorrectHabitNewName internal constructor(
    override val data: String,
    val reason: Reason
) : ValidatedHabitNewName() {
    sealed class Reason {
        object Empty : Reason()
        object AlreadyUsed : Reason()
        class TooLong(val maxLength: Int) : Reason()
    }
}