package epicarchitect.breakbadhabits.screens.habits.eventRecords.editing

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import epicarchitect.breakbadhabits.database.Habit
import epicarchitect.breakbadhabits.database.HabitEventRecord
import epicarchitect.breakbadhabits.environment.LocalAppEnvironment
import epicarchitect.breakbadhabits.habits.HabitEventCountError
import epicarchitect.breakbadhabits.habits.HabitEventRecordTimeRangeError
import epicarchitect.breakbadhabits.habits.timeRange
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.datetime.Instant

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
    eventRecordId: Int?,
    habitId: Int
): HabitEventRecordEditingScreenState? {
    val environment = LocalAppEnvironment.current
    val habitQueries = environment.database.habitQueries
    val eventRecordQueries = environment.database.habitEventRecordQueries

    val habitState = remember {
        habitQueries.habitById(habitId)
            .asFlow()
            .mapToOneOrNull(Dispatchers.IO)
    }.collectAsState(null)

    val habit = habitState.value ?: return null
    val defaultTimeRange = environment.dateTime.currentInstant().let { it..it }

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
            eventRecordQueries.recordById(eventRecordId)
                .asFlow()
                .mapToOneOrNull(Dispatchers.IO)
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