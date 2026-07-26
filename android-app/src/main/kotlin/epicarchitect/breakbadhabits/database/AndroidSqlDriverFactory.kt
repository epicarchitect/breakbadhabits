package epicarchitect.breakbadhabits.database

import android.content.Context
import androidx.sqlite.db.SupportSQLiteDatabase
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.android.AndroidSqliteDriver

class AndroidSqlDriverFactory(private val context: Context) : PlatformSqlDriverFactory {
    override fun create(
        schema: SqlSchema<QueryResult.Value<Unit>>,
        databaseName: String
    ) = AndroidSqliteDriver(
        schema = schema,
        context = context,
        name = databaseName,
        callback = object : AndroidSqliteDriver.Callback(schema) {
            override fun onConfigure(db: SupportSQLiteDatabase) {
                db.setForeignKeyConstraintsEnabled(true)
            }
        }
    )
}