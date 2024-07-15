package epicarchitect.breakbadhabits.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import epicarchitect.breakbadhabits.environment.Environment
import epicarchitect.breakbadhabits.operation.math.ranges.ascended
import epicarchitect.breakbadhabits.ui.component.button.Button
import epicarchitect.breakbadhabits.ui.component.button.ButtonStyles
import epicarchitect.breakbadhabits.ui.component.text.InputCard
import epicarchitect.breakbadhabits.ui.component.text.Text
import epicarchitect.breakbadhabits.ui.component.theme.AppTheme
import epicarchitect.breakbadhabits.ui.format.formatted
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.UtcOffset
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

private const val HIDE_PICKER = 0
private const val SHOW_PICKER_START = 1
private const val SHOW_PICKER_END = 2

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateTimeRangeInputCard(
    title: String,
    description: String,
    startTimeLabel: String,
    endTimeLabel: String,
    value: ClosedRange<Instant>,
    onChanged: (ClosedRange<Instant>) -> Unit,
    timeZone: TimeZone,
    error: String? = null,
    modifier: Modifier = Modifier,
) {
    var dateSelectionState by rememberSaveable { mutableStateOf(HIDE_PICKER) }
    var timeSelectionState by rememberSaveable { mutableStateOf(HIDE_PICKER) }

    val startDateTime = value.start.toLocalDateTime(timeZone)
    val endDateTime = value.endInclusive.toLocalDateTime(timeZone)

    var selectedStartDate = startDateTime.date
    var selectedStartTime = startDateTime.time

    var selectedEndDate = endDateTime.date
    var selectedEndTime = endDateTime.time

    if (dateSelectionState != HIDE_PICKER) {
        val date = if (dateSelectionState == SHOW_PICKER_START) selectedStartDate else selectedEndDate
        val state = rememberDatePickerState(
            initialSelectedDateMillis = date.toEpochMillis(),
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    return if (dateSelectionState == SHOW_PICKER_START) {
                        utcTimeMillis <= Environment.dateTime.currentInstantState.value
                            .toLocalDateTime(timeZone)
                            .date
                            .toEpochMillis()
                    } else {
                        utcTimeMillis >= selectedStartDate.toEpochMillis() && utcTimeMillis <= Environment.dateTime.currentInstantState.value
                            .toLocalDateTime(timeZone)
                            .date
                            .toEpochMillis()
                    }
                }
            }
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
                    text = "OK", // nice
                    style = ButtonStyles.regular.copy(elevation = 0.dp),
                    onClick = {
                        val newDate = Instant.fromEpochMilliseconds(state.selectedDateMillis!!)
                            .toLocalDateTime(timeZone).date

                        if (dateSelectionState == SHOW_PICKER_START) selectedStartDate = newDate
                        else selectedEndDate = newDate

                        dateSelectionState = HIDE_PICKER

                        onChanged(
                            (LocalDateTime(
                                selectedStartDate,
                                selectedStartTime
                            ).toInstant(timeZone)..LocalDateTime(
                                selectedEndDate,
                                selectedEndTime
                            ).toInstant(timeZone)).ascended()
                        )
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
                    style = ButtonStyles.regular.copy(elevation = 0.dp),
                    text = "OK", // :)
                    onClick = {
                        val newTime = LocalTime(state.hour, state.minute, 0)

                        if (timeSelectionState == SHOW_PICKER_START) selectedStartTime = newTime
                        else selectedEndTime = newTime

                        timeSelectionState = HIDE_PICKER

                        onChanged(
                            (LocalDateTime(
                                selectedStartDate,
                                selectedStartTime
                            ).toInstant(timeZone)..LocalDateTime(
                                selectedEndDate,
                                selectedEndTime
                            ).toInstant(timeZone)).ascended()
                        )
                    }
                )
            }
        }
    }

    InputCard(
        modifier = modifier,
        title = title,
        description = description,
        error = error
    ) {
        Column {
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = startTimeLabel,
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
                        disabledLeadingIconColor = AppTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    ),
                    leadingIcon = {
                        Icon(Environment.resources.icons.commonIcons.calendar)
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
                        disabledLeadingIconColor = AppTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    ),
                    leadingIcon = {
                        Icon(Environment.resources.icons.commonIcons.time)
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
                text = endTimeLabel,
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
                        disabledLeadingIconColor = AppTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    ),
                    leadingIcon = {
                        Icon(Environment.resources.icons.commonIcons.calendar)
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
                        disabledLeadingIconColor = AppTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    ),
                    leadingIcon = {
                        Icon(Environment.resources.icons.commonIcons.time)
                    },
                    shape = MaterialTheme.shapes.small,
                )
            }
        }
    }
}

private fun LocalDate.toEpochMillis() = LocalDateTime(
    date = this,
    time = LocalTime(
        hour = 0,
        minute = 0,
        second = 0
    )
).toInstant(offset = UtcOffset.ZERO).toEpochMilliseconds()