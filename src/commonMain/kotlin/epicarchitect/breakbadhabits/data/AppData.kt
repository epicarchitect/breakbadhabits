package epicarchitect.breakbadhabits.data

import epicarchitect.breakbadhabits.data.database.invoke
import epicarchitect.breakbadhabits.data.resources.AppResources
import epicarchitect.breakbadhabits.entity.datetime.CachedDateTime
import epicarchitect.breakbadhabits.entity.datetime.SystemDateTime
import epicarchitect.breakbadhabits.entity.datetime.UpdatingDateTime
import epicarchitect.breakbadhabits.entity.datetime.ZeroMillisecondsDateTime
import epicarchitect.breakbadhabits.entity.habits.HabitsConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlin.time.Duration.Companion.seconds

object AppData {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    val resources = AppResources()

    val database = AppDatabase("breakbadhabits.db")

    val userDateTime = UpdatingDateTime(
        coroutineScope = coroutineScope,
        delay = { 1.seconds },
        value = {
            CachedDateTime(
                ZeroMillisecondsDateTime(
                    SystemDateTime()
                )
            )
        }
    )

    val habitsConfig = HabitsConfig()
}