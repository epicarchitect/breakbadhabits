package epicarchitect.breakbadhabits.database.main

import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import epicarchitect.breakbadhabits.foundation.sqldelight.InstantAdapter
import epicarchitect.breakbadhabits.foundation.sqldelight.ListOfIntAdapter
import epicarchitect.breakbadhabits.foundation.sqldelight.SqlDriverFactory
import epicarchitect.breakbadhabits.sqldelight.main.AppSettings
import epicarchitect.breakbadhabits.sqldelight.main.Habit
import epicarchitect.breakbadhabits.sqldelight.main.HabitTrack
import epicarchitect.breakbadhabits.sqldelight.main.HabitWidget
import epicarchitect.breakbadhabits.sqldelight.main.MainDatabase

class MainDatabaseFactory(
    private val sqlDriverFactory: SqlDriverFactory
) {
    fun create(name: String) = MainDatabase(
        driver = sqlDriverFactory.create(
            schema = MainDatabase.Schema,
            databaseName = name
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
}