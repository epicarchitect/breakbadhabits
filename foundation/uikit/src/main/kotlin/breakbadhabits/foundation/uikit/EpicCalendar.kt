package breakbadhabits.foundation.uikit

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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import breakbadhabits.foundation.uikit.text.Text
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.time.temporal.WeekFields
import java.util.*


@Composable
fun EpicCalendar(
    modifier: Modifier = Modifier,
    state: EpicCalendarState,
    onDayClick: ((EpicCalendarState.Day) -> Unit)? = null,
    horizontalInnerPadding: Dp = 0.dp,
    rangeColor: Color = MaterialTheme.colorScheme.primary,
    rangeContentColor: Color = MaterialTheme.colorScheme.onPrimary
) {
    var cellWidth by remember { mutableStateOf(Dp.Unspecified) }
    val cellHeight = remember { 44.dp }
    val density = LocalDensity.current

    Box(modifier.onSizeChanged {
        val cellSpacersWidth = horizontalInnerPadding.value * density.density / 7f * 2f
        cellWidth =
            Dp(((it.width / 7f) - cellSpacersWidth + 1f) / density.density) // +1f to fix right padding
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
                Row(modifier = Modifier.fillMaxWidth()) {
                    it.forEachIndexed { index, day ->
                        val range = state.ranges.find { day.date in it }
                        val isDayAtStartOfInterval = range?.start?.let { it == day.date }
                        val isDayAtEndOfInterval = range?.endInclusive?.let { it == day.date }

                        if (index == 0) {
                            Spacer(
                                modifier = Modifier
                                    .width(horizontalInnerPadding)
                                    .height(cellHeight)
                                    .background(
                                        if (isDayAtStartOfInterval == true || range == null) Color.Transparent
                                        else rangeColor
                                    )
                            )
                        }

                        Box(
                            modifier = Modifier
                                .width(cellWidth)
                                .height(cellHeight)
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
                                .background(if (range != null) rangeColor else Color.Transparent),
                        ) {
                            Text(
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .height(cellHeight)
                                    .width(cellWidth)
                                    .clip(RoundedCornerShape(100.dp))
                                    .alpha(if (day.inCurrentMonth) 1.0f else 0.5f)
                                    .clickable(enabled = onDayClick != null) {
                                        onDayClick?.invoke(day)
                                    },
                                text = day.date.dayOfMonth.toString(),
                                textAlign = TextAlign.Center,
                                color = if (range != null) rangeContentColor else Color.Unspecified
                            )
                        }

                        if (index == 6) {
                            Spacer(
                                modifier = Modifier
                                    .width(horizontalInnerPadding)
                                    .height(cellHeight)
                                    .background(
                                        if (isDayAtEndOfInterval == true || range == null) Color.Transparent
                                        else rangeColor
                                    )
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
    ranges: List<EpicCalendarState.Range> = emptyList()
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
    var ranges: List<Range> by mutableStateOf(emptyList())

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

    data class Range(
        override val endInclusive: LocalDate,
        override val start: LocalDate
    ) : ClosedRange<LocalDate>

    companion object {
        const val VISIBLE_DAYS_COUNT = 42
    }
}