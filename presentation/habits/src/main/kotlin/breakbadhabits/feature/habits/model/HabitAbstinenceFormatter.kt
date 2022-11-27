package breakbadhabits.feature.habits.model

import breakbadhabits.entity.HabitAbstinence

interface HabitAbstinenceFormatter {
    fun format(abstinence: HabitAbstinence): String
}