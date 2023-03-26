package breakbadhabits.app.database

import app.cash.sqldelight.ColumnAdapter
import breakbadhabits.foundation.datetime.secondsToInstant
import kotlinx.datetime.Instant
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

internal object InstantAdapter : ColumnAdapter<Instant, Long> {
    override fun decode(databaseValue: Long): Instant {
        return Instant.fromEpochSeconds(databaseValue)
    }

    override fun encode(value: Instant): Long {
        return value.epochSeconds
    }
}