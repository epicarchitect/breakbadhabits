package epicarchitect.breakbadhabits.ui.screen.habits.records.editing

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import epicarchitect.breakbadhabits.data.AppData
import epicarchitect.breakbadhabits.data.HabitEventRecord
import epicarchitect.breakbadhabits.operation.habits.dailyEventCount
import epicarchitect.breakbadhabits.operation.habits.totalHabitEventCountByDaily
import epicarchitect.breakbadhabits.operation.habits.validation.DailyHabitEventCountError
import epicarchitect.breakbadhabits.operation.habits.validation.HabitEventRecordTimeRangeError
import epicarchitect.breakbadhabits.operation.habits.validation.checkDailyHabitEventCount
import epicarchitect.breakbadhabits.operation.habits.validation.checkHabitEventRecordTimeRange
import epicarchitect.breakbadhabits.operation.math.ranges.ascended
import epicarchitect.breakbadhabits.ui.component.Dialog
import epicarchitect.breakbadhabits.ui.component.FlowStateContainer
import epicarchitect.breakbadhabits.ui.component.Icon
import epicarchitect.breakbadhabits.ui.component.SimpleScrollableScreen
import epicarchitect.breakbadhabits.ui.component.button.Button
import epicarchitect.breakbadhabits.ui.component.button.ButtonStyles
import epicarchitect.breakbadhabits.ui.component.regex.Regexps
import epicarchitect.breakbadhabits.ui.component.stateOfOneOrNull
import epicarchitect.breakbadhabits.ui.component.text.InputCard
import epicarchitect.breakbadhabits.ui.component.text.Text
import epicarchitect.breakbadhabits.ui.component.text.TextInputCard
import epicarchitect.breakbadhabits.ui.component.theme.AppTheme
import epicarchitect.breakbadhabits.ui.format.formatted
import epicarchitect.breakbadhabits.ui.screen.habits.records.creation.toEpochMillis
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

class HabitEventRecordEditingScreen(private val habitEventRecordId: Int) : Screen {
    @Composable
    override fun Content() {
        HabitEventRecordEditing(habitEventRecordId)
    }
}

