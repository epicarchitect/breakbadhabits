package breakbadhabits.app.logic.habits.creator

sealed class HabitCountability {
    class Countable(val averageDailyValue: Double) : HabitCountability()
    object Uncountable : HabitCountability()
}