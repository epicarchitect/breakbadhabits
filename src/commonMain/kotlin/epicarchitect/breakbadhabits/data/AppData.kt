package epicarchitect.breakbadhabits.data

import androidx.compose.ui.text.intl.Locale
import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import epicarchitect.breakbadhabits.data.database.InstantAdapter
import epicarchitect.breakbadhabits.data.database.ListOfIntAdapter
import epicarchitect.breakbadhabits.data.database.SqlDriverFactory
import epicarchitect.breakbadhabits.data.database.appSettings.AppThemeAdapter
import epicarchitect.breakbadhabits.data.resources.AppResources
import epicarchitect.breakbadhabits.entity.datetime.CachedDateTime
import epicarchitect.breakbadhabits.entity.datetime.SystemDateTime
import epicarchitect.breakbadhabits.entity.datetime.UpdatingDateTime
import epicarchitect.breakbadhabits.entity.datetime.ZeroMillisecondsDateTime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.time.Duration.Companion.seconds

object AppData {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    val database = AppDatabase(
        driver = SqlDriverFactory.create(
            schema = AppDatabase.Schema,
            databaseName = "breakbadhabits.db"
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
            idAdapter = IntColumnAdapter,
            themeAdapter = AppThemeAdapter
        )
    )

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

    // TODO: update by locale
    val resources: StateFlow<AppResources> = MutableStateFlow(AppResources(Locale.current))

}