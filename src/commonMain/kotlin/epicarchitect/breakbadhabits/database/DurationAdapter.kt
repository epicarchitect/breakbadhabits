package epicarchitect.breakbadhabits.database

import app.cash.sqldelight.ColumnAdapter
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

object DurationAdapter : ColumnAdapter<Duration, Long> {
    override fun decode(databaseValue: Long): Duration {
        return databaseValue.milliseconds
    }

    override fun encode(value: Duration): Long {
        return value.inWholeMilliseconds
    }
}