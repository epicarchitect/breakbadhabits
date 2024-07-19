package epicarchitect.breakbadhabits.environment.database

import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import epicarchitect.breakbadhabits.database.AppSettings
import epicarchitect.breakbadhabits.database.Habit
import epicarchitect.breakbadhabits.database.HabitEventRecord
import epicarchitect.breakbadhabits.database.HabitWidget

operator fun AppDatabase.Companion.invoke(
    name: String
) = AppDatabase(
    driver = createSqlDriverFactory(
        schema = Schema,
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
    HabitEventRecordAdapter = HabitEventRecord.Adapter(
        idAdapter = IntColumnAdapter,
        habitIdAdapter = IntColumnAdapter,
        startTimeAdapter = InstantAdapter,
        endTimeAdapter = InstantAdapter,
        eventCountAdapter = IntColumnAdapter
    ),
    AppSettingsAdapter = AppSettings.Adapter(
        idAdapter = IntColumnAdapter,
        themeAdapter = AppSettingsThemeAdapter
    )
)