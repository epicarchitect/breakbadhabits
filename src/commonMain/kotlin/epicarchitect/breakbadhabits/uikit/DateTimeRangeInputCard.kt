package epicarchitect.breakbadhabits.uikit

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.TextSelectionColors
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import epicarchitect.breakbadhabits.Environment
import epicarchitect.breakbadhabits.datetime.format.formatted
import epicarchitect.breakbadhabits.uikit.button.Button
import epicarchitect.breakbadhabits.uikit.button.ButtonStyles
import epicarchitect.breakbadhabits.uikit.text.InputCard
import epicarchitect.breakbadhabits.uikit.text.Text
import epicarchitect.breakbadhabits.uikit.theme.AppTheme
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
    var startDateTimeDialogShown by rememberSaveable { mutableStateOf(false) }
    var endDateTimeDialogShown by rememberSaveable { mutableStateOf(false) }

    val startDateTime = value.start.toLocalDateTime(timeZone)
    val endDateTime = value.endInclusive.toLocalDateTime(timeZone)

    val selectedStartDate = startDateTime.date
    val selectedStartTime = startDateTime.time

    val selectedEndDate = endDateTime.date
    val selectedEndTime = endDateTime.time

    val textFieldColors = TextFieldDefaults.colors(
        disabledTextColor = AppTheme.colorScheme.onSurface.copy(alpha = 0.9f),
        disabledContainerColor = Color.Transparent,
        disabledIndicatorColor = AppTheme.colorScheme.onSurface.copy(alpha = 0.7f),
        disabledLeadingIconColor = AppTheme.colorScheme.onSurface.copy(alpha = 0.6f),
        selectionColors = TextSelectionColors(Color.Transparent, Color.Transparent)
    )

    if (startDateTimeDialogShown) {
        DateTimeSelectionDialog(
            initial = value.start,
            timeZone = timeZone,
            onSelected = {
                onChanged(it..value.endInclusive)
                startDateTimeDialogShown = false
            },
            onDismiss = {
                startDateTimeDialogShown = false
            },
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    return utcTimeMillis <= Environment.dateTime.currentInstant()
                        .toLocalDateTime(timeZone)
                        .date
                        .toEpochMillis()
                }
            }
        )
    }

    if (endDateTimeDialogShown) {
        DateTimeSelectionDialog(
            initial = value.endInclusive,
            timeZone = timeZone,
            onSelected = {
                onChanged(value.start..it)
                endDateTimeDialogShown = false
            },
            onDismiss = {
                endDateTimeDialogShown = false
            },
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    return utcTimeMillis >= selectedStartDate.toEpochMillis()
                            && utcTimeMillis <= Environment.dateTime.currentInstant()
                        .toLocalDateTime(timeZone).date.toEpochMillis()
                }
            }
        )
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
                        .padding(start = 14.dp, end = 14.dp)
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.small)
                        .clickable {
                            startDateTimeDialogShown = true
                        },
                    value = startDateTime.formatted(),
                    onValueChange = {},
                    readOnly = true,
                    enabled = false,
                    colors = textFieldColors,
                    leadingIcon = {
                        Icon(Environment.resources.icons.commonIcons.calendar)
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
                        .padding(start = 14.dp, end = 14.dp)
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.small)
                        .clickable {
                            endDateTimeDialogShown = true
                        },
                    value = endDateTime.formatted(),
                    onValueChange = {},
                    readOnly = true,
                    enabled = false,
                    colors = textFieldColors,
                    leadingIcon = {
                        Icon(Environment.resources.icons.commonIcons.calendar)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DateTimeSelectionDialog(
    initial: Instant,
    timeZone: TimeZone,
    onSelected: (Instant) -> Unit,
    onDismiss: () -> Unit,
    selectableDates: SelectableDates
) {
    var dateSelected by remember { mutableStateOf(false) }
    val initialDateTime = initial.toLocalDateTime(timeZone)

    val dateState = rememberDatePickerState(
        initialSelectedDateMillis = initialDateTime.date.toEpochMillis(),
        selectableDates = selectableDates
    )

    val timeState = rememberTimePickerState(
        initialHour = initialDateTime.time.hour,
        initialMinute = initialDateTime.time.minute
    )

    Dialog(
        onDismiss = onDismiss
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            if (dateSelected) {
                TimePicker(timeState)
            } else {
                DatePicker(dateState)
            }

            Button(
                modifier = Modifier.align(Alignment.End),
                style = ButtonStyles.regular.copy(elevation = 0.dp),
                text = "OK",
                onClick = {
                    if (dateSelected) {
                        val newDate = Instant.fromEpochMilliseconds(dateState.selectedDateMillis!!)
                            .toLocalDateTime(timeZone).date
                        val newTime = LocalTime(timeState.hour, timeState.minute, 0)

                        onSelected(LocalDateTime(newDate,  newTime).toInstant(timeZone))
                    } else {
                        dateSelected = true
                    }
                }
            )
        }
    }
}