package epicarchitect.breakbadhabits.database

import androidx.room3.Room
import androidx.room3.RoomDatabase
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import epicarchitect.breakbadhabits.database.migrations.MIGRATION_1_2
import epicarchitect.breakbadhabits.database.migrations.MIGRATION_5_6
import epicarchitect.breakbadhabits.database.migrations.MIGRATION_6_7
import epicarchitect.breakbadhabits.database.migrations.MIGRATION_7_8
import epicarchitect.breakbadhabits.database.migrations.MIGRATION_8_9
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import platform.Foundation.NSApplicationSupportDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSFileProtectionComplete
import platform.Foundation.NSFileProtectionKey
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSUserDomainMask

class IosAppDatabaseFactory : AppDatabaseFactory {

    @OptIn(ExperimentalForeignApi::class)
    override fun create(databaseName: String): AppDatabase {
        val applicationSupportDirectory = NSSearchPathForDirectoriesInDomains(
            directory = NSApplicationSupportDirectory,
            domainMask = NSUserDomainMask,
            expandTilde = true
        ).first() as String
        val databasesDirectory = "$applicationSupportDirectory/databases"

        NSFileManager.defaultManager.createDirectoryAtPath(
            path = databasesDirectory,
            withIntermediateDirectories = true,
            attributes = null,
            error = null
        )

        val databaseFilePath = "$databasesDirectory/$databaseName"

        return Room.databaseBuilder<AppDatabase>(databaseFilePath)
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .addMigrations(
                MIGRATION_1_2,
                MIGRATION_5_6,
                MIGRATION_6_7,
                MIGRATION_7_8,
                MIGRATION_8_9
            )
            .fallbackToDestructiveMigration()
            .addCallback(object : RoomDatabase.Callback() {
                override suspend fun onOpen(connection: SQLiteConnection) {
                    protectDatabaseFiles(databaseFilePath)
                }
            })
            .build()
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun protectDatabaseFiles(databaseFilePath: String) {
    val fileManager = NSFileManager.defaultManager
    listOf(databaseFilePath, "$databaseFilePath-wal", "$databaseFilePath-shm").forEach { path ->
        if (fileManager.fileExistsAtPath(path)) {
            fileManager.setAttributes(
                attributes = mapOf(
                    NSFileProtectionKey to NSFileProtectionComplete
                ),
                ofItemAtPath = path,
                error = null
            )
        }
    }
}
