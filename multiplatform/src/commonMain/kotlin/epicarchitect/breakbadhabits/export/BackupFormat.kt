package epicarchitect.breakbadhabits.export

import epicarchitect.breakbadhabits.infrastructure.AppTimeZone
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import kotlin.time.Instant

internal const val BACKUP_FORMAT = "breakbadhabits.backup"
internal const val BACKUP_FORMAT_VERSION = 1
internal const val MAX_BACKUP_BYTES = 20 * 1024 * 1024

internal class BackupTooLargeException : Exception()

@Serializable
internal data class BackupFile(
    val format: String,
    val formatVersion: Int,
    val appVersion: String = "",
    val exportedAt: Long = 0,
    val timeZone: String = "",
    val data: BackupData
)

@Serializable
internal data class BackupData(
    val habits: List<BackupHabit> = emptyList(),
    val events: List<BackupEvent> = emptyList(),
    val settings: BackupSettings? = null
)

@Serializable
internal data class BackupHabit(
    val id: String,
    val name: String,
    val iconId: Int
)

@Serializable
internal data class BackupEvent(
    val id: String,
    val habitId: String,
    val startTime: Long,
    val endTime: Long,
    val eventCount: Int,
    val comment: String
)

@Serializable
internal data class BackupSettings(
    val habitOrder: List<String> = emptyList()
)

internal sealed interface BackupResult {
    data class Ready(
        val backup: BackupFile,
        val fileName: String
    ) : BackupResult

    data object Restored : BackupResult

    data class Failed(val failure: BackupFailure) : BackupResult
}

internal enum class BackupFailure {
    InvalidFile,
    NewerVersion,
    Error
}

internal fun backupExportDateText(exportedAt: Long, timeZoneId: String): String? {
    if (exportedAt <= 0) return null

    val timeZone = timeZoneId
        .takeIf { it.isNotBlank() }
        ?.let { runCatching { TimeZone.of(it) }.getOrNull() }
        ?: AppTimeZone

    return runCatching {
        val dateTime = Instant.fromEpochSeconds(exportedAt).toLocalDateTime(timeZone)
        dateTime.day.toString().padStart(2, '0') +
                "." + dateTime.month.number.toString().padStart(2, '0') +
                "." + dateTime.year +
                ", " + dateTime.hour.toString().padStart(2, '0') +
                ":" + dateTime.minute.toString().padStart(2, '0')
    }.getOrNull()
}