private const val HIDE_PICKER = 0
private const val SHOW_PICKER_START = 1
private const val SHOW_PICKER_END = 2

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ColumnScope.Content(record: HabitEventRecord) {
    val strings = AppData.resources.strings.habitEventRecordEditingStrings
    val habitEventRecordQueries = AppData.database.habitEventRecordQueries
    val navigator = LocalNavigator.currentOrThrow

    val timeZone by AppData.dateTime.currentTimeZoneState.collectAsState()

    var showDeletion by remember { mutableStateOf(false) }

    var dateSelectionState by rememberSaveable { mutableStateOf(HIDE_PICKER) }
    var timeSelectionState by rememberSaveable { mutableStateOf(HIDE_PICKER) }

    val initialStartDate = record.startTime.toLocalDateTime(timeZone)
    val initialEndDate = record.endTime.toLocalDateTime(timeZone)

    var selectedStartDate by remember { mutableStateOf(initialStartDate.date) }
    var selectedStartTime by remember { mutableStateOf(initialStartDate.time) }

    var selectedEndDate by remember { mutableStateOf(initialEndDate.date) }
    var selectedEndTime by remember { mutableStateOf(initialEndDate.time) }

    var timeRangeError by remember(
        selectedStartTime,
        selectedStartDate,
        selectedEndDate,
        selectedEndTime
    ) { mutableStateOf<HabitEventRecordTimeRangeError?>(null) }
    var dailyEventCount by rememberSaveable(record) { mutableIntStateOf(record.dailyEventCount(timeZone)) }
    var dailyHabitEventCountError by remember { mutableStateOf<DailyHabitEventCountError?>(null) }

    var comment by rememberSaveable(record) { mutableStateOf(record.comment) }

    if (dateSelectionState != HIDE_PICKER) {
        val date = if (dateSelectionState == SHOW_PICKER_START) selectedStartDate else selectedEndDate
        val state = rememberDatePickerState(
            initialSelectedDateMillis = date.toEpochMillis()
        )
        Dialog(
            onDismiss = {
                dateSelectionState = HIDE_PICKER
            }
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                DatePicker(state)

                Button(
                    modifier = Modifier.align(Alignment.End),
                    text = strings.done(),
                    onClick = {
                        val newDate = Instant.fromEpochMilliseconds(state.selectedDateMillis!!)
                            .toLocalDateTime(timeZone).date

                        if (dateSelectionState == SHOW_PICKER_START) selectedStartDate = newDate
                        else selectedEndDate = newDate

                        dateSelectionState = HIDE_PICKER
                    }
                )
            }
        }
    }

    if (timeSelectionState != HIDE_PICKER) {
        val time = if (timeSelectionState == SHOW_PICKER_START) selectedStartTime else selectedEndTime
        val state = rememberTimePickerState(
            initialHour = time.hour,
            initialMinute = time.minute
        )
        Dialog(
            onDismiss = {
                timeSelectionState = HIDE_PICKER
            }
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                TimePicker(state)

                Button(
                    modifier = Modifier.align(Alignment.End),
                    text = strings.done(),
                    onClick = {
                        val newTime = LocalTime(state.hour, state.minute, 0)

                        if (timeSelectionState == SHOW_PICKER_START) selectedStartTime = newTime
                        else selectedEndTime = newTime

                        timeSelectionState = HIDE_PICKER
                    }
                )
            }
        }
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
        Column {
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = strings.startDateTimeLabel(),
                type = Text.Type.Title,
                priority = Text.Priority.Low
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .padding(start = 14.dp, end = 7.dp)
                        .weight(0.8f)
                        .clickable {
                            dateSelectionState = SHOW_PICKER_START
                        },
                    value = selectedStartDate.formatted(),
                    onValueChange = {},
                    readOnly = true,
                    enabled = false,
                    colors = TextFieldDefaults.colors(
                        disabledTextColor = AppTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                        disabledContainerColor = Color.Transparent,
                        disabledIndicatorColor = AppTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                        disabledLeadingIconColor = AppTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                    ),
                    leadingIcon = {
                        Icon(AppData.resources.icons.commonIcons.calendar)
                    },
                    shape = MaterialTheme.shapes.small,
                )
                OutlinedTextField(
                    modifier = Modifier
                        .padding(start = 7.dp, end = 14.dp)
                        .weight(0.5f)
                        .clickable {
                            timeSelectionState = SHOW_PICKER_START
                        },
                    value = selectedStartTime.formatted(),
                    onValueChange = {},
                    readOnly = true,
                    enabled = false,
                    colors = TextFieldDefaults.colors(
                        disabledTextColor = AppTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                        disabledContainerColor = Color.Transparent,
                        disabledIndicatorColor = AppTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                        disabledLeadingIconColor = AppTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                    ),
                    leadingIcon = {
                        Icon(AppData.resources.icons.commonIcons.time)
                    },
                    shape = MaterialTheme.shapes.small,
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Column {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = strings.endDateTimeLabel(),
                type = Text.Type.Title,
                priority = Text.Priority.Low
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .padding(start = 14.dp, end = 7.dp)
                        .weight(0.8f)
                        .clickable {
                            dateSelectionState = SHOW_PICKER_END
                        },
                    value = selectedEndDate.formatted(),
                    onValueChange = {},
                    readOnly = true,
                    enabled = false,
                    colors = TextFieldDefaults.colors(
                        disabledTextColor = AppTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                        disabledContainerColor = Color.Transparent,
                        disabledIndicatorColor = AppTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                        disabledLeadingIconColor = AppTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                    ),
                    leadingIcon = {
                        Icon(AppData.resources.icons.commonIcons.calendar)
                    },
                    shape = MaterialTheme.shapes.small,
                )
                OutlinedTextField(
                    modifier = Modifier
                        .padding(start = 7.dp, end = 14.dp)
                        .weight(0.5f)
                        .clickable {
                            timeSelectionState = SHOW_PICKER_END
                        },
                    value = selectedEndTime.formatted(),
                    onValueChange = {},
                    readOnly = true,
                    enabled = false,
                    colors = TextFieldDefaults.colors(
                        disabledTextColor = AppTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                        disabledContainerColor = Color.Transparent,
                        disabledIndicatorColor = AppTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                        disabledLeadingIconColor = AppTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                    ),
                    leadingIcon = {
                        Icon(AppData.resources.icons.commonIcons.time)
                    },
                    shape = MaterialTheme.shapes.small,
                )
            }
        }
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

            val selectedTimeRange = (LocalDateTime(
                date = selectedStartDate,
                time = selectedStartTime
            ).toInstant(timeZone)..LocalDateTime(
                date = selectedEndDate,
                time = selectedEndTime
            ).toInstant(timeZone)).ascended()

            timeRangeError = checkHabitEventRecordTimeRange(
                timeRange = selectedTimeRange,
                currentTime = AppData.dateTime.currentInstantState.value
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