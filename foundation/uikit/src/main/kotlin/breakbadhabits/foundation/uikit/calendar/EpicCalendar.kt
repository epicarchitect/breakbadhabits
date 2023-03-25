package breakbadhabits.foundation.uikit.calendar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import breakbadhabits.foundation.uikit.theme.AppTheme
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.time.temporal.WeekFields
import java.util.*

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

@Composable
fun EpicCalendar(
    modifier: Modifier = Modifier,
    state: EpicCalendarState,
    onDayClick: ((EpicCalendarState.Day) -> Unit)? = null,
    horizontalInnerPadding: Dp = 0.dp,
    rangeColor: Color = AppTheme.colorScheme.primary,
    rangeContentColor: Color = AppTheme.colorScheme.onPrimary,
    dayBadgeText: (EpicCalendarState.Day) -> String? = { null },
) {
    var cellWidth by remember { mutableStateOf(Dp.Unspecified) }
    val cellHeight = remember { 38.dp }
    val density = LocalDensity.current

    Box(modifier.onSizeChanged {
        val cellSpacersWidth = horizontalInnerPadding.value * density.density / 7f * 2f
        cellWidth =
            Dp(((it.width / 7f) - cellSpacersWidth + 1f) / density.density) // +1f to fix right padding
    }) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                state.weekDays.forEachIndexed { index, it ->
                    if (index == 0) {
                        Spacer(modifier = Modifier.width(horizontalInnerPadding))
                    }
                    Box(
                        modifier = Modifier
                            .width(cellWidth)
                            .height(cellHeight)
                    ) {
                        Text(
                            modifier = Modifier.align(Alignment.Center),
                            text = it.name,
                            fontSize = 14.sp
                        )
                    }
                    if (index == 6) {
                        Spacer(modifier = Modifier.width(horizontalInnerPadding))
                    }
                }
            }

            state.days.chunked(7).forEach {
                Row(modifier = Modifier.fillMaxWidth()) {
                    it.forEachIndexed { index, day ->
                        val ranges = remember(state.visibleRanges, day) {
                            state.visibleRanges.filter { day.date in it }
                        }
                        val isDayAtStartOfRange = remember(ranges, day) {
                            ranges.all { it.start == day.date }
                        }
                        val isDayAtEndOfRange = remember(ranges, day) {
                            ranges.all { it.endInclusive == day.date }
                        }

                        val startSpacerBackgroundColor = remember(isDayAtStartOfRange, ranges) {
                            if (isDayAtStartOfRange || ranges.isEmpty()) Color.Transparent else rangeColor
                        }

                        val endSpacerBackgroundColor = remember(isDayAtEndOfRange, ranges) {
                            if (isDayAtEndOfRange || ranges.isEmpty()) Color.Transparent else rangeColor
                        }

                        if (index == 0) {
                            Spacer(
                                modifier = Modifier
                                    .width(horizontalInnerPadding)
                                    .height(cellHeight)
                                    .background(startSpacerBackgroundColor)
                            )
                        }

                        val badgeText = remember(day, dayBadgeText) {
                            dayBadgeText(day)
                        }

                        Box(
                            modifier = Modifier
                                .width(cellWidth)
                                .height(cellHeight)
                        ) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .height(cellHeight)
                                    .width(cellWidth)
                                    .clip(
                                        when {
                                            isDayAtStartOfRange && isDayAtEndOfRange -> FullRangeShape
                                            isDayAtStartOfRange -> StartRangeShape
                                            isDayAtEndOfRange -> EndRangeShape
                                            else -> ZeroRangeShape
                                        }
                                    )
                                    .background(if (ranges.isNotEmpty()) rangeColor else Color.Transparent)
                                    .clip(CircleShape)
                                    .alpha(if (day.inCurrentMonth) 1.0f else 0.5f)
                                    .clickable(enabled = onDayClick != null) {
                                        onDayClick?.invoke(day)
                                    }
                            ) {
                                Text(
                                    modifier = Modifier.align(Alignment.Center),
                                    text = day.date.dayOfMonth.toString(),
                                    textAlign = TextAlign.Center,
                                    color = if (ranges.isNotEmpty()) rangeContentColor else Color.Unspecified,
                                    fontSize = 14.sp
                                )
                            }

                            if (badgeText != null) {
                                Text(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(top = 2.dp, end = 8.dp)
                                        .defaultMinSize(minHeight = 12.dp, minWidth = 12.dp)
                                        .clip(CircleShape)
                                        .background(AppTheme.colorScheme.background.copy(alpha = 0.1f))
                                        .padding(horizontal = 2.dp),
                                    text = badgeText,
                                    color = AppTheme.colorScheme.onPrimary,
                                    fontSize = 8.sp,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        if (index == 6) {
                            Spacer(
                                modifier = Modifier
                                    .width(horizontalInnerPadding)
                                    .height(cellHeight)
                                    .background(endSpacerBackgroundColor)
                            )
                        }
                    }
                }
            }
        }

        AnimatedVisibility(
            modifier = Modifier.matchParentSize(),
            visible = cellWidth == Dp.Unspecified,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Surface {}
        }
    }
}

