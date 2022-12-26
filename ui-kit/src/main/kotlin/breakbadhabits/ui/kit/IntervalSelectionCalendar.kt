package breakbadhabits.ui.kit

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.time.temporal.WeekFields
import java.util.Locale


@Composable
fun IntervalSelectionCalendar(
    yearMonth: YearMonth
) {
    val state = remember(yearMonth) { calculateState(yearMonth) }
    IntervalSelectionCalendarContent(state)
}

@Composable
private fun IntervalSelectionCalendarContent(state: State) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(horizontalArrangement = Arrangement.SpaceBetween) {
            state.weekDays.forEach {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .padding(2.dp)
                ) {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = it.name
                    )
                }
            }
        }


        state.days.chunked(7).forEach {
            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                it.forEach {
                    Card(
                        modifier = Modifier
                            .size(44.dp)
                            .padding(2.dp)
                            .alpha(if (it.inCurrentMonth) 1.0f else 0.5f)
                    ) {
                        Box {
                            Text(
                                modifier = Modifier.align(Alignment.Center),
                                text = it.date.dayOfMonth.toString()
                            )
                        }
                    }
                }
            }
        }
    }
}

private data class State(
    val weekDays: List<WeekDay>,
    val days: List<Day>
) {
    data class Day(val date: LocalDate, val inCurrentMonth: Boolean)
    data class WeekDay(val name: String)
}

private fun calculateState(yearMonth: YearMonth): State {
    val previousYearMonth = yearMonth.minusMonths(1)
    val nextYearMonth = yearMonth.plusMonths(1)
    val firstDayOfMonth = yearMonth.atDay(1)
    val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek


    val previousMonthLastDayOfWeek =
        previousYearMonth.atDay(previousYearMonth.lengthOfMonth()).dayOfWeek
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

    val weekDays = DayOfWeek.values().rotateRight(7 - firstDayOfWeek.ordinal).map {
        State.WeekDay(it.getDisplayName(TextStyle.SHORT, Locale.getDefault()))
    }

    val days = mutableListOf<State.Day>()

    Log.d("test123", "countLastDaysInPreviousMonth: $countLastDaysInPreviousMonth")
    repeat(countLastDaysInPreviousMonth) {
        days.add(
            State.Day(
                date = previousYearMonth.atDay(
                    previousYearMonth.lengthOfMonth() + it + 1 - countLastDaysInPreviousMonth
                ),
                inCurrentMonth = false
            )
        )
    }

    repeat(countDaysInCurrentMonth) {
        days.add(
            State.Day(
                date = yearMonth.atDay(it + 1),
                inCurrentMonth = true
            )
        )
    }

    repeat(countFirstDaysInNextMonth) {
        days.add(
            State.Day(
                date = nextYearMonth.atDay(it + 1),
                inCurrentMonth = false
            )
        )
    }

    return State(weekDays, days)
}

private fun <T> Array<T>.rotateRight(n: Int): List<T> = takeLast(n) + dropLast(n)
private infix fun DayOfWeek.daysUntil(other: DayOfWeek) = (7 + (value - other.value)) % 7