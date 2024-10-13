package epicarchitect.breakbadhabits.uikit

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableDates
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
    val startDateTime = value.start.toLocalDateTime(timeZone)
    val currentDateTime = Environment.dateTime.currentInstant().toLocalDateTime(timeZone)

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
            DateTimeInputRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                value = value.start,
                onChanged = {
                    onChanged(it..value.endInclusive)
                },
                isSelectableDate = {
                    it <= currentDateTime.date
                },
                timeZone = timeZone
            )
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
            DateTimeInputRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
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
    val dateTime = value.toLocalDateTime(timeZone)
    var dateSelectionVisible by rememberSaveable { mutableStateOf(false) }
    var timeSelectionVisible by rememberSaveable { mutableStateOf(false) }
    val borderColor = AppTheme.colorScheme.onSurface.copy(alpha = 0.7f)
    val textColor = AppTheme.colorScheme.onSurface.copy(alpha = 0.8f)

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
                    text = "OK", // nice
                    style = ButtonStyles.regular.copy(elevation = 0.dp),
                    onClick = {
                        val newDate = Instant.fromEpochMilliseconds(state.selectedDateMillis!!)
                            .toLocalDateTime(timeZone).date
                        dateSelectionVisible = false
                        onChanged(LocalDateTime(newDate, dateTime.time).toInstant(timeZone))
                    }
                )
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
                    style = ButtonStyles.regular.copy(elevation = 0.dp),
                    text = "OK", // :)
                    onClick = {
                        val newTime = LocalTime(state.hour, state.minute, 0)
                        timeSelectionVisible = false
                        onChanged(LocalDateTime(dateTime.date, newTime).toInstant(timeZone))
                    }
                )
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
                icon = Environment.resources.icons.commonIcons.calendar,
                tint = textColor
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = dateTime.date.formatted(withYear = true),
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
                icon = Environment.resources.icons.commonIcons.time,
                tint = textColor
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = dateTime.time.formatted(),
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