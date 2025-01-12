package epicarchitect.breakbadhabits.database

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema

interface PlatformSqlDriverFactory {
    fun create(
        schema: SqlSchema<QueryResult.Value<Unit>>,
        databaseName: String
    ): SqlDriver
}
