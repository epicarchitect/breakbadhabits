package breakbadhabits.app.database

import app.cash.sqldelight.ColumnAdapter
import kotlinx.datetime.TimeZone

internal object TimeZoneAdapter : ColumnAdapter<TimeZone, String> {
    override fun decode(databaseValue: String): TimeZone {
        return TimeZone.of(databaseValue)
    }

    override fun encode(value: TimeZone): String {
        return value.id
    }
}