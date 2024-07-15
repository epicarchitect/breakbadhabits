package epicarchitect.breakbadhabits.environment

import epicarchitect.breakbadhabits.environment.database.AppDatabase
import epicarchitect.breakbadhabits.environment.database.invoke
import epicarchitect.breakbadhabits.environment.datetime.ActualAppDateTime
import epicarchitect.breakbadhabits.environment.habits.HabitsConfig
import epicarchitect.breakbadhabits.environment.resources.AppResources
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

object Environment {
    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    val database = AppDatabase(name = "breakbadhabits.db")
    val resources = AppResources(coroutineScope, database)
    val dateTime = ActualAppDateTime(coroutineScope)
    val habitsConfig = HabitsConfig()
}