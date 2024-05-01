package epicarchitect.breakbadhabits.database

import app.cash.sqldelight.adapter.primitive.IntColumnAdapter

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