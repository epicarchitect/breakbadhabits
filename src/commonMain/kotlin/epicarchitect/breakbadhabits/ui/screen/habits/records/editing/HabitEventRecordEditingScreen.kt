package epicarchitect.breakbadhabits.ui.screen.habits.records.editing

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
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
import epicarchitect.breakbadhabits.data.HabitEventRecord
import epicarchitect.breakbadhabits.operation.datetime.toInstantRange
import epicarchitect.breakbadhabits.operation.datetime.toLocalDateTimeRange
import epicarchitect.breakbadhabits.operation.habits.dailyEventCount
import epicarchitect.breakbadhabits.operation.habits.timeRange
import epicarchitect.breakbadhabits.operation.habits.totalHabitEventCountByDaily
import epicarchitect.breakbadhabits.operation.habits.validation.DailyHabitEventCountError
import epicarchitect.breakbadhabits.operation.habits.validation.HabitEventRecordTimeRangeError
import epicarchitect.breakbadhabits.operation.habits.validation.checkDailyHabitEventCount
import epicarchitect.breakbadhabits.operation.habits.validation.checkHabitEventRecordTimeRange
import epicarchitect.breakbadhabits.ui.component.Dialog
import epicarchitect.breakbadhabits.ui.component.FlowStateContainer
import epicarchitect.breakbadhabits.ui.component.SimpleScrollableScreen
import epicarchitect.breakbadhabits.ui.component.button.Button
import epicarchitect.breakbadhabits.ui.component.button.ButtonStyles
import epicarchitect.breakbadhabits.ui.component.calendar.RangeSelectionCalendarDialog
import epicarchitect.breakbadhabits.ui.component.calendar.rememberSelectionCalendarState
import epicarchitect.breakbadhabits.ui.component.regex.Regexps
import epicarchitect.breakbadhabits.ui.component.stateOfOneOrNull
import epicarchitect.breakbadhabits.ui.component.text.InputCard
import epicarchitect.breakbadhabits.ui.component.text.Text
import epicarchitect.breakbadhabits.ui.component.text.TextInputCard
import epicarchitect.breakbadhabits.ui.format.formatted
import epicarchitect.calendar.compose.basis.EpicMonth
import epicarchitect.calendar.compose.basis.addYears
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone

fun dateSelectionMonthRange(timeZone: TimeZone) = EpicMonth.now(timeZone).let {
    it.addYears(-10).copy(month = Month.JANUARY)..it.copy(month = Month.DECEMBER)
}

class HabitEventRecordEditingScreen(private val habitEventRecordId: Int) : Screen {
    @Composable
    override fun Content() {
        HabitEventRecordEditing(habitEventRecordId)
    }
}

@Composable
fun HabitEventRecordEditing(habitEventRecordId: Int) {
    val strings = AppData.resources.strings.habitEventRecordEditingStrings
    val navigator = LocalNavigator.currentOrThrow

    FlowStateContainer(
        state = stateOfOneOrNull {
            AppData.database.habitEventRecordQueries.recordById(habitEventRecordId)
        }
    ) { habitEventRecord ->
        if (habitEventRecord != null) {
            FlowStateContainer(
                state = stateOfOneOrNull(habitEventRecord) {
                    AppData.database.habitQueries.habitById(habitEventRecord.habitId)
                }
            ) { habit ->
                SimpleScrollableScreen(
                    title = strings.titleText(habit?.name ?: "..."),
                    onBackClick = navigator::pop
                ) {
                    if (habit != null) {
                        Content(habitEventRecord)
                    }
                }
            }
        }
    }
}

