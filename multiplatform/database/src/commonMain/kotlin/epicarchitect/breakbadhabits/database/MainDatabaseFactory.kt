package epicarchitect.breakbadhabits.database

import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import epicarchitect.breakbadhabits.sqldelight.main.AppSettings
import epicarchitect.breakbadhabits.sqldelight.main.Habit
import epicarchitect.breakbadhabits.sqldelight.main.HabitTrack
import epicarchitect.breakbadhabits.sqldelight.main.HabitWidget
import epicarchitect.breakbadhabits.sqldelight.main.MainDatabase

operator fun MainDatabase.Companion.invoke(databaseName: String) = MainDatabase(
    driver = SqlDriverFactory.create(
        schema = Schema,
        databaseName = databaseName
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