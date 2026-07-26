package epicarchitect.breakbadhabits.database

import app.cash.sqldelight.ColumnAdapter
import kotlin.time.Instant

object InstantAdapter : ColumnAdapter<Instant, Long> {
    override fun decode(databaseValue: Long) = Instant.fromEpochSeconds(databaseValue)

    override fun encode(value: Instant) = value.epochSeconds
}