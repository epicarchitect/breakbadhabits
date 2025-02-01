package epicarchitect.breakbadhabits.database

import app.cash.sqldelight.adapter.primitive.IntColumnAdapter

operator fun AppDatabase.Companion.invoke(
    name: String
) = AppDatabase(
    driver = createSqlDriverFactory(
        schema = Schema,
        databaseName = name
    ),
    HabitAdapter = Habit.Adapter(
        idAdapter = IntColumnAdapter,
        levelAdapter = IntColumnAdapter,
        abstinenceWhenLevelUpgradedAdapter = DurationAdapter
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