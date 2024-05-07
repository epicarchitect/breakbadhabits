package epicarchitect.breakbadhabits.entity.validator

import epicarchitect.breakbadhabits.data.MainDatabase
import epicarchitect.breakbadhabits.entity.habits.HabitsConfig

class HabitNewNameValidator(
    private val mainDatabase: MainDatabase,
    private val config: HabitsConfig
) {

    fun validate(
        data: String,
        initial: String? = null
    ) = data.incorrectReason(initial)?.let {
        IncorrectHabitNewName(data, it)
    } ?: CorrectHabitNewName(data)

    private fun String.incorrectReason(initial: String? = null) = when {
        initial == this -> null
        isEmpty() -> IncorrectHabitNewName.Reason.Empty
        length > config.maxHabitNameLength() -> IncorrectHabitNewName.Reason.TooLong(config.maxHabitNameLength())
        isAlreadyUsed() -> IncorrectHabitNewName.Reason.AlreadyUsed
        else -> null
    }

    private fun String.isAlreadyUsed() =
        mainDatabase.habitQueries.countWithName(this@isAlreadyUsed).executeAsOne() > 0
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