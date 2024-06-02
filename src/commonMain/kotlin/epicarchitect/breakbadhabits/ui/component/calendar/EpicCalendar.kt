package epicarchitect.breakbadhabits.ui.component.calendar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import epicarchitect.breakbadhabits.data.AppData
import epicarchitect.breakbadhabits.operation.datetime.toMonthOfYear
import epicarchitect.breakbadhabits.operation.math.ranges.ascended
import epicarchitect.breakbadhabits.ui.format.formatted
import epicarchitect.breakbadhabits.ui.component.Card
import epicarchitect.breakbadhabits.ui.component.Dialog
import epicarchitect.breakbadhabits.ui.component.Icon
import epicarchitect.breakbadhabits.ui.component.IconButton
import epicarchitect.breakbadhabits.ui.component.text.Text
import epicarchitect.breakbadhabits.ui.component.theme.AppTheme
import epicarchitect.calendar.compose.basis.EpicMonth
import epicarchitect.calendar.compose.basis.epicMonth
import epicarchitect.calendar.compose.basis.getByIndex
import epicarchitect.calendar.compose.datepicker.EpicDatePicker
import epicarchitect.calendar.compose.datepicker.config.rememberEpicDatePickerConfig
import epicarchitect.calendar.compose.datepicker.state.EpicDatePickerState
import epicarchitect.calendar.compose.datepicker.state.rememberEpicDatePickerState
import epicarchitect.calendar.compose.pager.state.EpicCalendarPagerState
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.Month

class SelectionCalendarState(
    val epicState: EpicDatePickerState,
    initialSelectedTimes: ClosedRange<LocalTime>
) {
    var showYearMonthSelection by mutableStateOf(false)
    var selectedDateTimeTabIndex by mutableStateOf(0)
    var selectedStartTime by mutableStateOf(initialSelectedTimes.start)
    var selectedEndTime by mutableStateOf(initialSelectedTimes.endInclusive)
}

