package epicarchitect.breakbadhabits.database.migrations

import androidx.room3.migration.Migration
import androidx.sqlite.SQLiteConnection

val MIGRATION_8_9 = object : Migration(8, 9) {
    override suspend fun migrate(connection: SQLiteConnection) {
        connection.exec(
            "CREATE TABLE IF NOT EXISTS `app_settings_new` (" +
                    "`id` INTEGER NOT NULL, " +
                    "`habitOrder` TEXT NOT NULL, " +
                    "PRIMARY KEY(`id`))"
        )
        connection.exec(
            "INSERT INTO `app_settings_new` (`id`, `habitOrder`) " +
                    "SELECT `id`, COALESCE(json_extract(`customization`, '$.habitOrder'), '[]') " +
                    "FROM `app_settings`"
        )
        connection.exec("DROP TABLE `app_settings`")
        connection.exec("ALTER TABLE `app_settings_new` RENAME TO `app_settings`")
    }
}

private fun SQLiteConnection.exec(sql: String) {
    prepare(sql).use { statement ->
        statement.step()
    }
}
