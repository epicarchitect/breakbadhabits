package epicarchitect.breakbadhabits.export

import epicarchitect.breakbadhabits.database.Habit
import epicarchitect.breakbadhabits.database.HabitEventRecord
import epicarchitect.breakbadhabits.habits.HabitIcons
import epicarchitect.breakbadhabits.habits.HabitsRules
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

private const val Separator = ';'
private const val LineSeparator = "\r\n"

private val Header = listOf(
    "habit",
    "icon",
    "start_time",
    "end_time",
    "event_count",
    "comment"
)

internal data class ImportedHabit(
    val name: String,
    val iconId: Int,
    val records: MutableList<ImportedEventRecord> = mutableListOf()
)

internal data class ImportedEventRecord(
    val startTime: Instant,
    val endTime: Instant,
    val eventCount: Int,
    val comment: String
)

fun buildHabitsCsv(
    habits: List<Habit>,
    eventRecords: List<HabitEventRecord>,
    timeZone: TimeZone
): String = buildString {
    appendRow(Header)

    habits.forEach { habit ->
        val habitRecords = eventRecords
            .filter { it.habitId == habit.id }
            .sortedBy { it.startTime }

        if (habitRecords.isEmpty()) {
            appendRow(
                listOf(
                    habit.name,
                    habit.iconId.toString(),
                    "",
                    "",
                    "",
                    ""
                )
            )
        } else {
            habitRecords.forEach { record ->
                appendRow(
                    listOf(
                        habit.name,
                        habit.iconId.toString(),
                        record.startTime.toLocalDateTime(timeZone).toCsvString(),
                        record.endTime.toLocalDateTime(timeZone).toCsvString(),
                        record.eventCount.toString(),
                        record.comment
                    )
                )
            }
        }
    }
}

internal fun parseHabitsCsv(
    csv: String,
    timeZone: TimeZone
): List<ImportedHabit>? {
    val text = csv.removePrefix("\uFEFF")
    if (text.isBlank()) return null

    val parsedRows = parseRows(
        text = text,
        separator = detectSeparator(text)
    ) ?: return null
    val rows = parsedRows.dropWhile { row ->
        row.size == 1 && row.first().trim().startsWith("sep=", ignoreCase = true)
    }

    val firstRow = rows.firstOrNull() ?: return null
    val columns = columns(firstRow) ?: return null
    val dataRows = if (columns.hasHeader) rows.drop(1) else rows
    val habits = linkedMapOf<String, ImportedHabit>()

    dataRows.forEach { row ->
        if (row.all { it.isBlank() }) return@forEach

        val name = row.value(columns.habit).trim()
        if (name.isEmpty()) return null

        val habitName = name.take(HabitsRules.maxHabitNameLength)
        val iconId = row.value(columns.icon)
            .toIntOrNull()
            ?.takeIf { id -> HabitIcons.items.any { it.id == id } }
            ?: 0
        val startValue = row.value(columns.start).trim()
        val endValue = row.value(columns.end).trim()
        val habit = habits.getOrPut(habitName) {
            ImportedHabit(name = habitName, iconId = iconId)
        }

        if (startValue.isEmpty() && endValue.isEmpty()) {
            return@forEach
        }

        if (startValue.isEmpty() || endValue.isEmpty()) return null

        val startTime = parseDateTime(startValue, timeZone) ?: return null
        val endTime = parseDateTime(endValue, timeZone) ?: return null
        if (endTime < startTime) return null

        val eventCount = row.value(columns.eventCount)
            .let { it.ifEmpty { "1" } }
            .toIntOrNull()
            ?: return null
        if (eventCount !in 1..HabitsRules.maxEventCount) return null

        habit.records.add(
            ImportedEventRecord(
                startTime = startTime,
                endTime = endTime,
                eventCount = eventCount,
                comment = row.value(columns.comment)
            )
        )
    }

    return habits.values.toList().takeIf { it.isNotEmpty() }
}

private data class CsvColumns(
    val hasHeader: Boolean,
    val habit: Int,
    val icon: Int?,
    val start: Int?,
    val end: Int?,
    val eventCount: Int?,
    val comment: Int?
)

