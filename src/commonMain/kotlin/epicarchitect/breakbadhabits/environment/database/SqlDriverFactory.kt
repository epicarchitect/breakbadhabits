package epicarchitect.breakbadhabits.environment.database

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema

expect fun createSqlDriverFactory(
    schema: SqlSchema<QueryResult.Value<Unit>>,
    databaseName: String
): SqlDriver
