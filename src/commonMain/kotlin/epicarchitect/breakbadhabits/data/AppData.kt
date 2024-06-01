package epicarchitect.breakbadhabits.data

import epicarchitect.breakbadhabits.data.config.HabitsConfig
import epicarchitect.breakbadhabits.data.database.invoke
import epicarchitect.breakbadhabits.data.datetime.AppDateTime
import epicarchitect.breakbadhabits.data.resources.AppResources
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

object AppData {
    val resources = AppResources()
    val database = AppDatabase("breakbadhabits.db")
    val dateTime = AppDateTime(CoroutineScope(Dispatchers.Default))
    val habitsConfig = HabitsConfig()
}