package epicarchitect.breakbadhabits.export

import epicarchitect.breakbadhabits.habits.HabitsRules
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlin.uuid.Uuid

private val BackupJson = Json {
    ignoreUnknownKeys = true
    encodeDefaults = true
    prettyPrint = true
}

internal fun encodeBackup(backup: BackupFile): String = BackupJson.encodeToString(backup)

internal fun decodeBackup(text: String, fileName: String): BackupResult {
    val normalized = text.removePrefix("\uFEFF").trim()
    if (normalized.isEmpty() || normalized.length > MAX_BACKUP_BYTES) {
        return BackupResult.Failed(BackupFailure.InvalidFile)
    }

    return try {
        val root = BackupJson.parseToJsonElement(normalized).jsonObject
        val format = root["format"]?.jsonPrimitive?.contentOrNull
        if (format != BACKUP_FORMAT) {
            return BackupResult.Failed(BackupFailure.InvalidFile)
        }

        val version = root["formatVersion"]?.jsonPrimitive?.intOrNull
            ?: return BackupResult.Failed(BackupFailure.InvalidFile)
        when {
            version < 1 -> BackupResult.Failed(BackupFailure.InvalidFile)
            version > BACKUP_FORMAT_VERSION -> BackupResult.Failed(BackupFailure.NewerVersion)
            version < BACKUP_FORMAT_VERSION -> BackupResult.Failed(BackupFailure.InvalidFile)
            else -> {
                val backup = BackupJson.decodeFromJsonElement<BackupFile>(root)
                    .truncatedToRules()
                if (backup.isValid()) {
                    BackupResult.Ready(backup, fileName)
                } else {
                    BackupResult.Failed(BackupFailure.InvalidFile)
                }
            }
        }
    } catch (_: SerializationException) {
        BackupResult.Failed(BackupFailure.InvalidFile)
    } catch (_: IllegalArgumentException) {
        BackupResult.Failed(BackupFailure.InvalidFile)
    } catch (_: IllegalStateException) {
        BackupResult.Failed(BackupFailure.InvalidFile)
    }
}

private fun BackupFile.truncatedToRules(): BackupFile = copy(
    data = data.copy(
        habits = data.habits.map { habit ->
            habit.copy(
                name = habit.name.take(HabitsRules.maxHabitNameLength)
            )
        },
        events = data.events.map { event ->
            event.copy(
                comment = event.comment.take(HabitsRules.maxEventCommentLength)
            )
        }
    )
)

private fun BackupFile.isValid(): Boolean {
    if (format != BACKUP_FORMAT || formatVersion != BACKUP_FORMAT_VERSION) return false

    val habitIds = mutableSetOf<Uuid>()
    data.habits.forEach { habit ->
        if (habit.name.isBlank()) return false
        val habitId = runCatching { Uuid.parse(habit.id) }.getOrNull() ?: return false
        if (!habitIds.add(habitId)) return false
    }

    val eventIds = mutableSetOf<Uuid>()
    data.events.forEach { event ->
        val eventId = runCatching { Uuid.parse(event.id) }.getOrNull() ?: return false
        if (!eventIds.add(eventId)) return false

        val habitId = runCatching { Uuid.parse(event.habitId) }.getOrNull() ?: return false
        if (habitId !in habitIds) return false
        if (event.endTime < event.startTime) return false
        if (event.eventCount !in 1..HabitsRules.maxEventCount) return false
    }

    return true
}
