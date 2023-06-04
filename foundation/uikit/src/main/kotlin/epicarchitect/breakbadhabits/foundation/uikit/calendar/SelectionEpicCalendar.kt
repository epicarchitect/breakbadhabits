package epicarchitect.breakbadhabits.foundation.uikit.calendar

import android.text.format.DateFormat
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import epicarchitect.breakbadhabits.foundation.datetime.MonthOfYear
import epicarchitect.breakbadhabits.foundation.datetime.monthOfYear
import epicarchitect.breakbadhabits.foundation.datetime.next
import epicarchitect.breakbadhabits.foundation.datetime.previous
import epicarchitect.breakbadhabits.foundation.uikit.Card
import epicarchitect.breakbadhabits.foundation.uikit.IconButton
import epicarchitect.breakbadhabits.foundation.uikit.LocalResourceIcon
import epicarchitect.breakbadhabits.foundation.uikit.R
import epicarchitect.breakbadhabits.foundation.uikit.button.Button
import epicarchitect.breakbadhabits.foundation.uikit.theme.AppTheme
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toJavaLocalTime
import kotlinx.datetime.toLocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.format.TextStyle
import java.util.Locale

class IntervalSelectionEpicCalendarState {
    var timeZone by mutableStateOf(TimeZone.currentSystemDefault())
    val monthTitles = Month.values().map {
        it.getDisplayName(
            TextStyle.FULL_STANDALONE,
            Locale.getDefault()
        ).replaceFirstChar(Char::uppercase)
    }

    var availableRange: ClosedRange<Instant> by mutableStateOf(defaultAvailableRange(timeZone))
    val minMonthOfYear by derivedStateOf { availableRange.start.monthOfYear(timeZone) }
    val maxMonthOfYear by derivedStateOf { availableRange.endInclusive.monthOfYear(timeZone) }

    var selectedTab by mutableStateOf(0)
    var selectedStartDate by mutableStateOf<LocalDate?>(null)
    var selectedEndDate by mutableStateOf<LocalDate?>(null)
    var currentMonth by mutableStateOf(MonthOfYear.now(timeZone))
    var showYearMonthSelection by mutableStateOf(false)
    val selectedRange by derivedStateOf {
        val start = selectedStartDate?.let {
            LocalDateTime(it, selectedStartTime).toInstant(timeZone)
        }
        val end = selectedEndDate?.let {
            LocalDateTime(it, selectedEndTime).toInstant(timeZone)
        }
        when {
            start != null && end == null -> start..start
            start == null && end != null -> end..end
            start != null && end != null -> start..end
            else -> null
        }
    }
    var selectedStartTime by mutableStateOf(LocalTime(12, 0, 0))
    var selectedEndTime by mutableStateOf(LocalTime(12, 0, 0))

    fun selectRange(range: ClosedRange<Instant>) {
        val start = range.start.toLocalDateTime(timeZone)
        val end = range.endInclusive.toLocalDateTime(timeZone)
        selectedStartDate = start.date
        selectedStartTime = start.time
        selectedEndDate = end.date
        selectedEndTime = end.time
    }
}

