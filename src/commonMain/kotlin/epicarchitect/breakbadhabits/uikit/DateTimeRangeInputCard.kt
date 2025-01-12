package epicarchitect.breakbadhabits.uikit

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import epicarchitect.breakbadhabits.environment.LocalAppEnvironment
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.UtcOffset
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

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
    showAsRangeMode: Boolean,
    onRangeModeChanged: (Boolean) -> Unit
) {
    val environment = LocalAppEnvironment.current
    val strings = environment.resources.strings.habitEventRecordEditingStrings
    val startDateTime = value.start.toLocalDateTime(timeZone)
    val currentDateTime = environment.dateTime.currentInstant().toLocalDateTime(timeZone)

    InputCard(
        modifier = modifier,
        title = title,
        description = description,
        error = error
    ) {
        Column {
            AnimatedVisibility(visible = showAsRangeMode) {
                Text(
                    modifier = Modifier.padding(bottom = 4.dp),
                    text = startTimeLabel,
                    style = MaterialTheme.typography.titleSmall
                )
            }
            DateTimeInputRow(
                modifier = Modifier
                    .fillMaxWidth(),
                value = value.start,
                onChanged = {
                    if (!showAsRangeMode) {
                        onChanged(it..it)
                    } else {
                        onChanged(it..value.endInclusive)
                    }
                },
                isSelectableDate = {
                    it <= currentDateTime.date
                },
                timeZone = timeZone
            )
        }

        AnimatedVisibility(visible = showAsRangeMode) {
            Column(
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(
                    text = endTimeLabel,
                    style = MaterialTheme.typography.titleSmall
                )

                DateTimeInputRow(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .fillMaxWidth(),
                    value = value.endInclusive,
                    onChanged = {
                        onChanged(value.start..it)
                    },
                    isSelectableDate = {
                        it >= startDateTime.date && it <= currentDateTime.date
                    },
                    timeZone = timeZone
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        val onViewModeChanged = { checked: Boolean ->
            if (!checked) {
                onChanged(value.start..value.start)
            }

            onRangeModeChanged(checked)
        }

        Row(
            Modifier
                .clickable(
                    indication = null,
                    interactionSource = null
                ) {
                    onViewModeChanged(!showAsRangeMode)
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = showAsRangeMode,
                onCheckedChange = onViewModeChanged
            )

            Text(
                modifier = Modifier.padding(start = 4.dp),
                text = strings.inputDateTimeAsRangeCheckbox(),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DateTimeInputRow(
    value: Instant,
    isSelectableDate: (LocalDate) -> Boolean,
    timeZone: TimeZone,
    onChanged: (Instant) -> Unit,
    modifier: Modifier = Modifier
) {
    val environment = LocalAppEnvironment.current
    val dateTimeFormatter = environment.format.dateTimeFormatter
    val dateTime = value.toLocalDateTime(timeZone)
    var dateSelectionVisible by rememberSaveable { mutableStateOf(false) }
    var timeSelectionVisible by rememberSaveable { mutableStateOf(false) }
    val borderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
    val textColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)

    if (dateSelectionVisible) {
        val date = dateTime.date
        val state = rememberDatePickerState(
            initialSelectedDateMillis = date.toEpochMillis(),
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long) = isSelectableDate(
                    Instant.fromEpochMilliseconds(utcTimeMillis).toLocalDateTime(timeZone).date
                )
            }
        )

        DatePickerDialog(
            onDismissRequest = {
                dateSelectionVisible = false
            },
            confirmButton = {
                Button(
                    onClick = {
                        val newDate = Instant.fromEpochMilliseconds(state.selectedDateMillis!!)
                            .toLocalDateTime(timeZone).date
                        dateSelectionVisible = false
                        onChanged(LocalDateTime(newDate, dateTime.time).toInstant(timeZone))
                    }
                ) {
                    Text(text = "OK")
                }
            }
        ) {
            DatePicker(state)
        }
    }

    if (timeSelectionVisible) {
        val time = dateTime.time
        val state = rememberTimePickerState(
            initialHour = time.hour,
            initialMinute = time.minute
        )
        Dialog(
            onDismiss = {
                timeSelectionVisible = false
            }
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                TimePicker(state)

                Button(
                    modifier = Modifier.align(Alignment.End),
                    onClick = {
                        val newTime = LocalTime(state.hour, state.minute, 0)
                        timeSelectionVisible = false
                        onChanged(LocalDateTime(dateTime.date, newTime).toInstant(timeZone))
                    }
                ) {
                    Text(text = "OK")
                }
            }
        }
    }

    Row(
        modifier = modifier
            .border(
                width = 1.dp,
                color = borderColor,
                shape = MaterialTheme.shapes.small
            )
            .clip(MaterialTheme.shapes.small),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .height(56.dp)
                .clickable {
                    dateSelectionVisible = true
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(Modifier.width(16.dp))
            Icon(
                imageVector = Icons.Filled.CalendarToday,
                tint = textColor,
                contentDescription = null
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = dateTimeFormatter.format(dateTime.date),
                color = textColor
            )
            Spacer(Modifier.width(16.dp))
        }
        VerticalDivider(
            modifier = Modifier.height(56.dp),
            thickness = 1.5.dp,
            color = borderColor
        )
        Row(
            modifier = Modifier
                .height(56.dp)
                .clickable {
                    timeSelectionVisible = true
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(Modifier.width(16.dp))
            Icon(
                imageVector = Icons.Filled.AccessTime,
                tint = textColor,
                contentDescription = null
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = dateTimeFormatter.format(dateTime.time),
                color = textColor
            )
            Spacer(Modifier.width(16.dp))
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