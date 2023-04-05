package breakbadhabits.app.database

import android.content.Context
import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
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
        )
    )
}