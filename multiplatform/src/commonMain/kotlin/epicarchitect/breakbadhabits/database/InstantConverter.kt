package epicarchitect.breakbadhabits.database

import androidx.room3.ColumnTypeConverter
import kotlin.time.Instant

class InstantConverter {
    @ColumnTypeConverter
    fun instantToEpochSeconds(value: Instant): Long = value.epochSeconds

    @ColumnTypeConverter
    fun epochSecondsToInstant(value: Long): Instant = Instant.fromEpochSeconds(value)
}
