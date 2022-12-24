package breakbadhabits.ui.kit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.boguszpawlowski.composecalendar.StaticCalendar
import io.github.boguszpawlowski.composecalendar.rememberCalendarState
import java.time.Instant
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun EventsCalendar(
    modifier: Modifier = Modifier,
    calendarState: MutableState<YearMonth>,
    events: List<Long>,
    canChangeMonth: Boolean = true,
    withHeader: Boolean = true,
    horizontalSwipeEnabled: Boolean = true
) {
    val staticCalendarState = rememberCalendarState(calendarState.value)
    calendarState.value = staticCalendarState.monthState.currentMonth


    StaticCalendar(
        modifier = modifier,
        calendarState = staticCalendarState,
        horizontalSwipeEnabled = horizontalSwipeEnabled,
        monthHeader = { state ->
            if (withHeader) {
                Box(
                    modifier = Modifier.padding(bottom = 16.dp),
                ) {
                    if (canChangeMonth) {
                        IconButton(
                            modifier = Modifier.align(Alignment.CenterStart),
                            onClick = {
                                state.currentMonth = state.currentMonth.minusMonths(1)
                            }
                        ) {
                            Icon(Icons.Default.KeyboardArrowLeft)
                        }


                        IconButton(
                            modifier = Modifier.align(Alignment.CenterEnd),
                            onClick = {
                                state.currentMonth = state.currentMonth.plusMonths(1)
                            }
                        ) {
                            Icon(Icons.Default.KeyboardArrowRight)
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = state.currentMonth.month
                                .getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault())
                                .lowercase()
                                .replaceFirstChar { it.titlecase() },
                            style = MaterialTheme.typography.body1,
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = state.currentMonth.year.toString(),
                            style = MaterialTheme.typography.body1
                        )
                    }
                }
            }
        },
        dayContent = { state ->
            val eventsCount = events.count {
                val itemDate = Instant.ofEpochMilli(it)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()

                itemDate.dayOfMonth == state.date.dayOfMonth
                        && itemDate.monthValue == state.date.monthValue
                        && itemDate.year == state.date.year
            }

            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Card(
                    modifier = Modifier
                        .size(44.dp)
                        .padding(4.dp)
                        .align(Alignment.Center),
                    shape = RoundedCornerShape(100.dp),
                    elevation = 0.dp
                ) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .alpha(if (state.isFromCurrentMonth) 1.0f else 0.4f)
                            .background(
                                if (eventsCount > 0) {
                                    MaterialTheme.colors.primary
                                } else {
                                    Color.Transparent
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = state.date.dayOfMonth.toString(),
                            color = if (eventsCount > 0) {
                                Color.White
                            } else {
                                MaterialTheme.colors.onBackground
                            }
                        )
                    }
                }
            }

            if (eventsCount > 1) {
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Card(
                        modifier = Modifier
                            .padding(start = 32.dp, top = 2.dp)
                            .defaultMinSize(16.dp)
                            .align(Alignment.TopCenter),
                        shape = RoundedCornerShape(100.dp),
                        elevation = 4.dp,
                        backgroundColor = Color.White
                    ) {
                        Box(
                            modifier = Modifier.padding(2.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (eventsCount > 99) {
                                    "99+"
                                } else {
                                    eventsCount.toString()
                                },
                                color = Color.Black,
                                fontSize = 8.sp
                            )
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun rememberEventsCalendarState(
    initialMonth: YearMonth = YearMonth.now()
) = remember { mutableStateOf(initialMonth) }