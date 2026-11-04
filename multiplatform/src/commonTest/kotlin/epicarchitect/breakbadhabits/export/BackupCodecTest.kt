package epicarchitect.breakbadhabits.export

import epicarchitect.breakbadhabits.habits.HabitsRules
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.uuid.Uuid

class BackupCodecTest {

    private val habitId = Uuid.random()
    private val eventId = Uuid.random()

    @Test
    fun roundTripPreservesBackupData() {
        val backup = validBackup()

        val decoded = assertIs<BackupResult.Ready>(decodeBackup(encodeBackup(backup), ""))

        assertEquals(backup, decoded.backup)
    }

    @Test
    fun backupWithoutSettingsIsValid() {
        val backup = validBackup().copy(
            data = validBackup().data.copy(settings = null)
        )

        assertIs<BackupResult.Ready>(decodeBackup(encodeBackup(backup), ""))
    }

    @Test
    fun newerFormatVersionIsRejected() {
        val backup = validBackup().copy(formatVersion = BACKUP_FORMAT_VERSION + 1)

        val result = decodeBackup(encodeBackup(backup), "")

        val failed = assertIs<BackupResult.Failed>(result)
        assertEquals(BackupFailure.NewerVersion, failed.failure)
    }

    @Test
    fun wrongFormatIsRejected() {
        val backup = validBackup().copy(format = "other.format")

        val result = decodeBackup(encodeBackup(backup), "")

        val failed = assertIs<BackupResult.Failed>(result)
        assertEquals(BackupFailure.InvalidFile, failed.failure)
    }

    @Test
    fun missingVersionIsRejected() {
        val backup = validBackup()
        val json = encodeBackup(backup)
            .replace("\"formatVersion\": ${BACKUP_FORMAT_VERSION},", "")

        val result = decodeBackup(json, "")

        val failed = assertIs<BackupResult.Failed>(result)
        assertEquals(BackupFailure.InvalidFile, failed.failure)
    }

    @Test
    fun duplicateHabitIdsAreRejected() {
        val backup = validBackup()
        val duplicate = backup.data.habits.single()
        val invalid = backup.copy(
            data = backup.data.copy(habits = listOf(duplicate, duplicate))
        )

        val result = decodeBackup(encodeBackup(invalid), "")

        val failed = assertIs<BackupResult.Failed>(result)
        assertEquals(BackupFailure.InvalidFile, failed.failure)
    }

    @Test
    fun eventWithoutHabitIsRejected() {
        val backup = validBackup()
        val invalid = backup.copy(
            data = backup.data.copy(
                events = listOf(
                    backup.data.events.single().copy(
                        habitId = Uuid.random().toString()
                    )
                )
            )
        )

        val result = decodeBackup(encodeBackup(invalid), "")

        val failed = assertIs<BackupResult.Failed>(result)
        assertEquals(BackupFailure.InvalidFile, failed.failure)
    }

    @Test
    fun eventWithEndBeforeStartIsRejected() {
        val backup = validBackup()
        val invalid = backup.copy(
            data = backup.data.copy(
                events = listOf(
                    backup.data.events.single().copy(
                        startTime = 200,
                        endTime = 100
                    )
                )
            )
        )

        val result = decodeBackup(encodeBackup(invalid), "")

        val failed = assertIs<BackupResult.Failed>(result)
        assertEquals(BackupFailure.InvalidFile, failed.failure)
    }

    @Test
    fun unknownFieldsAreIgnored() {
        val json = encodeBackup(validBackup())
            .replace(
                "\"format\": \"$BACKUP_FORMAT\",",
                "\"format\": \"$BACKUP_FORMAT\",\n  \"futureField\": true,"
            )

        assertIs<BackupResult.Ready>(decodeBackup(json, ""))
    }

    @Test
    fun backupContainsFormatVersionAndDiscriminator() {
        val json = encodeBackup(validBackup())

        assertTrue(json.contains("\"format\": \"$BACKUP_FORMAT\""))
        assertTrue(json.contains("\"formatVersion\": $BACKUP_FORMAT_VERSION"))
    }

    @Test
    fun fileNameIsKeptForDisplay() {
        val result = assertIs<BackupResult.Ready>(
            decodeBackup(encodeBackup(validBackup()), "my-backup.json")
        )

        assertEquals("my-backup.json", result.fileName)
    }

    @Test
    fun oversizedHabitNameAndCommentAreTruncated() {
        val backup = validBackup()
        val longName = "n".repeat(HabitsRules.maxHabitNameLength + 10)
        val longComment = "c".repeat(HabitsRules.maxEventCommentLength + 10)
        val oversized = backup.copy(
            data = backup.data.copy(
                habits = backup.data.habits.map { it.copy(name = longName) },
                events = backup.data.events.map { it.copy(comment = longComment) }
            )
        )

        val result = assertIs<BackupResult.Ready>(decodeBackup(encodeBackup(oversized), ""))

        assertEquals(HabitsRules.maxHabitNameLength, result.backup.data.habits.single().name.length)
        assertEquals(
            HabitsRules.maxEventCommentLength,
            result.backup.data.events.single().comment.length
        )
    }

    @Test
    fun exportDateUsesTimeZoneFromBackup() {
        assertEquals(
            "26.11.2025, 12:40",
            backupExportDateText(1_764_150_000, "Europe/Moscow")
        )
        assertNull(backupExportDateText(0, "Europe/Moscow"))
    }

    private fun validBackup() = BackupFile(
        format = BACKUP_FORMAT,
        formatVersion = BACKUP_FORMAT_VERSION,
        appVersion = "5.0.0 (87)",
        exportedAt = 1_700_000_000,
        timeZone = "Europe/Moscow",
        data = BackupData(
            habits = listOf(
                BackupHabit(
                    id = habitId.toString(),
                    name = "Habit",
                    iconId = 3
                )
            ),
            events = listOf(
                BackupEvent(
                    id = eventId.toString(),
                    habitId = habitId.toString(),
                    startTime = 1_700_000_000,
                    endTime = 1_700_003_600,
                    eventCount = 2,
                    comment = "Comment"
                )
            ),
            settings = BackupSettings(
                habitOrder = listOf(habitId.toString())
            )
        )
    )
}
