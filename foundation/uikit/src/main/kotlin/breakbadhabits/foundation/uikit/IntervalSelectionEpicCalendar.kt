package breakbadhabits.foundation.uikit

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowLeft
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import breakbadhabits.foundation.uikit.button.Button
import breakbadhabits.foundation.uikit.button.InteractionType
import breakbadhabits.foundation.uikit.text.Text
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth
import java.time.format.DateTimeFormatter
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
    var startDate by mutableStateOf<LocalDate?>(null)
    var endDate by mutableStateOf<LocalDate?>(null)
    var yearMonth by mutableStateOf(maxYearMonth)
    var showYearMonthSelection by mutableStateOf(false)
}

@Composable
fun IntervalSelectionEpicCalendar(
    state: IntervalSelectionEpicCalendarState,
    onSelected: (ClosedRange<LocalDate>) -> Unit,
    onCancel: () -> Unit,
    intervalsInnerPadding: Dp = 0.dp,
) {
    @Composable
    fun calculateInterval(
        startDate: LocalDate?,
        endDate: LocalDate?
    ): EpicCalendarState.Range? {
        if (startDate == null) {
            if (endDate != null) {
                return EpicCalendarState.Range(
                    start = endDate,
                    endInclusive = endDate
                )
            }

            return null
        }
        if (endDate != null) {
            return EpicCalendarState.Range(
                start = startDate,
                endInclusive = endDate
            )
        }
        return EpicCalendarState.Range(
            start = startDate,
            endInclusive = startDate
        )
    }

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
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .weight(1f)
                                .padding(horizontal = 4.dp)
                                .clip(RoundedCornerShape(100.dp))
                                .clickable {
                                    state.showYearMonthSelection = !state.showYearMonthSelection
                                },
                            text = state.monthTitles[state.yearMonth.monthValue - 1] + " " + state.yearMonth.year,
                            textAlign = TextAlign.Center
                        )
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
                                        MaterialTheme.colorScheme.primary
                                    } else {
                                        MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f)
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
                                        fontSize = 14.sp
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
                                        MaterialTheme.colorScheme.primary
                                    } else {
                                        MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f)
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
                                        fontSize = 14.sp
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
                ranges = listOfNotNull(calculateInterval(state.startDate, state.endDate))
            ),
            onDayClick = {
                state.showYearMonthSelection = false
                if (state.selectedTab == 0) {
                    state.startDate = it.date
                } else if (state.selectedTab == 1) {
                    state.endDate = it.date
                }
            },
            horizontalInnerPadding = intervalsInnerPadding
        )

        AnimatedVisibility(visible = !state.showYearMonthSelection) {
            Column {
                TabRow(
                    modifier = Modifier.padding(top = 8.dp),
                    selectedTabIndex = state.selectedTab,
                    containerColor = Color.Transparent
                ) {
                    val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy")
                    Tab(
                        selected = state.selectedTab == 0,
                        onClick = {
                            state.selectedTab = 0
                            if (state.startDate != null) {
                                state.yearMonth = state.yearMonth
                                    .withMonth(state.startDate!!.monthValue)
                                    .withYear(state.startDate!!.year)
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
                                text = state.startDate?.format(formatter) ?: "не выбрано",
                                fontWeight = FontWeight.Light,
                                fontSize = 12.sp
                            )
                        }
                    }
                    Tab(
                        selected = state.selectedTab == 1,
                        onClick = {
                            state.selectedTab = 1
                            if (state.endDate != null) {
                                state.yearMonth = state.yearMonth
                                    .withMonth(state.endDate!!.monthValue)
                                    .withYear(state.endDate!!.year)
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
                                text = state.endDate?.format(formatter) ?: "не выбрано",
                                fontWeight = FontWeight.Light,
                                fontSize = 12.sp
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
                        text = "Cancel",
                        elevation = 0.dp
                    )
                    Spacer(Modifier.padding(4.dp))
                    Button(
                        onClick = {
                            if (state.selectedTab == 0 && state.endDate == null) {
                                state.selectedTab = 1
                            } else if (state.selectedTab == 1 && state.startDate == null) {
                                state.selectedTab = 0
                            } else {
                                val start = state.startDate ?: return@Button
                                val end = state.endDate ?: return@Button
                                onSelected(start..end)
                            }
                        },
                        text = if (state.selectedTab == 0 && state.endDate == null || state.selectedTab == 1 && state.startDate == null) "Next" else "Apply",
                        enabled = state.selectedTab == 0 && state.startDate != null || state.selectedTab == 1 && state.endDate != null,
                        interactionType = InteractionType.MAIN,
                        elevation = 0.dp
                    )
                }
            }
        }
    }
}

@Composable
fun IntervalSelectionEpicCalendarDialog(
    state: IntervalSelectionEpicCalendarState,
    onSelected: (ClosedRange<LocalDate>) -> Unit,
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
    initialRange: ClosedRange<LocalDate>? = null
) = remember(currentMonth, maxMonth, minMonth, initialRange) {
    IntervalSelectionEpicCalendarState().also {
        it.yearMonth = currentMonth
        it.maxYearMonth = maxMonth
        it.minYearMonth = minMonth
        it.startDate = initialRange?.start
        it.endDate = initialRange?.endInclusive
    }
}