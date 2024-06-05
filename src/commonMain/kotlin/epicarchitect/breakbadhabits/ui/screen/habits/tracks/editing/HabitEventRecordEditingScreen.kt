package epicarchitect.breakbadhabits.ui.screen.habits.tracks.editing

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import epicarchitect.breakbadhabits.data.AppData
import epicarchitect.breakbadhabits.data.Habit
import epicarchitect.breakbadhabits.data.HabitEventRecord
import epicarchitect.breakbadhabits.operation.datetime.toLocalDateTimeRange
import epicarchitect.breakbadhabits.operation.habits.dailyEventCount
import epicarchitect.breakbadhabits.operation.habits.timeRange
import epicarchitect.breakbadhabits.operation.habits.totalHabitEventRecordEventCountByDaily
import epicarchitect.breakbadhabits.operation.habits.validation.HabitEventRecordDailyEventCountIncorrectReason
import epicarchitect.breakbadhabits.operation.habits.validation.HabitEventRecordTimeRangeIncorrectReason
import epicarchitect.breakbadhabits.operation.habits.validation.habitEventRecordDailyEventCountIncorrectReason
import epicarchitect.breakbadhabits.operation.habits.validation.habitEventRecordTimeRangeIncorrectReason
import epicarchitect.breakbadhabits.ui.component.Dialog
import epicarchitect.breakbadhabits.ui.component.FlowStateContainer
import epicarchitect.breakbadhabits.ui.component.SimpleTopAppBar
import epicarchitect.breakbadhabits.ui.component.button.Button
import epicarchitect.breakbadhabits.ui.component.calendar.RangeSelectionCalendarDialog
import epicarchitect.breakbadhabits.ui.component.calendar.rememberSelectionCalendarState
import epicarchitect.breakbadhabits.ui.component.regex.Regexps
import epicarchitect.breakbadhabits.ui.component.stateOfOneOrNull
import epicarchitect.breakbadhabits.ui.component.text.ErrorText
import epicarchitect.breakbadhabits.ui.component.text.Text
import epicarchitect.breakbadhabits.ui.component.text.TextField
import epicarchitect.breakbadhabits.ui.format.formatted
import epicarchitect.calendar.compose.basis.EpicMonth
import epicarchitect.calendar.compose.basis.addYears
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

fun dateSelectionMonthRange(timeZone: TimeZone) = EpicMonth.now(timeZone).let {
    it.addYears(-10).copy(month = Month.JANUARY)..it.copy(month = Month.DECEMBER)
}

class HabitEventRecordEditingScreen(private val HabitEventRecordId: Int) : Screen {
    @Composable
    override fun Content() {
        HabitEventRecordEditing(HabitEventRecordId)
    }
}

@Composable
fun HabitEventRecordEditing(HabitEventRecordId: Int) {
    FlowStateContainer(
        state = stateOfOneOrNull {
            AppData.database.habitEventRecordQueries.recordById(HabitEventRecordId)
        }
    ) { habitEventRecord ->
        if (habitEventRecord != null) {
            FlowStateContainer(
                state = stateOfOneOrNull(habitEventRecord) {
                    AppData.database.habitQueries.habitById(habitEventRecord.habitId)
                }
            ) { habit ->
                if (habit != null) {
                    Loaded(habit, habitEventRecord)
                }
            }
        }
    }
}

