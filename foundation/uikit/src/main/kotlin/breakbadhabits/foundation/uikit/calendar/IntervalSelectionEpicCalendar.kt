package breakbadhabits.foundation.uikit.calendar

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowLeft
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
import breakbadhabits.foundation.uikit.Card
import breakbadhabits.foundation.uikit.Dialog
import breakbadhabits.foundation.uikit.IconButton
import breakbadhabits.foundation.uikit.LocalResourceIcon
import breakbadhabits.foundation.uikit.button.Button
import breakbadhabits.foundation.uikit.theme.AppTheme
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.Month
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.format.TextStyle
import java.util.*

class IntervalSelectionEpicCalendarState {
    val monthTitles = Month.values().map {
        it.getDisplayName(
            TextStyle.FULL_STANDALONE,
            Locale.getDefault()
        ).replaceFirstChar(Char::uppercase)
    }
    var maxYearMonth by mutableStateOf(YearMonth.now())
    var minYearMonth by mutableStateOf(YearMonth.now())
    var selectedTab by mutableStateOf(0)
    var selectedStartDate by mutableStateOf<LocalDate?>(null)
    var selectedEndDate by mutableStateOf<LocalDate?>(null)
    var yearMonth by mutableStateOf(maxYearMonth)
    var showYearMonthSelection by mutableStateOf(false)
    val selectedRange by derivedStateOf {
        val start = selectedStartDate
        val end = selectedEndDate
        when {
            start != null && end == null -> start..start
            start == null && end != null -> end..end
            start != null && end != null -> start..end
            else -> null
        }
    }
    var selectedStartTime by mutableStateOf(LocalTime.of(12, 0, 0))
    var selectedEndTime by mutableStateOf(LocalTime.of(12, 0, 0))
}

@Composable
fun IntervalSelectionEpicCalendar(
    state: IntervalSelectionEpicCalendarState,
    onSelected: (ClosedRange<LocalDateTime>) -> Unit,
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
                                state.yearMonth = state.yearMonth.minusMonths(1)
                            },
                            content = {
                                LocalResourceIcon(Icons.Default.ArrowLeft)
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
                                text = state.monthTitles[state.yearMonth.monthValue - 1] + " " + state.yearMonth.year,
                                textAlign = TextAlign.Center
                            )
                        }
                        IconButton(
                            onClick = {
                                state.yearMonth = state.yearMonth.plusMonths(1)
                            },
                            content = {
                                LocalResourceIcon(Icons.Default.ArrowRight)
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
                            selectedTabIndex = state.yearMonth.monthValue - 1,
                            modifier = Modifier.fillMaxWidth(),
                            containerColor = Color.Transparent,
                            indicator = {},
                            divider = {},
                            edgePadding = 12.dp
                        ) {
                            repeat(state.monthTitles.size) {
                                val monthTitle = state.monthTitles[it]
                                val isSelected = state.yearMonth.monthValue == it + 1
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
                                                state.yearMonth = state.yearMonth.withMonth(it + 1)
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
                                            Color.Unspecified
                                        }
                                    )
                                }
                            }
                        }
                        ScrollableTabRow(
                            selectedTabIndex = state.maxYearMonth.year - state.yearMonth.year,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp),
                            containerColor = Color.Transparent,
                            indicator = {},
                            divider = {},
                            edgePadding = 12.dp
                        ) {
                            repeat(state.maxYearMonth.year - state.minYearMonth.year) {
                                val year = state.maxYearMonth.year - it
                                val yearTitle = year.toString()
                                val isSelected = state.yearMonth.year == year

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
                                                state.yearMonth = state.yearMonth.withYear(year)
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
                                            Color.Unspecified
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
                yearMonth = state.yearMonth,
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
                        val itemTime = LocalTime.of(it, 0, 0)
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
                                text = timeFormatter.format(itemTime),
                                textAlign = TextAlign.Center,
                                overflow = TextOverflow.Ellipsis,
                                fontSize = 14.sp,
                                color = if (isSelected) {
                                    AppTheme.colorScheme.onPrimary
                                } else {
                                    Color.Unspecified
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
                            if (state.selectedStartDate != null) {
                                state.yearMonth = state.yearMonth
                                    .withMonth(state.selectedStartDate!!.monthValue)
                                    .withYear(state.selectedStartDate!!.year)
                            }
                        }
                    ) {
                        Column(
                            modifier = Modifier.padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Начало",
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = state.selectedStartDate?.let {
                                    it.format(dateFormatter) + "\n" + state.selectedStartTime.format(
                                        timeFormatter
                                    )
                                } ?: "не выбрано",
                                fontWeight = FontWeight.Light,
                                fontSize = 12.sp,
                                style = androidx.compose.ui.text.TextStyle(
                                    lineHeight = 12.sp
                                ),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                    Tab(
                        selected = state.selectedTab == 1,
                        onClick = {
                            state.selectedTab = 1
                            if (state.selectedEndDate != null) {
                                state.yearMonth = state.yearMonth
                                    .withMonth(state.selectedEndDate!!.monthValue)
                                    .withYear(state.selectedEndDate!!.year)
                            }
                        }
                    ) {
                        Column(
                            modifier = Modifier.padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Конец",
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = state.selectedEndDate?.let {
                                    it.format(dateFormatter) + "\n" + state.selectedEndTime.format(
                                        timeFormatter
                                    )
                                } ?: "не выбрано",
                                fontWeight = FontWeight.Light,
                                fontSize = 12.sp,
                                style = androidx.compose.ui.text.TextStyle(
                                    lineHeight = 12.sp
                                ),
                                textAlign = TextAlign.Center
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
                                val start = state.selectedStartDate ?: return@Button
                                val end = state.selectedEndDate ?: return@Button
                                onSelected(
                                    LocalDateTime.of(
                                        start,
                                        state.selectedStartTime
                                    )..LocalDateTime.of(
                                        end,
                                        state.selectedEndTime
                                    )
                                )
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
fun IntervalSelectionEpicCalendarDialog(
    state: IntervalSelectionEpicCalendarState,
    onSelected: (ClosedRange<LocalDateTime>) -> Unit,
    onCancel: () -> Unit,
) {
    Dialog(onDismiss = onCancel) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            IntervalSelectionEpicCalendar(
                state = state,
                onSelected = onSelected,
                intervalsInnerPadding = 8.dp,
                onCancel = onCancel
            )
        }
    }
}

@Composable
fun rememberRangeSelectionEpicCalendarState(
    currentMonth: YearMonth = YearMonth.now(),
    maxMonth: YearMonth = YearMonth.now(),
    minMonth: YearMonth = YearMonth.now(),
    initialRange: ClosedRange<LocalDateTime>? = null,
) = remember(currentMonth, maxMonth, minMonth, initialRange) {
    IntervalSelectionEpicCalendarState().also {
        it.yearMonth = currentMonth
        it.maxYearMonth = maxMonth
        it.minYearMonth = minMonth
        it.selectedStartDate = initialRange?.start?.toLocalDate()
        it.selectedEndDate = initialRange?.endInclusive?.toLocalDate()
        it.selectedStartTime = initialRange?.start?.toLocalTime()
        it.selectedEndTime = initialRange?.endInclusive?.toLocalTime()
    }
}