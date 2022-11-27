package breakbadhabits.presentation

import breakbadhabits.entity.HabitAbstinence

data class FormattedHabitAbstinence(
    val abstinence: HabitAbstinence,
    val result: String
) {
    override fun toString() = result
}