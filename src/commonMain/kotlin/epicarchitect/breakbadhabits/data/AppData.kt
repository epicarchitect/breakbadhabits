package epicarchitect.breakbadhabits.data

import epicarchitect.breakbadhabits.data.config.HabitsConfig
import epicarchitect.breakbadhabits.data.database.invoke
import epicarchitect.breakbadhabits.data.datetime.AppDateTime
import epicarchitect.breakbadhabits.data.resources.AppResources

object AppData {
    val resources = AppResources()
    val database = AppDatabase()
    val dateTime = AppDateTime()
    val habitsConfig = HabitsConfig()
}