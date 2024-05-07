package epicarchitect.breakbadhabits.data

import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import epicarchitect.breakbadhabits.data.datetime.SystemDateTime
import epicarchitect.breakbadhabits.data.datetime.UpdatingDateTime
import epicarchitect.breakbadhabits.entity.datetime.CachedDateTime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlin.time.Duration.Companion.seconds

object AppData {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    val database = MainDatabase(
        driver = SqlDriverFactory.create(
            schema = MainDatabase.Schema,
            databaseName = "breakbadhabits-main.db"
        ),
        HabitAdapter = Habit.Adapter(
            idAdapter = IntColumnAdapter,
            iconIdAdapter = IntColumnAdapter
        ),
        HabitWidgetAdapter = HabitWidget.Adapter(
            idAdapter = IntColumnAdapter,
            systemIdAdapter = IntColumnAdapter,
            habitIdsAdapter = ListOfIntAdapter
        ),
        HabitTrackAdapter = HabitTrack.Adapter(
            idAdapter = IntColumnAdapter,
            habitIdAdapter = IntColumnAdapter,
            startTimeAdapter = InstantAdapter,
            endTimeAdapter = InstantAdapter,
            eventCountAdapter = IntColumnAdapter
        ),
        AppSettingsAdapter = AppSettings.Adapter(
            idAdapter = IntColumnAdapter
        )
    )

    val userDateTime = UpdatingDateTime(
        scope = coroutineScope,
        delay = { 1.seconds },
        value = { CachedDateTime(SystemDateTime()) }
    )
}