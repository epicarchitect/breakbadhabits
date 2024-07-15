package epicarchitect.breakbadhabits.environment.database

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import epicarchitect.breakbadhabits.BreakBadHabitsApp

actual fun createSqlDriverFactory(
    schema: SqlSchema<QueryResult.Value<Unit>>,
    databaseName: String
): SqlDriver = AndroidSqliteDriver(
    schema = schema,
    context = BreakBadHabitsApp.instance,
    name = databaseName
)
