package breakbadhabits.app.database

import android.content.Context
import app.cash.sqldelight.driver.android.AndroidSqliteDriver

object AppDatabaseFactory {
    fun create(
        context: Context,
        name: String
    ) = AppDatabase(
        driver = AndroidSqliteDriver(
            schema = AppDatabase.Schema,
            context = context,
            name = name
        ),
        HabitAppWidgetConfigAdapter = HabitAppWidgetConfig.Adapter(
            habitIdsAdapter = ListOfLongsAdapter
        ),
        HabitTrackAdapter = HabitTrack.Adapter(
            startTimeAdapter = InstantAdapter,
            endTimeAdapter = InstantAdapter
        )
    )
}