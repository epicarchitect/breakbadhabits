package epicarchitect.breakbadhabits.screens.habits.eventRecords.editing

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import epicarchitect.breakbadhabits.database.Habit
import epicarchitect.breakbadhabits.database.HabitEventRecord
import epicarchitect.breakbadhabits.habits.HabitEventCountError
import epicarchitect.breakbadhabits.habits.HabitEventRecordTimeRangeError
import epicarchitect.breakbadhabits.habits.timeRange
import epicarchitect.breakbadhabits.infrastructure.AppDatabase
import kotlin.time.Clock
import kotlin.time.Instant
import kotlin.uuid.Uuid

class HabitEventRecordEditingScreenState(
    val initialRecord: HabitEventRecord?,
    val habit: Habit,
    defaultTimeRange: ClosedRange<Instant>,
) {
    var timeRange by mutableStateOf(initialRecord?.timeRange() ?: defaultTimeRange)
    var timeRangeError by mutableStateOf<HabitEventRecordTimeRangeError?>(null)
    var eventCount by mutableIntStateOf(initialRecord?.eventCount ?: 1)
    var eventCountError by mutableStateOf<HabitEventCountError?>(null)
    var comment by mutableStateOf(initialRecord?.comment ?: "")
    var dateTimeInputAsRange by mutableStateOf(timeRange.start != timeRange.endInclusive)
}

@Composable
fun rememberHabitEventRecordEditingState(
    eventRecordId: Uuid?,
    habitId: Uuid
): HabitEventRecordEditingScreenState? {
    val habitsDao = AppDatabase.habitsDao
    val habitEventRecordsDao = AppDatabase.habitEventRecordsDao

    val habitState = remember {
        habitsDao.habitById(habitId)
    }.collectAsState(null)

    val habit = habitState.value ?: return null
    val defaultTimeRange = Clock.System.now().let { it..it }

    return if (eventRecordId == null) {
        remember {
            HabitEventRecordEditingScreenState(
                initialRecord = null,
                habit = habit,
                defaultTimeRange = defaultTimeRange
            )
        }
    } else {
        val eventRecordState = remember {
            habitEventRecordsDao.recordById(eventRecordId)
        }.collectAsState(null)
        val eventRecord = eventRecordState.value ?: return null

        remember {
            HabitEventRecordEditingScreenState(
                initialRecord = eventRecord,
                habit = habit,
                defaultTimeRange = defaultTimeRange
            )
        }
    }
}