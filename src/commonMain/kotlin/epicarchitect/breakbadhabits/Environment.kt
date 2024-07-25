package epicarchitect.breakbadhabits

import epicarchitect.breakbadhabits.database.AppDatabase
import epicarchitect.breakbadhabits.database.invoke
import epicarchitect.breakbadhabits.datetime.AppDateTime
import epicarchitect.breakbadhabits.habits.HabitsRules
import epicarchitect.breakbadhabits.habits.HabitsTimePulse
import epicarchitect.breakbadhabits.habits.gamification.HabitLevels
import epicarchitect.breakbadhabits.resources.AppResources
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

//private const val MAGIC_COEFFICIENT = 1.5791829
//private const val MAGIC_COEFFICIENT = 2.0
private const val MAGIC_COEFFICIENT = 1.42867
private const val MAX_HABIT_LEVEL = 100
private const val DATABASE_NAME = "breakbadhabits.db"

object Environment {
    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    val database = AppDatabase(name = DATABASE_NAME)
    val resources = AppResources()
    val dateTime = AppDateTime()
    val habitsRules = HabitsRules()
    val habitsTimePulse = HabitsTimePulse(coroutineScope, dateTime)
    val habitLevels = HabitLevels(
        maxHabitLevel = MAX_HABIT_LEVEL,
        magicCoefficient = MAGIC_COEFFICIENT
    )
}