@Composable
private fun ColumnScope.Content(record: HabitEventRecord) {
    val strings = AppData.resources.strings.habitEventRecordEditingStrings
    val habitEventRecordQueries = AppData.database.habitEventRecordQueries
    val navigator = LocalNavigator.currentOrThrow

    val timeZone by AppData.dateTime.currentTimeZoneState.collectAsState()

    var showRangeSelectionShow by rememberSaveable { mutableStateOf(false) }
    var showDeletion by remember { mutableStateOf(false) }

    var selectedTimeRange by remember(record) { mutableStateOf(record.timeRange()) }
    var timeRangeError by remember(selectedTimeRange) { mutableStateOf<HabitEventRecordTimeRangeError?>(null) }

    var dailyEventCount by rememberSaveable(record) { mutableIntStateOf(record.dailyEventCount(timeZone)) }
    var dailyHabitEventCountError by remember { mutableStateOf<DailyHabitEventCountError?>(null) }

    var comment by rememberSaveable(record) { mutableStateOf(record.comment) }

    if (showRangeSelectionShow) {
        val state = rememberSelectionCalendarState(
            initialSelectedDateTime = selectedTimeRange.toLocalDateTimeRange(timeZone),
            monthRange = dateSelectionMonthRange(timeZone)
        )

        RangeSelectionCalendarDialog(
            state = state,
            onCancel = {
                showRangeSelectionShow = false
            },
            onConfirm = {
                showRangeSelectionShow = false
                selectedTimeRange = it.toInstantRange(timeZone)
            }
        )
    }

    if (showDeletion) {
        DeletionDialog(
            record = record,
            onDismiss = { showDeletion = false }
        )
    }

    Spacer(Modifier.height(16.dp))

    TextInputCard(
        title = strings.dailyEventCountLabel(),
        description = strings.dailyEventCountDescription(),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        value = dailyEventCount.toString(),
        onValueChange = {
            dailyEventCount = it.toIntOrNull() ?: 0
            dailyHabitEventCountError = null
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number
        ),
        regex = Regexps.integersOrEmpty(maxCharCount = 4),
        error = dailyHabitEventCountError?.let(strings::dailyEventCountError)
    )

    Spacer(Modifier.height(16.dp))

    InputCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        title = strings.timeRangeTitle(),
        description = strings.timeRangeDescription(),
        error = timeRangeError?.let(strings::timeRangeError)
    ) {
        Button(
            modifier = Modifier.padding(it),
            onClick = {
                showRangeSelectionShow = true
            },
            text = selectedTimeRange.formatted(timeZone)
        )
    }

    Spacer(Modifier.height(16.dp))

    TextInputCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        title = strings.commentTitle(),
        description = strings.commentDescription(),
        value = comment,
        onValueChange = {
            comment = it
        },
        multiline = true
    )

    Spacer(modifier = Modifier.height(16.dp))

    Button(
        modifier = Modifier.padding(horizontal = 16.dp),
        text = strings.deleteButton(),
        style = ButtonStyles.dangerous,
        onClick = {
            showDeletion = true
        }
    )

    Spacer(modifier = Modifier.weight(1.0f))

    Spacer(modifier = Modifier.height(16.dp))

    Button(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .align(Alignment.End),
        text = strings.finishButton(),
        style = ButtonStyles.primary,
        onClick = {
            dailyHabitEventCountError = checkDailyHabitEventCount(dailyEventCount)
            if (dailyHabitEventCountError != null) return@Button

            timeRangeError = checkHabitEventRecordTimeRange(
                timeRange = selectedTimeRange,
                currentTime = AppData.dateTime.currentTimeState.value
            )
            if (timeRangeError != null) return@Button

            habitEventRecordQueries.update(
                id = record.id,
                startTime = selectedTimeRange.start,
                endTime = selectedTimeRange.endInclusive,
                eventCount = totalHabitEventCountByDaily(
                    dailyEventCount = dailyEventCount,
                    timeRange = selectedTimeRange,
                    timeZone = timeZone
                ),
                comment = comment
            )
            navigator.pop()
        }
    )

    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
private fun DeletionDialog(
    record: HabitEventRecord,
    onDismiss: () -> Unit
) {
    val strings = AppData.resources.strings.habitEventRecordEditingStrings
    val habitEventRecordQueries = AppData.database.habitEventRecordQueries
    val navigator = LocalNavigator.currentOrThrow

    Dialog(onDismiss) {
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
                    onClick = onDismiss
                )

                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    text = strings.yes(),
                    style = ButtonStyles.primary,
                    onClick = {
                        habitEventRecordQueries.deleteById(record.id)
                        navigator.pop()
                    }
                )
            }
        }
    }
}