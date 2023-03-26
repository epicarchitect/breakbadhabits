package breakbadhabits.app.database

import app.cash.sqldelight.ColumnAdapter
import breakbadhabits.foundation.datetime.secondsToInstant
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

internal object TimeZoneAdapter : ColumnAdapter<TimeZone, String> {
    override fun decode(databaseValue: String): TimeZone {
        return TimeZone.of(databaseValue)
    }

    override fun encode(value: TimeZone): String {
        return value.id
    }
}