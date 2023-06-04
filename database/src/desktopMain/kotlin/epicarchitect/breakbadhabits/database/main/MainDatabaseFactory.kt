package epicarchitect.breakbadhabits.database.main

import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import epicarchitect.breakbadhabits.database.InstantAdapter
import epicarchitect.breakbadhabits.database.ListOfIntAdapter
import epicarchitect.breakbadhabits.sqldelight.main.Habit
import epicarchitect.breakbadhabits.sqldelight.main.HabitTrack
import epicarchitect.breakbadhabits.sqldelight.main.HabitWidget
import epicarchitect.breakbadhabits.sqldelight.main.MainDatabase

object MainDatabaseFactory {
    fun create(): MainDatabase {
        val driver = JdbcSqliteDriver("jdbc:sqlite:main")
        MainDatabase.Schema.create(driver)
        return MainDatabase(
            driver = driver,
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
}