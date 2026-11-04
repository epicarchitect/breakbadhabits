package epicarchitect.breakbadhabits.database.migrations

import androidx.room3.migration.Migration
import androidx.sqlite.SQLiteConnection

val MIGRATION_6_7 = object : Migration(6, 7) {
    override suspend fun migrate(connection: SQLiteConnection) {
        connection.exec(
            "CREATE TABLE IF NOT EXISTS `premium_cache_new` (" +
                    "`source` TEXT NOT NULL, " +
                    "`expiresAt` INTEGER NOT NULL, " +
                    "`id` INTEGER NOT NULL, " +
                    "PRIMARY KEY(`id`))"
        )
        connection.exec(
            "INSERT INTO `premium_cache_new` (`source`, `expiresAt`, `id`) " +
                    "SELECT COALESCE(`source`, 'unknown'), " +
                    "COALESCE(`expiresAtEpochMs` / 1000, 253402300799), " +
                    "`id` " +
                    "FROM `premium_cache` " +
                    "WHERE `isPremium` = 1"
        )
        connection.exec("DROP TABLE `premium_cache`")
        connection.exec("ALTER TABLE `premium_cache_new` RENAME TO `premium_cache`")
    }
}

private fun SQLiteConnection.exec(sql: String) {
    prepare(sql).use { statement ->
        statement.step()
    }
}
