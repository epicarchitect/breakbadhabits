@file:OptIn(ExperimentalMaterialApi::class)

package breakbadhabits.ui.kit

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Chip
import androidx.compose.material.ChipDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowLeft
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun IntervalSelectionEpicCalendar(
    modifier: Modifier = Modifier,
    onSelected: (LocalDate?, LocalDate?) -> Unit,
    horizontalInnerPadding: Dp = 0.dp,
) {
    val density = LocalDensity.current.density
    var selectedTab by remember { mutableStateOf<Int>(0) }
    var startDate by remember { mutableStateOf<LocalDate?>(null) }
    var endDate by remember { mutableStateOf<LocalDate?>(null) }
    var yearMonth by remember { mutableStateOf(YearMonth.now()) }

    fun calculateInterval(
        startDate: LocalDate?,
        endDate: LocalDate?
    ): EpicCalendarState.Interval? {
        if (startDate == null) {
            return null
        }
        if (endDate != null) {
            return EpicCalendarState.Interval(
                startDate = startDate,
                endDate = endDate,
                color = Color.Red.copy(alpha = 0.5f)
            )
        }
        return EpicCalendarState.Interval(
            startDate = startDate,
            endDate = startDate,
            color = Color.Red.copy(alpha = 0.5f)
        )
    }

    Column(modifier) {
        var showYearMonthSelection by remember { mutableStateOf(false) }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = horizontalInnerPadding),
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
                            text = Month.of(yearMonth.monthValue).getDisplayName(
                                TextStyle.FULL_STANDALONE,
                                Locale.getDefault()
                            ).replaceFirstChar { it.uppercase() } + " " + yearMonth.year,
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

                        val monthsRowState = rememberLazyListState()
                        val yearsRowState = rememberLazyListState()

                        LaunchedEffect(yearMonth.monthValue) {
                            monthsRowState.animateScrollToItem(
                                yearMonth.monthValue - 1,
                                scrollOffset = (-yearMonthSelectionBoxSize.width / 2f + (100f + 34f) / 2 * density).toInt()
                            )
                        }
                        LaunchedEffect(yearMonth.year) {
                            yearsRowState.animateScrollToItem(
                                (2023 - 1949) - (yearMonth.year - 1949),
                                scrollOffset = (-yearMonthSelectionBoxSize.width / 2f + (80f + 34f) / 2 * density).toInt()
                            )
                        }

                        LazyRow(
                            state = monthsRowState,
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp)
                        ) {
                            items(12) {
                                Chip(
                                    modifier = Modifier.width(100.dp),
                                    onClick = {
                                        yearMonth = yearMonth.withMonth(it + 1)
                                    },
                                    colors = ChipDefaults.chipColors(
                                        backgroundColor = if (yearMonth.monthValue == it + 1) {
                                            Color.Red.copy(alpha = 0.5f)
                                        } else {
                                            Color.LightGray.copy(alpha = 0.5f)
                                        },
                                    ),
                                ) {
                                    Box(
                                        modifier = Modifier.fillMaxWidth(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            Month.of(it + 1).getDisplayName(
                                                TextStyle.FULL_STANDALONE,
                                                Locale.getDefault()
                                            ).replaceFirstChar { it.uppercase() }
                                        )
                                    }
                                }
                            }
                        }
                        LazyRow(
                            state = yearsRowState,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp)
                        ) {
                            val now = YearMonth.now()
                            items(now.year - 1949) {
                                val year = now.year - it
                                Chip(
                                    modifier = Modifier.width(80.dp),
                                    onClick = {
                                        yearMonth = yearMonth.withYear(year)
                                    },
                                    colors = ChipDefaults.chipColors(
                                        backgroundColor = if (yearMonth.year == year) {
                                            Color.Red.copy(alpha = 0.5f)
                                        } else {
                                            Color.LightGray.copy(alpha = 0.5f)
                                        },
                                    )
                                ) {
                                    Box(
                                        modifier = Modifier.fillMaxWidth(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(year.toString())
                                    }
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
                when (selectedTab) {
                    0 -> {
                        startDate = it.date
                    }

                    1 -> {
                        endDate = it.date
                    }
                }

                onSelected(startDate, endDate)
            },
            horizontalInnerPadding = horizontalInnerPadding
        )

        TabRow(selectedTabIndex = selectedTab) {
            val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")
            Tab(
                selected = selectedTab == 0,
                onClick = {
                    selectedTab = 0
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
    }
}