private fun columns(firstRow: List<String>): CsvColumns? {
    val normalized = firstRow.map { it.trim().lowercase() }
    val hasHeader = normalized.contains("start_time") ||
            normalized.contains("end_time") ||
            normalized.contains("event_count") ||
            normalized.contains("icon") ||
            (normalized.firstOrNull() == "habit" && normalized.contains("comment"))

    if (hasHeader) {
        val habit = normalized.indexOf("habit")
        if (habit < 0) return null

        val start = normalized.indexOf("start_time").takeIf { it >= 0 }
        val end = normalized.indexOf("end_time").takeIf { it >= 0 }
        if ((start == null) != (end == null)) return null

        return CsvColumns(
            hasHeader = true,
            habit = habit,
            icon = normalized.indexOf("icon").takeIf { it >= 0 },
            start = start,
            end = end,
            eventCount = normalized.indexOf("event_count").takeIf { it >= 0 },
            comment = normalized.indexOf("comment").takeIf { it >= 0 }
        )
    }

    return when {
        firstRow.size >= 6 -> CsvColumns(
            hasHeader = false,
            habit = 0,
            icon = 1,
            start = 2,
            end = 3,
            eventCount = 4,
            comment = 5
        )

        firstRow.size == 5 -> CsvColumns(
            hasHeader = false,
            habit = 0,
            icon = null,
            start = 1,
            end = 2,
            eventCount = 3,
            comment = 4
        )

        firstRow.size == 2 -> CsvColumns(
            hasHeader = false,
            habit = 0,
            icon = 1,
            start = null,
            end = null,
            eventCount = null,
            comment = null
        )

        firstRow.size == 1 -> CsvColumns(
            hasHeader = false,
            habit = 0,
            icon = null,
            start = null,
            end = null,
            eventCount = null,
            comment = null
        )

        else -> null
    }
}

private fun List<String>.value(index: Int?): String =
    index?.let { getOrNull(it) }.orEmpty().trim()

private fun detectSeparator(text: String): Char {
    val firstLine = text.substringBefore('\n')
    val counts = mutableMapOf(';' to 0, ',' to 0, '\t' to 0)
    var inQuotes = false

    firstLine.forEach { char ->
        if (char == '"') {
            inQuotes = !inQuotes
        } else if (!inQuotes && counts.containsKey(char)) {
            counts[char] = counts.getValue(char) + 1
        }
    }

    return counts.maxByOrNull { it.value }
        ?.takeIf { it.value > 0 }
        ?.key
        ?: ';'
}

private fun parseRows(
    text: String,
    separator: Char
): List<List<String>>? {
    val rows = mutableListOf<List<String>>()
    val row = mutableListOf<String>()
    val field = StringBuilder()
    var inQuotes = false
    var index = 0

    fun finishField() {
        row.add(field.toString())
        field.clear()
    }

    fun finishRow() {
        finishField()
        if (row.any { it.isNotEmpty() }) {
            rows.add(row.toList())
        }
        row.clear()
    }

    while (index < text.length) {
        val char = text[index]

        if (inQuotes) {
            when {
                char == '"' && index + 1 < text.length && text[index + 1] == '"' -> {
                    field.append('"')
                    index++
                }

                char == '"' -> inQuotes = false

                char == '\r' && index + 1 < text.length && text[index + 1] == '\n' -> {
                    field.append('\n')
                    index++
                }

                else -> field.append(char)
            }
        } else {
            when (char) {
                '"' -> {
                    if (field.isNotEmpty()) return null
                    inQuotes = true
                }

                separator -> finishField()

                '\r' -> {
                    if (index + 1 < text.length && text[index + 1] == '\n') {
                        index++
                    }
                    finishRow()
                }

                '\n' -> finishRow()

                else -> field.append(char)
            }
        }

        index++
    }

    if (inQuotes) return null
    if (field.isNotEmpty() || row.isNotEmpty()) {
        finishRow()
    }

    return rows
}

private fun StringBuilder.appendRow(values: List<String>) {
    append(values.joinToString(Separator.toString()) { it.csvEscaped() })
    append(LineSeparator)
}

private fun String.csvEscaped(): String {
    val requiresEscaping = any {
        it == Separator || it == '"' || it == '\n' || it == '\r'
    }
    return if (requiresEscaping) {
        "\"" + replace("\"", "\"\"") + "\""
    } else {
        this
    }
}

private fun LocalDateTime.toCsvString(): String = buildString {
    append(year)
    append('-')
    append(month.number.pad2())
    append('-')
    append(day.pad2())
    append(' ')
    append(hour.pad2())
    append(':')
    append(minute.pad2())
    append(':')
    append(second.pad2())
}

private fun Int.pad2() = toString().padStart(2, '0')

private fun parseDateTime(value: String, timeZone: TimeZone): Instant? {
    val text = value.trim()
    if (text.isEmpty()) return null

    runCatching {
        LocalDateTime.parse(text.replace(' ', 'T'))
    }.getOrNull()?.let { return it.toInstant(timeZone) }

    runCatching {
        LocalDate.parse(text)
    }.getOrNull()?.let { date ->
        return LocalDateTime(date, LocalTime(0, 0)).toInstant(timeZone)
    }

    val parts = text.split(Regex("\\s+"), limit = 2)
    val dateParts = parts.first().split('.')
    if (dateParts.size != 3) return null

    val day = dateParts[0].toIntOrNull() ?: return null
    val month = dateParts[1].toIntOrNull() ?: return null
    val year = dateParts[2].toIntOrNull() ?: return null
    val date = runCatching { LocalDate(year, month, day) }.getOrNull() ?: return null
    val time = parts.getOrNull(1)
        ?.let { runCatching { LocalTime.parse(it) }.getOrNull() ?: return null }
        ?: LocalTime(0, 0)

    return LocalDateTime(date, time).toInstant(timeZone)
}
