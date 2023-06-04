package epicarchitect.breakbadhabits.database

import android.content.Context
import app.cash.sqldelight.db.QueryResult.Value
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.android.AndroidSqliteDriver

actual class DriverFactory(private val context: Context) {
    actual fun createDriver(
        schema: SqlSchema<Value<Unit>>,
        databaseName: String
    ): SqlDriver = AndroidSqliteDriver(
        schema = schema,
        context = context,
        name = databaseName
    )
}