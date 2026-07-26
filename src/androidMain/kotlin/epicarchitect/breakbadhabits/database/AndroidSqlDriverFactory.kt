package epicarchitect.breakbadhabits.database

import android.app.Application
import androidx.sqlite.db.SupportSQLiteDatabase
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.android.AndroidSqliteDriver

class AndroidSqlDriverFactory(
    private val application: Application
) : PlatformSqlDriverFactory {
    override fun create(
        schema: SqlSchema<QueryResult.Value<Unit>>,
        name: String
    ) = AndroidSqliteDriver(
        schema = schema,
        context = application,
        name = name,
        callback = object : AndroidSqliteDriver.Callback(schema) {
            override fun onConfigure(db: SupportSQLiteDatabase) {
                db.setForeignKeyConstraintsEnabled(true)
            }
        }
    )
}