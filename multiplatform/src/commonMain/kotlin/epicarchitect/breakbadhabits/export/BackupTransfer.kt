package epicarchitect.breakbadhabits.export

import epicarchitect.breakbadhabits.infrastructure.AppTimeZone
import androidx.room3.withReadTransaction
import androidx.room3.withWriteTransaction
import epicarchitect.breakbadhabits.database.AppSettings
import epicarchitect.breakbadhabits.infrastructure.AnalyticsOperator
import epicarchitect.breakbadhabits.infrastructure.AppDatabase
import epicarchitect.breakbadhabits.infrastructure.DataExporter
import epicarchitect.breakbadhabits.infrastructure.appVersion
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Instant
import kotlin.uuid.Uuid

internal suspend fun exportBackup() {
    val backup = AppDatabase.withReadTransaction {
        val habits = AppDatabase.habitsDao.all()
        val eventRecords = AppDatabase.habitEventRecordsDao.records()
        val settings = AppDatabase.appSettingsDao.get()

        BackupFile(
            format = BACKUP_FORMAT,
            formatVersion = BACKUP_FORMAT_VERSION,
            appVersion = appVersion,
            exportedAt = Clock.System.now().epochSeconds,
            timeZone = AppTimeZone.id,
            data = BackupData(
                habits = habits.map { habit ->
                    BackupHabit(
                        id = habit.id.toString(),
                        name = habit.name,
                        iconId = habit.iconId
                    )
                },
                events = eventRecords.map { record ->
                    BackupEvent(
                        id = record.id.toString(),
                        habitId = record.habitId.toString(),
                        startTime = record.startTime.epochSeconds,
                        endTime = record.endTime.epochSeconds,
                        eventCount = record.eventCount,
                        comment = record.comment
                    )
                },
                settings = settings?.let { currentSettings ->
                    BackupSettings(habitOrder = currentSettings.habitOrder)
                }
            )
        )
    }

    val content = withContext(Dispatchers.Default) {
        encodeBackup(backup).encodeToByteArray()
    }
    if (content.size > MAX_BACKUP_BYTES) {
        throw BackupTooLargeException()
    }
    DataExporter.exportBackup(
        fileName = "breakbadhabits-backup-${fileDate(AppTimeZone)}.json",
        content = content
    )
}

internal suspend fun restoreBackup(backup: BackupFile): BackupResult {
    return try {
        AppDatabase.withWriteTransaction {
            replaceBackup(backup)
        }
    } catch (cancellation: CancellationException) {
        throw cancellation
    } catch (exception: Exception) {
        AnalyticsOperator.logError(exception)
        BackupResult.Failed(BackupFailure.Error)
    }
}

private suspend fun replaceBackup(backup: BackupFile): BackupResult {
    val habitIds = backup.data.habits.mapTo(mutableSetOf()) { it.id }

    AppDatabase.habitEventRecordsDao.deleteAll()
    AppDatabase.habitsDao.deleteAll()
    AppDatabase.habitWidgetDao.deleteAll()
    AppDatabase.appSettingsDao.deleteAll()

    backup.data.habits.forEach { habit ->
        insertHabit(habit)
    }
    backup.data.events.forEach { event ->
        insertEvent(event)
    }
    backup.data.settings?.let { settings ->
        saveSettings(settings, habitIds)
    }

    return BackupResult.Restored
}

private suspend fun insertHabit(habit: BackupHabit) {
    AppDatabase.habitsDao.insert(
        id = Uuid.parse(habit.id),
        name = habit.name,
        iconId = habit.iconId
    )
}

private suspend fun insertEvent(event: BackupEvent) {
    AppDatabase.habitEventRecordsDao.insert(
        id = Uuid.parse(event.id),
        habitId = Uuid.parse(event.habitId),
        startTime = Instant.fromEpochSeconds(event.startTime),
        endTime = Instant.fromEpochSeconds(event.endTime),
        eventCount = event.eventCount,
        comment = event.comment
    )
}

private suspend fun saveSettings(
    settings: BackupSettings,
    knownHabitIds: Set<String>
) {
    AppDatabase.appSettingsDao.save(
        AppSettings(
            id = 0,
            habitOrder = settings.habitOrder
                .filter { it in knownHabitIds }
                .distinct()
        )
    )
}

private fun fileDate(timeZone: TimeZone): String {
    val date = Clock.System.now().toLocalDateTime(timeZone).date
    return date.year.toString() +
            "-" + date.month.number.toString().padStart(2, '0') +
            "-" + date.day.toString().padStart(2, '0')
}
