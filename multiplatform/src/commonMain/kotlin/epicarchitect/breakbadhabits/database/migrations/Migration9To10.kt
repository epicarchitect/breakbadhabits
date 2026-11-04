package epicarchitect.breakbadhabits.database.migrations

import androidx.room3.migration.Migration
import androidx.sqlite.SQLiteConnection

val MIGRATION_9_10 = object : Migration(9, 10) {
    override suspend fun migrate(connection: SQLiteConnection) {
        connection.exec("ALTER TABLE `premium_cache` ADD COLUMN `productId` TEXT")
        connection.exec("ALTER TABLE `premium_cache` ADD COLUMN `amountLabel` TEXT")
        connection.exec("ALTER TABLE `premium_cache` ADD COLUMN `priceMinorUnits` INTEGER")
        connection.exec("ALTER TABLE `premium_cache` ADD COLUMN `discountMinorUnits` INTEGER")
        connection.exec("ALTER TABLE `premium_cache` ADD COLUMN `currency` TEXT")
        connection.exec("ALTER TABLE `premium_cache` ADD COLUMN `sandbox` INTEGER NOT NULL DEFAULT 0")
        connection.exec("ALTER TABLE `premium_cache` ADD COLUMN `acknowledgementState` TEXT")
    }
}

private fun SQLiteConnection.exec(sql: String) {
    prepare(sql).use { statement ->
        statement.step()
    }
}