@Composable
private fun Loaded(
    habit: Habit,
    habitTrack: HabitEventRecord,
) {
    val strings = AppData.resources.strings.habitEventRecordEditingStrings
    val habitEventRecordQueries = AppData.database.habitEventRecordQueries
    val navigator = LocalNavigator.currentOrThrow

    var rangeSelectionShow by rememberSaveable { mutableStateOf(false) }
    val timeZone by AppData.dateTime.currentTimeZoneState.collectAsState()

    var selectedDateTimeRange by remember(habitTrack) {
        mutableStateOf(habitTrack.timeRange.toLocalDateTimeRange(timeZone))
    }
    var timeRangeIncorrectReason by remember(selectedDateTimeRange) {
        mutableStateOf<HabitEventRecordTimeRangeIncorrectReason?>(null)
    }

    var dailyEventCount by rememberSaveable(habitTrack) {
        mutableIntStateOf(habitTrack.dailyEventCount(timeZone))
    }
    var dailyEventCountIncorrectReason by remember {
        mutableStateOf<HabitEventRecordDailyEventCountIncorrectReason?>(null)
    }
    var comment by rememberSaveable(habitTrack) {
        mutableStateOf(habitTrack.comment)
    }

    if (rangeSelectionShow) {
        val state = rememberSelectionCalendarState(
            initialSelectedDateTime = selectedDateTimeRange,
            monthRange = dateSelectionMonthRange(timeZone)
        )

        RangeSelectionCalendarDialog(
            state = state,
            onCancel = {
                rangeSelectionShow = false
            },
            onConfirm = {
                rangeSelectionShow = false
                selectedDateTimeRange = it
            }
        )
    }

    var deletionShow by remember { mutableStateOf(false) }
    if (deletionShow) {
        Dialog(
            onDismiss = {
                deletionShow = false
            }
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = strings.deleteConfirmation(),
                    type = Text.Type.Description,
                    priority = Text.Priority.High
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Button(
                        text = strings.cancel(),
                        onClick = {
                            deletionShow = false
                        }
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Button(
                        text = strings.yes(),
                        type = Button.Type.Main,
                        onClick = {
                            habitEventRecordQueries.deleteById(habitTrack.id)
                            navigator.pop()
                        }
                    )
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        SimpleTopAppBar(
            title = strings.titleText(habit.name),
            onBackClick = navigator::pop
        )

        Spacer(Modifier.height(12.dp))

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = strings.dailyEventCountDescription()
        )

        Spacer(Modifier.height(12.dp))

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            value = dailyEventCount.toString(),
            onValueChange = {
                dailyEventCount = it.toIntOrNull() ?: 0
                dailyEventCountIncorrectReason = null
            },
            label = strings.dailyEventCountLabel(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            regex = Regexps.integersOrEmpty(maxCharCount = 4),
            error = dailyEventCountIncorrectReason?.let(strings::dailyEventCountError),
        )

        Spacer(Modifier.height(12.dp))

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = strings.timeRangeDescription()
        )

        Spacer(Modifier.height(12.dp))

        Button(
            modifier = Modifier.padding(horizontal = 16.dp),
            onClick = {
                rangeSelectionShow = true
            },
            text = selectedDateTimeRange.formatted()
        )

        Spacer(Modifier.height(12.dp))

        timeRangeIncorrectReason?.let {
            ErrorText(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = strings.timeRangeError(it)
            )
        }

        Spacer(Modifier.height(24.dp))

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = strings.commentDescription()
        )

        Spacer(Modifier.height(12.dp))

        TextField(
            modifier = Modifier.padding(horizontal = 16.dp),
            value = comment,
            onValueChange = {
                comment = it
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = strings.deleteDescription()
        )

        Button(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = strings.deleteButton(),
            type = Button.Type.Dangerous,
            onClick = {
                deletionShow = true
            }
        )

        Spacer(modifier = Modifier.weight(1.0f))

        Spacer(modifier = Modifier.height(48.dp))

        Text(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .align(Alignment.End),
            text = strings.finishDescription()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.End),
            text = strings.finishButton(),
            type = Button.Type.Main,
            onClick = {
                dailyEventCountIncorrectReason = dailyEventCount.habitEventRecordDailyEventCountIncorrectReason()
                if (dailyEventCountIncorrectReason != null) return@Button

                timeRangeIncorrectReason = selectedDateTimeRange.habitEventRecordTimeRangeIncorrectReason(
                    currentTime = AppData.dateTime.currentTimeState.value,
                    timeZone = timeZone
                )
                if (timeRangeIncorrectReason != null) return@Button

                val startTime = selectedDateTimeRange.start.toInstant(timeZone)
                val endTime = selectedDateTimeRange.endInclusive.toInstant(timeZone)

                habitEventRecordQueries.update(
                    id = habitTrack.id,
                    startTime = startTime,
                    endTime = endTime,
                    eventCount = totalHabitEventRecordEventCountByDaily(dailyEventCount, startTime, endTime, timeZone),
                    comment = comment
                )
                navigator.pop()
            }
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}