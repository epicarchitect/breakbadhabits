package epicarchitect.breakbadhabits.database

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.native.NativeSqliteDriver

class IosSqlDriverFactory : PlatformSqlDriverFactory {
    override fun create(
        schema: SqlSchema<QueryResult.Value<Unit>>,
        databaseName: String
    ) = NativeSqliteDriver(
        schema = schema,
        name = databaseName
    )
}