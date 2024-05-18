package epicarchitect.breakbadhabits.data.database

import android.annotation.SuppressLint
import android.content.Context
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.android.AndroidSqliteDriver

@SuppressLint("StaticFieldLeak")
actual object SqlDriverFactory {
    lateinit var context: Context

    actual fun create(
        schema: SqlSchema<QueryResult.Value<Unit>>,
        databaseName: String
    ): SqlDriver = AndroidSqliteDriver(
        schema = schema,
        context = context,
        name = databaseName
    )
}