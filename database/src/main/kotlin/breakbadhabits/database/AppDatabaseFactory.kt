package breakbadhabits.database

import android.content.Context
import com.squareup.sqldelight.android.AndroidSqliteDriver

class AppDatabaseFactory {
    fun create(context: Context) = AppDatabase(
        AndroidSqliteDriver(
            schema = AppDatabase.Schema,
            context = context,
            name = "breakbadhabits-v4.db"
        )
    )
}
