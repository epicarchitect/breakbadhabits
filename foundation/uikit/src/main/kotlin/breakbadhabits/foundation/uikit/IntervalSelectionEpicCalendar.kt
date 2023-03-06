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

@Composable
fun IntervalSelectionEpicCalendar(
    onSelected: (ClosedRange<LocalDate>) -> Unit,
    onCancel: () -> Unit,
    intervalsInnerPadding: Dp = 0.dp,
    maxYearMonth: YearMonth,
    minYearMonth: YearMonth
) {

    val monthTitles = remember {
        Month.values().map {
            it.getDisplayName(
                TextStyle.FULL_STANDALONE,
                Locale.getDefault()
            ).replaceFirstChar(Char::uppercase)
        }
    }
    var selectedTab by remember { mutableStateOf(0) }
    var startDate by remember { mutableStateOf<LocalDate?>(null) }
    var endDate by remember { mutableStateOf<LocalDate?>(null) }
    var yearMonth by remember { mutableStateOf(maxYearMonth) }
    var showYearMonthSelection by remember { mutableStateOf(false) }

    @Composable
    fun calculateInterval(
        startDate: LocalDate?,
        endDate: LocalDate?
    ): EpicCalendarState.Interval? {
        if (startDate == null) {
            if (endDate != null) {
                return EpicCalendarState.Interval(
                    startDate = endDate,
                    endDate = endDate,
                    backgroundColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            }

            return null
        }
        if (endDate != null) {
            return EpicCalendarState.Interval(
                startDate = startDate,
                endDate = endDate,
                backgroundColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        }
        return EpicCalendarState.Interval(
            startDate = startDate,
            endDate = startDate,
            backgroundColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    }

    Column(modifier = Modifier.padding(top = 8.dp)) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = intervalsInnerPadding),
        ) {
            Column {
                AnimatedVisibility(visible = !showYearMonthSelection) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(
                            onClick = {
                                yearMonth = yearMonth.minusMonths(1)
                            },
                            content = {
                                Icon(Icons.Default.ArrowLeft)
                            },
                        )
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .weight(1f)
                                .padding(horizontal = 4.dp)
                                .clip(RoundedCornerShape(100.dp))
                                .clickable { showYearMonthSelection = !showYearMonthSelection },
                            text = monthTitles[yearMonth.monthValue - 1] + " " + yearMonth.year,
                            textAlign = TextAlign.Center
                        )
                        IconButton(
                            onClick = {
                                yearMonth = yearMonth.plusMonths(1)
                            },
                            content = {
                                Icon(Icons.Default.ArrowRight)
                            }
                        )
                    }
                }
                AnimatedVisibility(visible = showYearMonthSelection) {
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
                                    showYearMonthSelection = false
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                modifier = Modifier.padding(4.dp),
                                imageVector = Icons.Default.KeyboardArrowUp
                            )
                        }

                        ScrollableTabRow(
                            selectedTabIndex = yearMonth.monthValue - 1,
                            modifier = Modifier.fillMaxWidth(),
                            containerColor = Color.Transparent,
                            indicator = {},
                            divider = {},
                            edgePadding = 12.dp
                        ) {
                            repeat(monthTitles.size) {
                                val monthTitle = monthTitles[it]
                                val isSelected = yearMonth.monthValue == it + 1
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
                                                yearMonth = yearMonth.withMonth(it + 1)
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
                            selectedTabIndex = maxYearMonth.year - yearMonth.year,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp),
                            containerColor = Color.Transparent,
                            indicator = {},
                            divider = {},
                            edgePadding = 12.dp
                        ) {
                            repeat(maxYearMonth.year - minYearMonth.year) {
                                val year = maxYearMonth.year - it
                                val yearTitle = year.toString()
                                val isSelected = yearMonth.year == year

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
                                                yearMonth = yearMonth.withYear(year)
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
                yearMonth = yearMonth,
                intervals = listOfNotNull(calculateInterval(startDate, endDate))
            ),
            onDayClick = {
                showYearMonthSelection = false
                if (selectedTab == 0) {
                    startDate = it.date
                } else if (selectedTab == 1) {
                    endDate = it.date
                }
            },
            horizontalInnerPadding = intervalsInnerPadding
        )

        AnimatedVisibility(visible = !showYearMonthSelection) {
            Column {
                TabRow(
                    modifier = Modifier.padding(top = 8.dp),
                    selectedTabIndex = selectedTab,
                    containerColor = Color.Transparent
                ) {
                    val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy")
                    Tab(
                        selected = selectedTab == 0,
                        onClick = {
                            selectedTab = 0
                            if (startDate != null) {
                                yearMonth = yearMonth
                                    .withMonth(startDate!!.monthValue)
                                    .withYear(startDate!!.year)
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
                                text = startDate?.format(formatter) ?: "не выбрано",
                                fontWeight = FontWeight.Light,
                                fontSize = 12.sp
                            )
                        }
                    }
                    Tab(
                        selected = selectedTab == 1,
                        onClick = {
                            selectedTab = 1
                            if (endDate != null) {
                                yearMonth = yearMonth
                                    .withMonth(endDate!!.monthValue)
                                    .withYear(endDate!!.year)
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
                                text = endDate?.format(formatter) ?: "не выбрано",
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
                            if (selectedTab == 0 && endDate == null) {
                                selectedTab = 1
                            } else if (selectedTab == 1 && startDate == null) {
                                selectedTab = 0
                            } else {
                                val start = startDate ?: return@Button
                                val end = endDate ?: return@Button
                                onSelected(start..end)
                            }
                        },
                        text = if (selectedTab == 0 && endDate == null || selectedTab == 1 && startDate == null) "Next" else "Apply",
                        enabled = selectedTab == 0 && startDate != null || selectedTab == 1 && endDate != null,
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
    onSelected: (ClosedRange<LocalDate>) -> Unit,
    onCancel: () -> Unit,
    maxYearMonth: YearMonth,
    minYearMonth: YearMonth
) {
    Dialog(onDismiss = onCancel) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            IntervalSelectionEpicCalendar(
                onSelected = onSelected,
                intervalsInnerPadding = 8.dp,
                onCancel = onCancel,
                maxYearMonth = maxYearMonth,
                minYearMonth = minYearMonth
            )
        }
    }
}