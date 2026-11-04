package epicarchitect.breakbadhabits.database.migrations

import androidx.room3.migration.Migration
import androidx.sqlite.SQLiteConnection

val MIGRATION_5_6 = object : Migration(5, 6) {
    override suspend fun migrate(connection: SQLiteConnection) {
        connection.exec(
            "CREATE TABLE IF NOT EXISTS `habit_widgets_new` (" +
                    "`title` TEXT NOT NULL, " +
                    "`systemId` INTEGER NOT NULL, " +
                    "`habitIds` TEXT NOT NULL, " +
                    "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL)"
        )
        connection.exec(
            "INSERT INTO `habit_widgets_new` (`title`, `systemId`, `habitIds`, `id`) " +
                    "SELECT `title`, `systemId`, `habitIds`, `id` " +
                    "FROM `habit_widgets` " +
                    "WHERE `style` = 'classic' " +
                    "AND `id` IN (" +
                    "SELECT MAX(`id`) FROM `habit_widgets` " +
                    "WHERE `style` = 'classic' GROUP BY `systemId`" +
                    ")"
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
