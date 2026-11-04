package epicarchitect.breakbadhabits.database.migrations

import androidx.room3.migration.Migration
import androidx.sqlite.SQLiteConnection
import kotlin.uuid.Uuid

val MIGRATION_1_2 = object : Migration(1, 2) {
    override suspend fun migrate(connection: SQLiteConnection) {
        val habits = mutableListOf<LegacyHabit>()
        connection.prepare("SELECT id, name, iconId FROM Habit").use { statement ->
            while (statement.step()) {
                habits += LegacyHabit(
                    id = statement.getLong(0),
                    name = statement.getText(1),
                    iconId = statement.getLong(2)
                )
            }
        }

        val eventRecords = mutableListOf<LegacyHabitEventRecord>()
        connection.prepare(
            "SELECT id, habitId, startTime, endTime, eventCount, comment FROM HabitEventRecord"
        ).use { statement ->
            while (statement.step()) {
                eventRecords += LegacyHabitEventRecord(
                    id = statement.getLong(0),
                    habitId = statement.getLong(1),
                    startTime = statement.getLong(2),
                    endTime = statement.getLong(3),
                    eventCount = statement.getLong(4),
                    comment = statement.getText(5)
                )
            }
        }

        connection.exec("DROP TABLE IF EXISTS `HabitEventRecord`")
        connection.exec("DROP TABLE IF EXISTS `Habit`")
        connection.exec(CREATE_HABIT_TABLE)
        connection.exec(CREATE_HABIT_EVENT_RECORD_TABLE)
        connection.exec(CREATE_HABIT_EVENT_RECORD_INDEX)
        connection.exec(CREATE_PREMIUM_CACHE_TABLE)
        connection.exec(CREATE_HABIT_WIDGET_TABLE)

        val habitIds = mutableMapOf<Long, String>()
        connection.prepare(
            "INSERT INTO habits (id, name, iconId) VALUES (?, ?, ?)"
        ).use { statement ->
            habits.forEach { habit ->
                val newId = Uuid.generateV7().toString()
                habitIds[habit.id] = newId
                statement.bindText(1, newId)
                statement.bindText(2, habit.name)
                statement.bindLong(3, habit.iconId)
                statement.step()
                statement.reset()
            }
        }

        connection.prepare(
            "INSERT INTO habit_event_records (id, habitId, startTime, endTime, eventCount, comment) " +
                    "VALUES (?, ?, ?, ?, ?, ?)"
        ).use { statement ->
            eventRecords.forEach { eventRecord ->
                val newHabitId = habitIds[eventRecord.habitId] ?: return@forEach
                statement.bindText(1, Uuid.generateV7().toString())
                statement.bindText(2, newHabitId)
                statement.bindLong(3, eventRecord.startTime)
                statement.bindLong(4, eventRecord.endTime)
                statement.bindLong(5, eventRecord.eventCount)
                statement.bindText(6, eventRecord.comment)
                statement.step()
                statement.reset()
            }
        }
    }
}

private data class LegacyHabit(
    val id: Long,
    val name: String,
    val iconId: Long
)

private data class LegacyHabitEventRecord(
    val id: Long,
    val habitId: Long,
    val startTime: Long,
    val endTime: Long,
    val eventCount: Long,
    val comment: String
)

private const val CREATE_HABIT_TABLE = "CREATE TABLE IF NOT EXISTS `habits` " +
        "(`id` TEXT NOT NULL, `name` TEXT NOT NULL, `iconId` INTEGER NOT NULL, PRIMARY KEY(`id`))"

private const val CREATE_HABIT_EVENT_RECORD_TABLE =
    "CREATE TABLE IF NOT EXISTS `habit_event_records` " +
            "(`id` TEXT NOT NULL, `habitId` TEXT NOT NULL, `startTime` INTEGER NOT NULL, " +
            "`endTime` INTEGER NOT NULL, `eventCount` INTEGER NOT NULL, `comment` TEXT NOT NULL, " +
            "PRIMARY KEY(`id`), FOREIGN KEY(`habitId`) REFERENCES `habits`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE)"

private const val CREATE_HABIT_EVENT_RECORD_INDEX =
    "CREATE INDEX IF NOT EXISTS `index_habit_event_records_habitId_endTime` " +
            "ON `habit_event_records` (`habitId`, `endTime`)"

private const val CREATE_PREMIUM_CACHE_TABLE = "CREATE TABLE IF NOT EXISTS `premium_cache` " +
        "(`isPremium` INTEGER NOT NULL, `source` TEXT, `expiresAtEpochMs` INTEGER, " +
        "`id` INTEGER NOT NULL, PRIMARY KEY(`id`))"

private const val CREATE_HABIT_WIDGET_TABLE = "CREATE TABLE IF NOT EXISTS `habit_widgets` " +
        "(`title` TEXT NOT NULL, `systemId` INTEGER NOT NULL, `habitIds` TEXT NOT NULL, " +
        "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL)"

private fun SQLiteConnection.exec(sql: String) {
    prepare(sql).use { statement ->
        statement.step()
    }
}
