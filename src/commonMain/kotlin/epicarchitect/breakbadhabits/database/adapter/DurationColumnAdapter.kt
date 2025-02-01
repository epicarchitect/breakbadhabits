package epicarchitect.breakbadhabits.database.adapter

import app.cash.sqldelight.ColumnAdapter
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

object DurationColumnAdapter : ColumnAdapter<Duration, Long> {
    override fun decode(databaseValue: Long): Duration {
        return databaseValue.milliseconds
    }

    override fun encode(value: Duration): Long {
        return value.inWholeMilliseconds
    }
}