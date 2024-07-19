package epicarchitect.breakbadhabits.environment

import epicarchitect.breakbadhabits.environment.database.AppDatabase
import epicarchitect.breakbadhabits.environment.database.invoke
import epicarchitect.breakbadhabits.environment.datetime.AppDateTime
import epicarchitect.breakbadhabits.environment.habits.HabitsEnvironment
import epicarchitect.breakbadhabits.environment.resources.AppResources
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

object Environment {
    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    val database = AppDatabase(name = "breakbadhabits.db")
    val resources = AppResources()
    val dateTime = AppDateTime()
    val habits = HabitsEnvironment(coroutineScope, dateTime)
}