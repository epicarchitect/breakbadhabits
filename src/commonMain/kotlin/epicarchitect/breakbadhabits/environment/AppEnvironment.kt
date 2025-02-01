package epicarchitect.breakbadhabits.environment

import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import epicarchitect.breakbadhabits.database.AppDatabase
import epicarchitect.breakbadhabits.database.adapter.DurationColumnAdapter
import epicarchitect.breakbadhabits.database.Habit
import epicarchitect.breakbadhabits.database.HabitEventRecord
import epicarchitect.breakbadhabits.database.adapter.InstantColumnAdapter
import epicarchitect.breakbadhabits.database.PlatformSqlDriverFactory
import epicarchitect.breakbadhabits.datetime.AppDateTime
import epicarchitect.breakbadhabits.format.AppFormat
import epicarchitect.breakbadhabits.format.PlatformDateTimeFormatter
import epicarchitect.breakbadhabits.habits.HabitsEnvironment
import epicarchitect.breakbadhabits.habits.gamification.HabitLevels
import epicarchitect.breakbadhabits.language.PlatformLanguageProvider
import epicarchitect.breakbadhabits.resources.AppResources
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class AppEnvironment(
    platformSqlDriverFactory: PlatformSqlDriverFactory,
    platformLanguageProvider: PlatformLanguageProvider,
    platformDateTimeFormatter: PlatformDateTimeFormatter
) {
    val database = AppDatabase(
        driver = platformSqlDriverFactory.create(
            schema = AppDatabase.Schema,
            databaseName = "breakbadhabits.db"
        ),
        HabitAdapter = Habit.Adapter(
            idAdapter = IntColumnAdapter,
            iconIdAdapter = IntColumnAdapter,
            levelAdapter = IntColumnAdapter,
            earnedCoinsFromPreviousLevelAdapter = IntColumnAdapter,
            abstinenceWhenLevelUpgradedAdapter = DurationColumnAdapter
        ),
        HabitEventRecordAdapter = HabitEventRecord.Adapter(
            idAdapter = IntColumnAdapter,
            habitIdAdapter = IntColumnAdapter,
            startTimeAdapter = InstantColumnAdapter,
            endTimeAdapter = InstantColumnAdapter,
            eventCountAdapter = IntColumnAdapter
        )
    )
    val dateTime = AppDateTime()
    val habits = HabitsEnvironment(
        coroutineScope = CoroutineScope(Dispatchers.Default),
        dateTime = dateTime
    )
    val resources = AppResources(platformLanguageProvider)
    val format = AppFormat(
        resources = resources,
        platformDateTimeFormatter = platformDateTimeFormatter
    )
    val habitLevels = HabitLevels(
        maxHabitLevel = 100,
        magicCoefficient = 1.5791829
    )
}