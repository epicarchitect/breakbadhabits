package breakbadhabits.ui.kit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.time.temporal.WeekFields
import java.util.Locale


@Composable
fun EpicCalendar(
    modifier: Modifier = Modifier,
    state: EpicCalendarState,
    intervals: List<EpicCalendarInterval>,
    horizontalInnerPadding: Dp,
    dayDefaultColor: Color
) {
    var cellWidth by remember { mutableStateOf(Dp.Unspecified) }
    val cellHeight = remember { 44.dp }
    val density = LocalDensity.current

    Box(modifier.onSizeChanged {
        val innerPaddingOfCell = horizontalInnerPadding.value * density.density / 7 * 2
        cellWidth = Dp(((it.width / 7f) - innerPaddingOfCell) / density.density)
    }) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
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
                            text = it.name
                        )
                    }
                    if (index == 6) {
                        Spacer(modifier = Modifier.width(horizontalInnerPadding))
                    }
                }
            }

            state.days.chunked(7).forEach {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    it.forEachIndexed { index, day ->
                        val interval = intervals.find { day.date in it.startDate..it.endDate }
                        val isDayAtStartOfInterval = interval?.startDate?.let { it == day.date }
                        val isDayAtEndOfInterval = interval?.endDate?.let { it == day.date }

                        if (index == 0) {
                            if (isDayAtStartOfInterval == true) {
                                Spacer(
                                    modifier = Modifier
                                        .width(horizontalInnerPadding)
                                        .height(cellHeight)
                                        .background(dayDefaultColor)
                                )
                            } else {
                                Spacer(
                                    modifier = Modifier
                                        .width(horizontalInnerPadding)
                                        .height(cellHeight)
                                        .background(interval?.color ?: dayDefaultColor)
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .width(cellWidth)
                                .height(cellHeight)
                                .alpha(if (day.inCurrentMonth) 1.0f else 0.5f)
                                .clip(
                                    when {
                                        isDayAtStartOfInterval != null && isDayAtStartOfInterval && isDayAtEndOfInterval != null && isDayAtEndOfInterval -> {
                                            RoundedCornerShape(
                                                topStart = 100.dp,
                                                bottomStart = 100.dp,
                                                topEnd = 100.dp,
                                                bottomEnd = 100.dp
                                            )
                                        }

                                        isDayAtStartOfInterval != null && isDayAtStartOfInterval -> {
                                            RoundedCornerShape(
                                                topStart = 100.dp,
                                                bottomStart = 100.dp
                                            )
                                        }

                                        isDayAtEndOfInterval != null && isDayAtEndOfInterval -> {
                                            RoundedCornerShape(
                                                topEnd = 100.dp,
                                                bottomEnd = 100.dp
                                            )
                                        }

                                        else -> {
                                            RoundedCornerShape(0.dp)
                                        }
                                    }
                                )
                                .background(interval?.color ?: dayDefaultColor),
                        ) {
                            Text(
                                modifier = Modifier.align(Alignment.Center),
                                text = day.date.dayOfMonth.toString()
                            )
                        }

                        if (index == 6) {
                            if (isDayAtEndOfInterval == true) {
                                Spacer(
                                    modifier = Modifier
                                        .width(horizontalInnerPadding)
                                        .height(cellHeight)
                                        .background(dayDefaultColor)
                                )
                            } else {
                                Spacer(
                                    modifier = Modifier
                                        .width(horizontalInnerPadding)
                                        .height(cellHeight)
                                        .background(interval?.color ?: dayDefaultColor)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun rememberEpicCalendarState(
    yearMonth: YearMonth
) = remember(yearMonth) {
    calculateState(yearMonth)
}

data class EpicCalendarState(
    val weekDays: List<WeekDay>,
    val days: List<Day>
) {
    data class Day(val date: LocalDate, val inCurrentMonth: Boolean)
    data class WeekDay(val name: String)
}

data class EpicCalendarInterval(
    val startDate: LocalDate,
    val endDate: LocalDate,
    val color: Color
)

private fun calculateState(
    yearMonth: YearMonth
): EpicCalendarState {
    val previousYearMonth = yearMonth.minusMonths(1)
    val nextYearMonth = yearMonth.plusMonths(1)
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

        else -> {
            error("Unexpected firstDayOfWeek: $firstDayOfWeek")
        }
    }
    val countDaysInCurrentMonth = yearMonth.lengthOfMonth()
    val countFirstDaysInNextMonth = 42 - countLastDaysInPreviousMonth - countDaysInCurrentMonth

    val weekDays = calculateWeekDays(firstDayOfWeek)

    val days = mutableListOf<EpicCalendarState.Day>()

    repeat(countLastDaysInPreviousMonth) {
        days.add(
            EpicCalendarState.Day(
                date = previousYearMonth.atDay(
                    previousYearMonth.lengthOfMonth() + it + 1 - countLastDaysInPreviousMonth
                ),
                inCurrentMonth = false
            )
        )
    }

    repeat(countDaysInCurrentMonth) {
        days.add(
            EpicCalendarState.Day(
                date = yearMonth.atDay(it + 1),
                inCurrentMonth = true
            )
        )
    }

    repeat(countFirstDaysInNextMonth) {
        days.add(
            EpicCalendarState.Day(
                date = nextYearMonth.atDay(it + 1),
                inCurrentMonth = false
            )
        )
    }

    return EpicCalendarState(
        weekDays = weekDays,
        days = days
    )
}


private fun calculateWeekDays(firstDayOfWeek: DayOfWeek) = DayOfWeek.values().let {
    val n = 7 - firstDayOfWeek.ordinal
    it.takeLast(n) + it.dropLast(n)
}.map {
    EpicCalendarState.WeekDay(it.getDisplayName(TextStyle.SHORT, Locale.getDefault()))
}
