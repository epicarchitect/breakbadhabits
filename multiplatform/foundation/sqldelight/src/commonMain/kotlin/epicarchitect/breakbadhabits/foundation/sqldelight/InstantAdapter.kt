package epicarchitect.breakbadhabits.foundation.sqldelight

import app.cash.sqldelight.ColumnAdapter
import kotlinx.datetime.Instant

object InstantAdapter : ColumnAdapter<Instant, Long> {
    override fun decode(databaseValue: Long): Instant {
        return Instant.fromEpochSeconds(databaseValue)
    }

    override fun encode(value: Instant): Long {
        return value.epochSeconds
    }
}