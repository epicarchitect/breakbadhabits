package epicarchitect.breakbadhabits.export

import epicarchitect.breakbadhabits.database.Habit
import epicarchitect.breakbadhabits.infrastructure.AppDatabase
import epicarchitect.breakbadhabits.infrastructure.DataExporter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Instant
import kotlin.uuid.Uuid

suspend fun exportHabitsCsv() {
    val timeZone = TimeZone.currentSystemDefault()
    val habits = AppDatabase.habitsDao.habits().first()
    val records = AppDatabase.habitEventRecordsDao.records()
    val csv = withContext(Dispatchers.Default) {
        buildHabitsCsv(
            habits = habits,
            eventRecords = records,
            timeZone = timeZone
        )
    }
    DataExporter.exportCsv(
        fileName = "breakbadhabits-${fileDate(timeZone)}.csv",
        content = ("\uFEFF" + csv).encodeToByteArray()
    )
}

suspend fun importHabitsCsv(csv: String): ImportResult {
    val timeZone = TimeZone.currentSystemDefault()
    val imported = withContext(Dispatchers.Default) {
        parseHabitsCsv(csv, timeZone)
    } ?: return ImportResult.InvalidFile

    return try {
        val database = AppDatabase
        val existingHabits = database.habitsDao.habits().first()
        val habitsByName = existingHabits.associateBy { it.name }.toMutableMap()
        val eventKeysByHabit = database.habitEventRecordsDao.records()
            .groupBy { it.habitId }
            .mapValues { (_, records) ->
                records.mapTo(mutableSetOf()) { record ->
                    EventKey(
                        startTime = record.startTime,
                        endTime = record.endTime,
                        eventCount = record.eventCount,
                        comment = record.comment
                    )
                }
            }
            .toMutableMap()

        var addedHabits = 0
        var addedEvents = 0

        imported.forEach { importedHabit ->
            val habit = habitsByName[importedHabit.name] ?: Habit(
                id = Uuid.random(),
                name = importedHabit.name,
                iconId = importedHabit.iconId
            ).also { newHabit ->
                database.habitsDao.insert(
                    id = newHabit.id,
                    name = newHabit.name,
                    iconId = newHabit.iconId
                )
                habitsByName[newHabit.name] = newHabit
                addedHabits++
            }

            val eventKeys = eventKeysByHabit.getOrPut(habit.id) { mutableSetOf() }
            importedHabit.records.forEach { record ->
                val key = EventKey(
                    startTime = record.startTime,
                    endTime = record.endTime,
                    eventCount = record.eventCount,
                    comment = record.comment
                )
                if (eventKeys.add(key)) {
                    database.habitEventRecordsDao.insert(
                        id = Uuid.random(),
                        habitId = habit.id,
                        startTime = record.startTime,
                        endTime = record.endTime,
                        eventCount = record.eventCount,
                        comment = record.comment
                    )
                    addedEvents++
                }
            }
        }

        ImportResult.Success(
            habits = addedHabits,
            events = addedEvents
        )
    } catch (_: Throwable) {
        ImportResult.Error
    }
}

private data class EventKey(
    val startTime: Instant,
    val endTime: Instant,
    val eventCount: Int,
    val comment: String
)

private fun fileDate(timeZone: TimeZone): String {
    val date = Clock.System.now().toLocalDateTime(timeZone).date
    return date.year.toString() +
            "-" + date.month.number.toString().padStart(2, '0') +
            "-" + date.day.toString().padStart(2, '0')
}
