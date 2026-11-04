package epicarchitect.breakbadhabits.database

import android.content.Context
import androidx.room3.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import epicarchitect.breakbadhabits.database.migrations.MIGRATION_1_2
import kotlinx.coroutines.Dispatchers

class AndroidAppDatabaseFactory(
    private val context: Context
) : AppDatabaseFactory {
    override fun create(databaseName: String) =
        Room.databaseBuilder<AppDatabase>(context, databaseName)
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .addMigrations(MIGRATION_1_2)
            .fallbackToDestructiveMigration()
            .build()
}
