package epicarchitect.breakbadhabits.database.main

import android.content.Context
import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import epicarchitect.breakbadhabits.database.InstantAdapter
import epicarchitect.breakbadhabits.database.ListOfIntAdapter
import epicarchitect.breakbadhabits.sqldelight.main.Habit
import epicarchitect.breakbadhabits.sqldelight.main.HabitTrack
import epicarchitect.breakbadhabits.sqldelight.main.HabitWidget
import epicarchitect.breakbadhabits.sqldelight.main.MainDatabase

object MainDatabaseFactory {
    fun create(
        context: Context,
        name: String
    ) = MainDatabase(
        driver = AndroidSqliteDriver(
            schema = MainDatabase.Schema,
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