@Composable
fun rememberSelectionCalendarState(
    initialSelectedDateTime: ClosedRange<LocalDateTime>,
    monthRange: ClosedRange<EpicMonth>
): SelectionCalendarState {
    val epicState = rememberEpicDatePickerState(
        config = rememberEpicDatePickerConfig(
            selectionContainerColor = AppTheme.colorScheme.primary,
            selectionContentColor = AppTheme.colorScheme.onPrimary
        ),
        selectedDates = listOf(
            initialSelectedDateTime.start.date,
            initialSelectedDateTime.endInclusive.date
        ),
        monthRange = monthRange,
        selectionMode = EpicDatePickerState.SelectionMode.Range
    )
    return remember(epicState, initialSelectedDateTime) {
        SelectionCalendarState(
            epicState = epicState,
            initialSelectedTimes = initialSelectedDateTime.start.time..initialSelectedDateTime.endInclusive.time
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
val EpicCalendarPagerState.targetMonth get() = monthRange.getByIndex(pagerState.targetPage)

class RangeSelectionCalendarDialogResources {
    val strings = LocalizedRangeSelectionCalendarDialogStrings(Locale.current)
}

interface RangeSelectionCalendarDialogStrings {
    fun start(): String
    fun end(): String
    fun cancel(): String
    fun apply(): String
}

class RussianRangeSelectionCalendarDialogStrings : RangeSelectionCalendarDialogStrings {
    override fun start() = "Начало"
    override fun end() = "Конец"
    override fun cancel() = "Отмена"
    override fun apply() = "Применить"
}

class EnglishRangeSelectionCalendarDialogStrings : RangeSelectionCalendarDialogStrings {
    override fun start() = "Start"
    override fun end() = "End"
    override fun cancel() = "Cancel"
    override fun apply() = "Apply"
}

class LocalizedRangeSelectionCalendarDialogStrings(locale: Locale) : RangeSelectionCalendarDialogStrings by (
    when (locale.language) {
        "ru" -> RussianRangeSelectionCalendarDialogStrings()
        else -> EnglishRangeSelectionCalendarDialogStrings()
    }
    )

@Composable
fun RangeSelectionCalendarDialog(
    state: SelectionCalendarState,
    resources: RangeSelectionCalendarDialogResources,
    onCancel: () -> Unit,
    onConfirm: (ClosedRange<LocalDateTime>) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    Dialog(
        onDismiss = onCancel
    ) {
        Column {
            AnimatedVisibility(visible = !state.showYearMonthSelection) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = {
                            coroutineScope.launch {
                                state.epicState.pagerState.scrollMonths(-1)
                            }
                        },
                        icon = AppData.resources.icons.commonIcons.arrowLeft
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .weight(1f)
                            .padding(horizontal = 4.dp)
                            .clip(RoundedCornerShape(100.dp))
                            .clickable {
                                state.showYearMonthSelection = !state.showYearMonthSelection
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = state.epicState.pagerState.currentMonth.toMonthOfYear().formatted(),
                            textAlign = TextAlign.Center
                        )
                    }
                    IconButton(
                        onClick = {
                            coroutineScope.launch {
                                state.epicState.pagerState.scrollMonths(1)
                            }
                        },
                        icon = AppData.resources.icons.commonIcons.arrowRight
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
                        Icon(
                            modifier = Modifier.padding(4.dp),
                            icon = AppData.resources.icons.commonIcons.arrowUp
                        )
                    }

                    ScrollableTabRow(
                        selectedTabIndex = state.epicState.pagerState.targetMonth.month.ordinal,
                        modifier = Modifier.fillMaxWidth(),
                        containerColor = Color.Transparent,
                        indicator = {},
                        divider = {},
                        edgePadding = 12.dp
                    ) {
                        repeat(Month.entries.size) {
                            val month = Month.entries[it]
                            val monthTitle = month.formatted()
                            val isSelected = state.epicState.pagerState.targetMonth.month == month
                            Card(
                                modifier = Modifier.padding(4.dp),
                                backgroundColor = if (isSelected) {
                                    AppTheme.colorScheme.primary
                                } else {
                                    AppTheme.colorScheme.onBackground.copy(alpha = 0.1f)
                                },
                                elevation = 0.dp
                            ) {
                                androidx.compose.material3.Text(
                                    modifier = Modifier
                                        .defaultMinSize(minWidth = 90.dp)
                                        .clickable {
                                            coroutineScope.launch {
                                                state.epicState.pagerState.scrollToMonth(
                                                    state.epicState.pagerState.targetMonth.copy(month = month)
                                                )
                                            }
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
                    val yearsCount = state.epicState.pagerState.monthRange.let {
                        it.endInclusive.year - it.start.year
                    }

                    val endYear = state.epicState.pagerState.monthRange.endInclusive.year

                    ScrollableTabRow(
                        selectedTabIndex = yearsCount - (endYear - state.epicState.pagerState.targetMonth.year),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        containerColor = Color.Transparent,
                        indicator = {},
                        divider = {},
                        edgePadding = 12.dp
                    ) {
                        state.epicState.pagerState.monthRange.let {
                            it.start.year..it.endInclusive.year
                        }.forEach { year ->
                            val yearTitle = year.toString()
                            val isSelected = state.epicState.pagerState.targetMonth.year == year

                            Card(
                                modifier = Modifier.padding(4.dp),
                                backgroundColor = if (isSelected) {
                                    AppTheme.colorScheme.primary
                                } else {
                                    AppTheme.colorScheme.onBackground.copy(alpha = 0.1f)
                                },
                                elevation = 0.dp,
                            ) {
                                androidx.compose.material3.Text(
                                    modifier = Modifier
                                        .defaultMinSize(minWidth = 90.dp)
                                        .clickable {
                                            coroutineScope.launch {
                                                state.epicState.pagerState.scrollToMonth(
                                                    state.epicState.pagerState.currentMonth.copy(year = year)
                                                )
                                            }
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

            EpicDatePicker(
                modifier = Modifier.pointerInput(Unit) {
                    detectVerticalDragGestures { change, _ ->
                        if (change.previousPosition.y > change.position.y) {
                            state.showYearMonthSelection = false
                        }
                    }
                },
                state = state.epicState
            )

            AnimatedVisibility(visible = !state.showYearMonthSelection && state.epicState.selectedDates.isNotEmpty()) {
                Column {
                    val selectedTime = if (state.selectedDateTimeTabIndex == 0) {
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
                                modifier = Modifier.padding(horizontal = 4.dp),
                                backgroundColor = if (isSelected) {
                                    AppTheme.colorScheme.primary
                                } else {
                                    AppTheme.colorScheme.onBackground.copy(alpha = 0.1f)
                                },
                                elevation = 0.dp,
                            ) {
                                androidx.compose.material3.Text(
                                    modifier = Modifier
                                        .defaultMinSize(minWidth = 90.dp)
                                        .clickable {
                                            if (state.selectedDateTimeTabIndex == 0) {
                                                state.selectedStartTime = itemTime
                                            } else {
                                                state.selectedEndTime = itemTime
                                            }
                                        }
                                        .padding(
                                            vertical = 8.dp,
                                            horizontal = 20.dp
                                        ),
                                    text = itemTime.formatted(),
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
                        selectedTabIndex = state.selectedDateTimeTabIndex,
                        containerColor = Color.Transparent
                    ) {
                        Tab(
                            selected = state.selectedDateTimeTabIndex == 0,
                            onClick = {
                                coroutineScope.launch {
                                    state.selectedDateTimeTabIndex = 0
                                    state.epicState.selectedDates.minOrNull()?.let {
                                        state.epicState.pagerState.scrollToMonth(it.epicMonth)
                                    }
                                }
                            }
                        ) {
                            Column(
                                modifier = Modifier.padding(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                androidx.compose.material3.Text(
                                    text = resources.strings.start(),
                                    fontWeight = FontWeight.Bold,
                                    color = AppTheme.colorScheme.onSurface
                                )

                                androidx.compose.material3.Text(
                                    text = state.epicState.selectedDates.minOrNull()?.let {
                                        val date = it.formatted()
                                        val time = state.selectedStartTime.formatted()
                                        "$date\n$time"
                                    } ?: "",
                                    fontWeight = FontWeight.Light,
                                    fontSize = 12.sp,
                                    style = TextStyle(
                                        lineHeight = 12.sp
                                    ),
                                    textAlign = TextAlign.Center,
                                    color = AppTheme.colorScheme.onSurface
                                )
                            }
                        }
                        Tab(
                            selected = state.selectedDateTimeTabIndex == 1,
                            onClick = {
                                coroutineScope.launch {
                                    state.selectedDateTimeTabIndex = 1
                                    state.epicState.selectedDates.maxOrNull()?.let {
                                        state.epicState.pagerState.scrollToMonth(it.epicMonth)
                                    }
                                }
                            }
                        ) {
                            Column(
                                modifier = Modifier.padding(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                androidx.compose.material3.Text(
                                    text = resources.strings.end(),
                                    fontWeight = FontWeight.Bold,
                                    color = AppTheme.colorScheme.onSurface
                                )

                                androidx.compose.material3.Text(
                                    text = state.epicState.selectedDates.maxOrNull()?.let {
                                        val date = it.formatted()
                                        val time = state.selectedEndTime.formatted()
                                        "$date\n$time"
                                    } ?: "",
                                    fontWeight = FontWeight.Light,
                                    fontSize = 12.sp,
                                    style = TextStyle(
                                        lineHeight = 12.sp
                                    ),
                                    textAlign = TextAlign.Center,
                                    color = AppTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, top = 4.dp, end = 12.dp, bottom = 4.dp)
            ) {
                androidx.compose.material3.Button(
                    onClick = {
                        if (state.showYearMonthSelection) {
                            state.showYearMonthSelection = false
                        } else {
                            onCancel()
                        }
                    },
                    elevation = ButtonDefaults.buttonElevation(0.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AppTheme.colorScheme.surface,
                        contentColor = AppTheme.colorScheme.onSurface
                    ),
                ) {
                    androidx.compose.material3.Text(resources.strings.cancel())
                }
                Spacer(Modifier.weight(1f))
                AnimatedVisibility(visible = !state.showYearMonthSelection && state.epicState.selectedDates.isNotEmpty()) {
                    androidx.compose.material3.Button(
                        onClick = {
                            val range = LocalDateTime(
                                date = state.epicState.selectedDates.min(),
                                time = state.selectedStartTime
                            )..LocalDateTime(
                                date = state.epicState.selectedDates.max(),
                                time = state.selectedEndTime
                            )
                            onConfirm(range.ascended())
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AppTheme.colorScheme.surface,
                            contentColor = AppTheme.colorScheme.primary
                        ),
                        elevation = ButtonDefaults.buttonElevation(0.dp)
                    ) {
                        androidx.compose.material3.Text(resources.strings.apply())
                    }
                }
            }
        }
    }
}