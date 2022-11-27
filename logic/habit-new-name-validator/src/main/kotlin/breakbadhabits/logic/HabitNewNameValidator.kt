package breakbadhabits.logic

import breakbadhabits.entity.Habit
import breakbadhabits.logic.dependecy.repository.HabitsRepository
import kolmachikhin.alexander.validation.Validator

class HabitNewNameValidator(
    private val maxLength: Int,
    private val habitsRepository: HabitsRepository
) : Validator<Habit.Name, HabitNewNameValidator.IncorrectReason>() {

    override suspend fun Habit.Name.incorrectReason() = when {
        value.isEmpty() -> IncorrectReason.Empty()
        value.length > maxLength -> IncorrectReason.TooLong(maxLength)
        habitsRepository.habitNameExists(this) -> IncorrectReason.AlreadyUsed()
        else -> null
    }

    sealed class IncorrectReason {
        class Empty : IncorrectReason()
        class AlreadyUsed : IncorrectReason()
        class TooLong(val maxLength: Int) : IncorrectReason()
    }
}