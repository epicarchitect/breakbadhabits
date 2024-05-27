package epicarchitect.breakbadhabits.uikit.calendar

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
import androidx.compose.foundation.shape.CircleShape
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
import epicarchitect.breakbadhabits.entity.datetime.PlatformDateTimeFormatter
import epicarchitect.breakbadhabits.entity.math.ranges.ascended
import epicarchitect.breakbadhabits.ui.habits.tracks.list.fromEpic
import epicarchitect.breakbadhabits.uikit.Card
import epicarchitect.breakbadhabits.uikit.Dialog
import epicarchitect.breakbadhabits.uikit.Icon
import epicarchitect.breakbadhabits.uikit.IconButton
import epicarchitect.breakbadhabits.uikit.text.Text
import epicarchitect.breakbadhabits.uikit.theme.AppTheme
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

private val ZeroRangeShape = RoundedCornerShape(0)
private val StartRangeShape = RoundedCornerShape(
    topStartPercent = 100,
    bottomStartPercent = 100
)
private val EndRangeShape = RoundedCornerShape(
    topEndPercent = 100,
    bottomEndPercent = 100
)
private val FullRangeShape = CircleShape

// @Composable
// fun EpicCalendar(
//    modifier: Modifier = Modifier,
//    state: EpicCalendarState,
//    onDayClick: ((EpicCalendarState.Day) -> Unit)? = null,
//    horizontalInnerPadding: Dp = 0.dp,
//    rangeColor: Color = AppTheme.colorScheme.primary,
//    rangeContentColor: Color = AppTheme.colorScheme.onPrimary,
//    dayBadgeText: (EpicCalendarState.Day) -> String? = { null },
//    cellHeight: Dp = 38.dp
// ) {
//    var cellWidth by remember { mutableStateOf(Dp.Unspecified) }
//    val density = LocalDensity.current
//
//    Box(modifier.onSizeChanged {
//        val cellSpacersWidth = horizontalInnerPadding.value * density.density / 7f * 2f
//        cellWidth =
//            Dp(((it.width / 7f) - cellSpacersWidth + 1f) / density.density) // +1f to fix right padding
//    }) {
//        Column(
//            verticalArrangement = Arrangement.spacedBy(4.dp)
//        ) {
//            Row(
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                state.weekDays.forEachIndexed { index, it ->
//                    if (index == 0) {
//                        Spacer(modifier = Modifier.width(horizontalInnerPadding))
//                    }
//                    Box(
//                        modifier = Modifier
//                            .width(cellWidth)
//                            .height(cellHeight)
//                    ) {
//                        Text(
//                            modifier = Modifier.align(Alignment.Center),
//                            text = it.name,
//                            fontSize = 14.sp
//                        )
//                    }
//                    if (index == 6) {
//                        Spacer(modifier = Modifier.width(horizontalInnerPadding))
//                    }
//                }
//            }
//
//            state.days.chunked(7).forEach {
//                Row(modifier = Modifier.fillMaxWidth()) {
//                    it.forEachIndexed { index, day ->
//                        val ranges = remember(state.visibleDateRanges, day) {
//                            state.visibleDateRanges.filter { day.date in it }
//                        }
//                        val isDayAtStartOfRange = remember(ranges, day) {
//                            ranges.all { it.start == day.date }
//                        }
//                        val isDayAtEndOfRange = remember(ranges, day) {
//                            ranges.all { it.endInclusive == day.date }
//                        }
//
//                        val startSpacerBackgroundColor = remember(isDayAtStartOfRange, ranges) {
//                            if (isDayAtStartOfRange || ranges.isEmpty()) Color.Transparent else rangeColor
//                        }
//
//                        val endSpacerBackgroundColor = remember(isDayAtEndOfRange, ranges) {
//                            if (isDayAtEndOfRange || ranges.isEmpty()) Color.Transparent else rangeColor
//                        }
//
//                        if (index == 0) {
//                            Spacer(
//                                modifier = Modifier
//                                    .width(horizontalInnerPadding)
//                                    .height(cellHeight)
//                                    .background(startSpacerBackgroundColor)
//                            )
//                        }
//
//                        val badgeText = remember(day, dayBadgeText) {
//                            dayBadgeText(day)
//                        }
//
//                        Box(
//                            modifier = Modifier
//                                .width(cellWidth)
//                                .height(cellHeight)
//                        ) {
//                            Box(
//                                modifier = Modifier
//                                    .align(Alignment.Center)
//                                    .height(cellHeight)
//                                    .width(cellWidth)
//                                    .clip(
//                                        when {
//                                            isDayAtStartOfRange && isDayAtEndOfRange -> FullRangeShape
//                                            isDayAtStartOfRange -> StartRangeShape
//                                            isDayAtEndOfRange -> EndRangeShape
//                                            else -> ZeroRangeShape
//                                        }
//                                    )
//                                    .background(if (ranges.isNotEmpty()) rangeColor else Color.Transparent)
//                                    .clip(CircleShape)
//                                    .alpha(if (day.inCurrentMonth) 1.0f else 0.5f)
//                                    .clickable(enabled = onDayClick != null) {
//                                        onDayClick?.invoke(day)
//                                    }
//                            ) {
//                                Text(
//                                    modifier = Modifier.align(Alignment.Center),
//                                    text = day.date.dayOfMonth.toString(),
//                                    textAlign = TextAlign.Center,
//                                    color = if (ranges.isNotEmpty()) rangeContentColor else Color.Unspecified,
//                                    fontSize = 14.sp
//                                )
//                            }
//
//                            if (badgeText != null) {
//                                Text(
//                                    modifier = Modifier
//                                        .align(Alignment.TopEnd)
//                                        .padding(top = 2.dp, end = 8.dp)
//                                        .defaultMinSize(minHeight = 12.dp, minWidth = 12.dp)
//                                        .clip(CircleShape)
//                                        .background(AppTheme.colorScheme.background.copy(alpha = 0.1f))
//                                        .padding(horizontal = 2.dp),
//                                    text = badgeText,
//                                    color = AppTheme.colorScheme.onPrimary,
//                                    fontSize = 8.sp,
//                                    textAlign = TextAlign.Center
//                                )
//                            }
//                        }
//
//                        if (index == 6) {
//                            Spacer(
//                                modifier = Modifier
//                                    .width(horizontalInnerPadding)
//                                    .height(cellHeight)
//                                    .background(endSpacerBackgroundColor)
//                            )
//                        }
//                    }
//                }
//            }
//        }
//
//        AnimatedVisibility(
//            modifier = Modifier.matchParentSize(),
//            visible = cellWidth == Dp.Unspecified,
//            enter = fadeIn(),
//            exit = fadeOut()
//        ) {
//            Surface {}
//        }
//    }
// }
//
// @Composable
// fun rememberEpicCalendarState(
//    timeZone: TimeZone,
//    monthOfYear: MonthOfYear = MonthOfYear.now(timeZone),
//    ranges: List<ClosedRange<Instant>> = emptyList()
// ) = remember(monthOfYear, timeZone, ranges) {
//    EpicCalendarState().also {
//        it.timeZone = timeZone
//        it.monthOfYear = monthOfYear
//        it.ranges = ranges
//    }
// }
//
// class EpicCalendarState {
//    var timeZone by mutableStateOf(TimeZone.currentSystemDefault())
//    var monthOfYear by mutableStateOf(MonthOfYear.now(timeZone))
//    var ranges: List<ClosedRange<Instant>> by mutableStateOf(emptyList())

//    val firstDayOfWeek: DayOfWeek by mutableStateOf(calculateFirstDayOfWeek())
//    val weekDays: List<WeekDay> by derivedStateOf { calculateWeekDays(firstDayOfWeek) }
//    val days: List<Day> by derivedStateOf { calculateDays(monthOfYear) }
//    val dateRanges by derivedStateOf { ranges.toLocalDateRanges(timeZone) }
//    val visibleDateRanges by derivedStateOf {
//        dateRanges.filter { range ->
//            days.any { day ->
//                day.date in range
//            }
//        }
//    }
//
//    private fun calculateFirstDayOfWeek() = WeekFields.of(Locale.getDefault()).firstDayOfWeek
//
//    private fun calculateWeekDays(firstDayOfWeek: DayOfWeek) = DayOfWeek.values().let {
//        val n = 7 - firstDayOfWeek.ordinal
//        it.takeLast(n) + it.dropLast(n)
//    }.map {
//        WeekDay(it.getDisplayName(TextStyle.SHORT, Locale.getDefault()))
//    }
//
//    private fun calculateDays(currentYearMonth: MonthOfYear): List<Day> {
//        val previousYearMonth = currentYearMonth.previous()
//        val nextYearMonth = currentYearMonth.next()
//        val previousMonthLastDayOfWeek = previousYearMonth.lastDayOfWeek()
//
//        val countLastDaysInPreviousMonth = when (firstDayOfWeek) {
//            DayOfWeek.MONDAY -> previousMonthLastDayOfWeek.value
//            DayOfWeek.SUNDAY -> {
//                if (previousMonthLastDayOfWeek == DayOfWeek.SATURDAY) 0
//                else previousMonthLastDayOfWeek.value + 1
//            }
//
//            else -> error("Unexpected firstDayOfWeek: $firstDayOfWeek")
//        }
//        val countDaysInCurrentMonth = currentYearMonth.length()
//        val countFirstDaysInNextMonth =
//            VISIBLE_DAYS_COUNT - countLastDaysInPreviousMonth - countDaysInCurrentMonth
//
//        val days = mutableListOf<Day>()
//
//        repeat(countLastDaysInPreviousMonth) {
//            val date = previousYearMonth.atDay(
//                previousYearMonth.length() + it + 1 - countLastDaysInPreviousMonth
//            )
//
//            days.add(
//                Day(
//                    date = date,
//                    inCurrentMonth = false
//                )
//            )
//        }
//
//        repeat(countDaysInCurrentMonth) {
//            val date = currentYearMonth.atDay(it + 1)
//            days.add(
//                Day(
//                    date = date,
//                    inCurrentMonth = true
//                )
//            )
//        }
//
//        repeat(countFirstDaysInNextMonth) {
//            val date = nextYearMonth.atDay(it + 1)
//            days.add(
//                Day(
//                    date = date,
//                    inCurrentMonth = false
//                )
//            )
//        }
//
//        return days
//    }
//
//    data class Day(
//        val date: LocalDate,
//        val inCurrentMonth: Boolean
//    )
//
//    data class WeekDay(val name: String)
//
//    companion object {
//        const val VISIBLE_DAYS_COUNT = 42
//    }
// }

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
                            text = PlatformDateTimeFormatter.monthOfYear(state.epicState.pagerState.currentMonth.fromEpic()),
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
                            val monthTitle = PlatformDateTimeFormatter.month(month)
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
                                    text = PlatformDateTimeFormatter.localTime(itemTime),
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
                                        val date = PlatformDateTimeFormatter.localDate(it)
                                        val time = PlatformDateTimeFormatter.localTime(state.selectedStartTime)
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
                                        val date = PlatformDateTimeFormatter.localDate(it)
                                        val time = PlatformDateTimeFormatter.localTime(state.selectedEndTime)
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