@Composable
fun IntervalSelectionEpicCalendar(
    state: IntervalSelectionEpicCalendarState,
    onSelected: (ClosedRange<Instant>) -> Unit,
    onCancel: () -> Unit,
    intervalsInnerPadding: Dp = 0.dp,
) {
    val context = LocalContext.current
    val timeFormatter = DateTimeFormatter.ofPattern(
        if (DateFormat.is24HourFormat(context)) "HH:mm"
        else "hh:mm a"
    )
    val dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)
    Column(modifier = Modifier.padding(top = 8.dp)) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = intervalsInnerPadding),
        ) {
            Column {
                AnimatedVisibility(visible = !state.showYearMonthSelection) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(
                            onClick = {
                                state.currentMonth = state.currentMonth.previous()
                            },
                            content = {
                                LocalResourceIcon(R.drawable.uikit_arrow_left)
                            },
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .weight(1f)
                                .padding(horizontal = 4.dp)
                                .clip(RoundedCornerShape(100.dp))
                                .clickable {
                                    state.showYearMonthSelection = !state.showYearMonthSelection
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = state.monthTitles[state.currentMonth.month.value - 1] + " " + state.currentMonth.year,
                                textAlign = TextAlign.Center
                            )
                        }
                        IconButton(
                            onClick = {
                                state.currentMonth = state.currentMonth.next()
                            },
                            content = {
                                LocalResourceIcon(R.drawable.uikit_arrow_right)
                            }
                        )
                    }
                }
                AnimatedVisibility(visible = state.showYearMonthSelection) {
                    var yearMonthSelectionBoxSize by remember { mutableStateOf(IntSize.Zero) }
                    Column(
                        modifier = Modifier.onSizeChanged {
                            yearMonthSelectionBoxSize = it
                        }
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    state.showYearMonthSelection = false
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            LocalResourceIcon(
                                modifier = Modifier.padding(4.dp),
                                imageVector = Icons.Default.KeyboardArrowUp
                            )
                        }

                        ScrollableTabRow(
                            selectedTabIndex = state.currentMonth.month.value - 1,
                            modifier = Modifier.fillMaxWidth(),
                            containerColor = Color.Transparent,
                            indicator = {},
                            divider = {},
                            edgePadding = 12.dp
                        ) {
                            repeat(state.monthTitles.size) {
                                val monthTitle = state.monthTitles[it]
                                val isSelected = state.currentMonth.month.value == it + 1
                                Card(
                                    modifier = Modifier.padding(4.dp),
                                    backgroundColor = if (isSelected) {
                                        AppTheme.colorScheme.primary
                                    } else {
                                        AppTheme.colorScheme.onBackground.copy(alpha = 0.1f)
                                    },
                                    elevation = 0.dp
                                ) {
                                    Text(
                                        modifier = Modifier
                                            .defaultMinSize(minWidth = 90.dp)
                                            .clickable {
                                                state.currentMonth = state.currentMonth.copy(
                                                    month = Month(it + 1)
                                                )
                                            }
                                            .padding(
                                                vertical = 8.dp,
                                                horizontal = 20.dp
                                            ),
                                        text = monthTitle,
                                        textAlign = TextAlign.Center,
                                        overflow = TextOverflow.Ellipsis,
                                        fontSize = 14.sp,
                                        color = if (isSelected) {
                                            AppTheme.colorScheme.onPrimary
                                        } else {
                                            AppTheme.colorScheme.onBackground
                                        }
                                    )
                                }
                            }
                        }
                        ScrollableTabRow(
                            selectedTabIndex = state.maxMonthOfYear.year - state.currentMonth.year,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp),
                            containerColor = Color.Transparent,
                            indicator = {},
                            divider = {},
                            edgePadding = 12.dp
                        ) {
                            repeat(state.maxMonthOfYear.year - state.minMonthOfYear.year) {
                                val year = state.maxMonthOfYear.year - it
                                val yearTitle = year.toString()
                                val isSelected = state.currentMonth.year == year

                                Card(
                                    modifier = Modifier.padding(4.dp),
                                    backgroundColor = if (isSelected) {
                                        AppTheme.colorScheme.primary
                                    } else {
                                        AppTheme.colorScheme.onBackground.copy(alpha = 0.1f)
                                    },
                                    elevation = 0.dp,
                                ) {
                                    Text(
                                        modifier = Modifier
                                            .defaultMinSize(minWidth = 90.dp)
                                            .clickable {
                                                state.currentMonth = state.currentMonth.copy(
                                                    year = year
                                                )
                                            }
                                            .padding(
                                                vertical = 8.dp,
                                                horizontal = 20.dp
                                            ),
                                        text = yearTitle,
                                        textAlign = TextAlign.Center,
                                        overflow = TextOverflow.Ellipsis,
                                        fontSize = 14.sp,
                                        color = if (isSelected) {
                                            AppTheme.colorScheme.onPrimary
                                        } else {
                                            AppTheme.colorScheme.onBackground
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        EpicCalendar(
            state = rememberEpicCalendarState(
                timeZone = state.timeZone,
                monthOfYear = state.currentMonth,
                ranges = listOfNotNull(state.selectedRange)
            ),
            onDayClick = {
                state.showYearMonthSelection = false
                if (state.selectedTab == 0) {
                    state.selectedStartDate = it.date
                } else if (state.selectedTab == 1) {
                    state.selectedEndDate = it.date
                }
            },
            horizontalInnerPadding = intervalsInnerPadding
        )

        AnimatedVisibility(visible = !state.showYearMonthSelection) {
            Column {
                val selectedTime = if (state.selectedTab == 0) {
                    state.selectedStartTime
                } else {
                    state.selectedEndTime
                }

                ScrollableTabRow(
                    selectedTabIndex = selectedTime.hour,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    containerColor = Color.Transparent,
                    indicator = {},
                    divider = {},
                    edgePadding = 12.dp
                ) {
                    repeat(24) {
                        val itemTime = LocalTime(it, 0, 0)
                        val isSelected = selectedTime.hour == itemTime.hour
                        Card(
                            modifier = Modifier.padding(4.dp),
                            backgroundColor = if (isSelected) {
                                AppTheme.colorScheme.primary
                            } else {
                                AppTheme.colorScheme.onBackground.copy(alpha = 0.1f)
                            },
                            elevation = 0.dp,
                        ) {
                            Text(
                                modifier = Modifier
                                    .defaultMinSize(minWidth = 90.dp)
                                    .clickable {
                                        if (state.selectedTab == 0) {
                                            state.selectedStartTime = itemTime
                                        } else {
                                            state.selectedEndTime = itemTime
                                        }
                                    }
                                    .padding(
                                        vertical = 8.dp,
                                        horizontal = 20.dp
                                    ),
                                text = timeFormatter.format(itemTime.toJavaLocalTime()),
                                textAlign = TextAlign.Center,
                                overflow = TextOverflow.Ellipsis,
                                fontSize = 14.sp,
                                color = if (isSelected) {
                                    AppTheme.colorScheme.onPrimary
                                } else {
                                    AppTheme.colorScheme.onBackground
                                }
                            )
                        }
                    }
                }

                TabRow(
                    modifier = Modifier.padding(top = 8.dp),
                    selectedTabIndex = state.selectedTab,
                    containerColor = Color.Transparent
                ) {
                    Tab(
                        selected = state.selectedTab == 0,
                        onClick = {
                            state.selectedTab = 0
                            state.selectedStartDate?.let {
                                state.currentMonth = it.monthOfYear
                            }
                        }
                    ) {
                        Column(
                            modifier = Modifier.padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Начало",
                                fontWeight = FontWeight.Bold,
                                color = AppTheme.colorScheme.onSurface
                            )

                            Text(
                                text = state.selectedStartDate?.let {
                                    val date = dateFormatter.format(it.toJavaLocalDate())
                                    val time =
                                        timeFormatter.format(state.selectedStartTime.toJavaLocalTime())
                                    "$date\n$time"
                                } ?: "не выбрано",
                                fontWeight = FontWeight.Light,
                                fontSize = 12.sp,
                                style = androidx.compose.ui.text.TextStyle(
                                    lineHeight = 12.sp
                                ),
                                textAlign = TextAlign.Center,
                                color = AppTheme.colorScheme.onSurface
                            )
                        }
                    }
                    Tab(
                        selected = state.selectedTab == 1,
                        onClick = {
                            state.selectedTab = 1
                            state.selectedEndDate?.let {
                                state.currentMonth = it.monthOfYear
                            }
                        }
                    ) {
                        Column(
                            modifier = Modifier.padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Конец",
                                fontWeight = FontWeight.Bold,
                                color = AppTheme.colorScheme.onSurface
                            )

                            Text(
                                text = state.selectedEndDate?.let {
                                    val date = dateFormatter.format(it.toJavaLocalDate())
                                    val time =
                                        timeFormatter.format(state.selectedEndTime.toJavaLocalTime())
                                    "$date\n$time"
                                } ?: "не выбрано",
                                fontWeight = FontWeight.Light,
                                fontSize = 12.sp,
                                style = androidx.compose.ui.text.TextStyle(
                                    lineHeight = 12.sp
                                ),
                                textAlign = TextAlign.Center,
                                color = AppTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(top = 12.dp, end = 16.dp, bottom = 8.dp)
                ) {
                    Button(
                        onClick = onCancel,
                        text = "Cancel"
                    )
                    Spacer(Modifier.padding(4.dp))
                    Button(
                        onClick = {
                            if (state.selectedTab == 0 && state.selectedEndDate == null) {
                                state.selectedTab = 1
                            } else if (state.selectedTab == 1 && state.selectedStartDate == null) {
                                state.selectedTab = 0
                            } else {
                                state.selectedRange?.let(onSelected)
                            }
                        },
                        text = if (state.selectedTab == 0 && state.selectedEndDate == null || state.selectedTab == 1 && state.selectedStartDate == null) "Next" else "Apply",
                        enabled = state.selectedTab == 0 && state.selectedStartDate != null || state.selectedTab == 1 && state.selectedEndDate != null,
                        type = Button.Type.Main
                    )
                }
            }
        }
    }
}

@Composable
fun rememberSelectionEpicCalendarState(
    timeZone: TimeZone,
    availableRange: ClosedRange<Instant> = defaultAvailableRange(timeZone),
    currentMonth: MonthOfYear = MonthOfYear.now(timeZone),
    initialRange: ClosedRange<Instant>? = null,
) = remember(currentMonth, availableRange, initialRange) {
    IntervalSelectionEpicCalendarState().also {
        it.timeZone = timeZone
        it.availableRange = availableRange
        it.currentMonth = currentMonth
        if (initialRange != null) it.selectRange(initialRange)
    }
}

private fun defaultAvailableRange(timeZone: TimeZone) = Clock.System.now().let {
    it.minus(10, DateTimeUnit.YEAR, timeZone)..it
}