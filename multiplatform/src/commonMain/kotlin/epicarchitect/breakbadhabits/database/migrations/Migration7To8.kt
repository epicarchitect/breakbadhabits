package epicarchitect.breakbadhabits.database.migrations

import androidx.room3.migration.Migration
import androidx.sqlite.SQLiteConnection

val MIGRATION_7_8 = object : Migration(7, 8) {
    override suspend fun migrate(connection: SQLiteConnection) {
        connection.exec(
            "CREATE TABLE IF NOT EXISTS `habit_widgets_new` (" +
                    "`systemId` INTEGER NOT NULL, " +
                    "`habitIds` TEXT NOT NULL, " +
                    "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL)"
        )
        connection.exec(
            "INSERT INTO `habit_widgets_new` (`systemId`, `habitIds`, `id`) " +
                    "SELECT `systemId`, `habitIds`, `id` FROM `habit_widgets`"
        )
        connection.exec("DROP TABLE `habit_widgets`")
        connection.exec("ALTER TABLE `habit_widgets_new` RENAME TO `habit_widgets`")
        connection.exec(
            "CREATE UNIQUE INDEX IF NOT EXISTS `index_habit_widgets_systemId` " +
                    "ON `habit_widgets` (`systemId`)"
        )
    }
}

private fun SQLiteConnection.exec(sql: String) {
    prepare(sql).use { statement ->
        statement.step()
    }
}