@Composable
fun rememberEpicCalendarState(
    yearMonth: YearMonth,
    ranges: List<ClosedRange<LocalDate>> = emptyList()
) = remember(yearMonth, ranges) {
    EpicCalendarState().also {
        it.yearMonth = yearMonth
        it.ranges = ranges
    }
}

class EpicCalendarState {
    var yearMonth: YearMonth by mutableStateOf(YearMonth.now())
    val firstDayOfWeek: DayOfWeek by mutableStateOf(calculateFirstDayOfWeek())
    val weekDays: List<WeekDay> by derivedStateOf { calculateWeekDays(firstDayOfWeek) }
    val days: List<Day> by derivedStateOf { calculateDays(yearMonth) }
    var ranges: List<ClosedRange<LocalDate>> by mutableStateOf(emptyList())
    val visibleRanges: List<ClosedRange<LocalDate>> by derivedStateOf {
        ranges.filter { range ->
            days.any { day ->
                day.date in range
            }
        }
    }

    private fun calculateFirstDayOfWeek() = WeekFields.of(Locale.getDefault()).firstDayOfWeek

    private fun calculateWeekDays(firstDayOfWeek: DayOfWeek) = DayOfWeek.values().let {
        val n = 7 - firstDayOfWeek.ordinal
        it.takeLast(n) + it.dropLast(n)
    }.map {
        WeekDay(it.getDisplayName(TextStyle.SHORT, Locale.getDefault()))
    }

    private fun calculateDays(currentYearMonth: YearMonth): List<Day> {
        val previousYearMonth = currentYearMonth.minusMonths(1)
        val nextYearMonth = currentYearMonth.plusMonths(1)
        val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek

        val previousMonthLastDayOfWeek = previousYearMonth.atDay(
            previousYearMonth.lengthOfMonth()
        ).dayOfWeek

        val countLastDaysInPreviousMonth = when (firstDayOfWeek) {
            DayOfWeek.MONDAY -> {
                previousMonthLastDayOfWeek.value
            }

            DayOfWeek.SUNDAY -> {
                if (previousMonthLastDayOfWeek == DayOfWeek.SATURDAY) {
                    0
                } else {
                    previousMonthLastDayOfWeek.value + 1
                }
            }

            else -> error("Unexpected firstDayOfWeek: $firstDayOfWeek")
        }
        val countDaysInCurrentMonth = currentYearMonth.lengthOfMonth()
        val countFirstDaysInNextMonth =
            VISIBLE_DAYS_COUNT - countLastDaysInPreviousMonth - countDaysInCurrentMonth

        val days = mutableListOf<Day>()

        repeat(countLastDaysInPreviousMonth) {
            val date = previousYearMonth.atDay(
                previousYearMonth.lengthOfMonth() + it + 1 - countLastDaysInPreviousMonth
            )

            days.add(
                Day(
                    date = date,
                    inCurrentMonth = false
                )
            )
        }

        repeat(countDaysInCurrentMonth) {
            val date = currentYearMonth.atDay(it + 1)
            days.add(
                Day(
                    date = date,
                    inCurrentMonth = true
                )
            )
        }

        repeat(countFirstDaysInNextMonth) {
            val date = nextYearMonth.atDay(it + 1)
            days.add(
                Day(
                    date = date,
                    inCurrentMonth = false
                )
            )
        }

        return days
    }

    data class Day(
        val date: LocalDate,
        val inCurrentMonth: Boolean
    )

    data class WeekDay(val name: String)

    companion object {
        const val VISIBLE_DAYS_COUNT = 42
    }
}