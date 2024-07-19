package epicarchitect.breakbadhabits

import epicarchitect.breakbadhabits.database.AppDatabase
import epicarchitect.breakbadhabits.database.invoke
import epicarchitect.breakbadhabits.datetime.AppDateTime
import epicarchitect.breakbadhabits.habits.HabitsTimePulse
import epicarchitect.breakbadhabits.resources.AppResources
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

object Environment {
    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    val database = AppDatabase(name = "breakbadhabits.db")
    val resources = AppResources()
    val dateTime = AppDateTime()
    val habitsRules = epicarchitect.breakbadhabits.habits.HabitsRules()
    val habitsTimePulse = HabitsTimePulse(coroutineScope, dateTime)
}