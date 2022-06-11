package breakbadhabits.android.app.compose.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import breakbadhabits.android.app.R
import breakbadhabits.android.app.data.HabitEventData
import breakbadhabits.android.app.formatter.AbstinenceTimeFormatter
import breakbadhabits.android.app.formatter.DateTimeFormatter
import breakbadhabits.android.app.resources.HabitIconResources
import breakbadhabits.android.app.time.monthEquals
import breakbadhabits.android.app.utils.TikTik
import breakbadhabits.android.app.viewmodel.HabitAnalyzeViewModel
import breakbadhabits.android.app.viewmodel.HabitEventsViewModel
import breakbadhabits.android.app.viewmodel.HabitViewModel
import breakbadhabits.android.compose.molecule.ActionType
import breakbadhabits.android.compose.molecule.Button
import breakbadhabits.android.compose.molecule.Card
import breakbadhabits.android.compose.molecule.Icon
import breakbadhabits.android.compose.molecule.IconButton
import breakbadhabits.android.compose.molecule.Text
import breakbadhabits.android.compose.molecule.Title
import breakbadhabits.android.compose.organism.histogram.Histogram
import breakbadhabits.android.compose.organism.histogram.HistogramState
import breakbadhabits.compose.organism.events.calendar.EventData
import breakbadhabits.compose.organism.events.calendar.EventsCalendar
import breakbadhabits.compose.organism.events.calendar.rememberEventsCalendarState
import breakbadhabits.compose.organism.statistics.StatisticData
import breakbadhabits.compose.organism.statistics.Statistics
import java.time.Instant
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.Calendar
import java.util.Locale

@Composable
fun HabitScreen(
    habitAnalyzeViewModel: HabitAnalyzeViewModel,
    habitViewModel: HabitViewModel,
    habitEventsViewModel: HabitEventsViewModel,
    abstinenceTimeFormatter: AbstinenceTimeFormatter,
    dateTimeFormatter: DateTimeFormatter,
    habitIconResources: HabitIconResources,
    openHabitEventCreation: () -> Unit,
    openHabitEventEditing: (habitEventId: Int) -> Unit,
    openHabitEditing: () -> Unit,
    showALlEvents: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        val currentTime by TikTik.everySecond().collectAsState(initial = System.currentTimeMillis())
        val habitState by habitViewModel.habitFlow.collectAsState()
        (habitState as? HabitViewModel.HabitState.Loaded)?.habit?.let { habit ->
            val lastHabitEvent by habit.lastHabitEvent.collectAsState(initial = null)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        modifier = Modifier.size(44.dp),
                        painter = painterResource(habitIconResources[habit.iconId])
                    )

                    Title(
                        modifier = Modifier.padding(top = 8.dp),
                        text = habit.name
                    )

                    Text(
                        text = when (val event = lastHabitEvent) {
                            null -> stringResource(R.string.habit_noEvents)
                            else -> abstinenceTimeFormatter.format(currentTime - event.time)
                        }
                    )

                    Button(
                        modifier = Modifier.padding(top = 8.dp),
                        onClick = { openHabitEventCreation() },
                        text = stringResource(R.string.habit_resetTime),
                        actionType = ActionType.MAIN
                    )
                }

                IconButton(
                    modifier = Modifier.align(Alignment.TopEnd),
                    onClick = {
                        openHabitEditing()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                    )
                }
            }
        }

        val allEvents by habitAnalyzeViewModel.allEventsFlow.collectAsState()

        if (allEvents.isNotEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 16.dp, end = 16.dp)
            ) {
                val calendarState = rememberEventsCalendarState()
                val allHabitEvents by habitEventsViewModel.habitEventStateFlow.collectAsState()
                val habitEvents = allHabitEvents.filter {
                    Instant.ofEpochMilli(it.timeInMillis)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                        .monthValue == calendarState.value.monthValue
                }.sortedByDescending { it.timeInMillis }

                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Title(
                        modifier = Modifier.padding(16.dp),
                        text = "${
                            calendarState.value.month
                                .getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault())
                                .replaceFirstChar { it.titlecase(Locale.getDefault()) }
                        } ${calendarState.value.year}"
                    )

                    EventsCalendar(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        calendarState = calendarState,
                        events = habitEvents.map {
                            EventData(
                                it.id,
                                it.timeInMillis
                            )
                        },
                        canChangeMonth = false,
                        withHeader = false,
                        horizontalSwipeEnabled = false
                    )

                    habitEvents.forEach { item ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    openHabitEventEditing(item.id)
                                }
                        ) {
                            Column(
                                modifier = Modifier.padding(start = 14.dp, end = 14.dp, top = 4.dp, bottom = 4.dp),
                            ) {
                                Text(
                                    modifier = Modifier.padding(2.dp),
                                    text = dateTimeFormatter.formatDateTime(
                                        Calendar.getInstance().apply {
                                            timeInMillis = item.timeInMillis
                                        },
                                        withoutYear = true
                                    )
                                )
                                Text(
                                    modifier = Modifier.padding(2.dp),
                                    text = if (item.comment.isNullOrEmpty()) {
                                        stringResource(R.string.habitEvents_noComment)
                                    } else {
                                        item.comment
                                    }
                                )
                            }
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .clickable {
                                showALlEvents()
                            }
                    ) {
                        Text(
                            modifier = Modifier.align(Alignment.Center),
                            fontWeight = FontWeight.Medium,
                            text = stringResource(R.string.habit_showAllEvents)
                        )
                    }
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 16.dp, end = 16.dp)
            ) {
                val sortedEvents = allEvents.sortedBy { it.time }

                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Title(
                        modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
                        text = stringResource(R.string.habitAnalyze_abstinenceChart_title)
                    )

                    if (sortedEvents.isNotEmpty()) {
                        Histogram(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp),
                            state = HistogramState(
                                items = mutableListOf<HistogramState.Item>().apply {
                                    for (i in sortedEvents.indices) {
                                        val value = if (i != sortedEvents.indices.last) {
                                            sortedEvents[i + 1].time - sortedEvents[i].time
                                        } else {
                                            currentTime - sortedEvents[i].time
                                        }

                                        add(
                                            HistogramState.Item(
                                                id = sortedEvents[i].id,
                                                value = value.toFloat(),
                                                formattedValue = abstinenceTimeFormatter.format(value, 2)
                                            )
                                        )
                                    }
                                }
                            ),
                            startPadding = 16.dp,
                            endPadding = 16.dp,
                        )
                    }
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 16.dp)
            ) {
                val events by habitAnalyzeViewModel.allEventsFlow.collectAsState()

                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Title(
                        modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 4.dp),
                        text = stringResource(R.string.habitAnalyze_statistics_title)
                    )

                    val abstinenceTimes = calculateAbstinenceTimes(events.sortedBy { it.time }, currentTime)

                    Statistics(
                        modifier = Modifier.fillMaxWidth(),
                        statistics = listOf(
                            StatisticData(
                                name = stringResource(R.string.habitAnalyze_statistics_averageAbstinenceTime),
                                value = abstinenceTimeFormatter.format(abstinenceTimes.average().toLong(), 2)
                            ),
                            StatisticData(
                                name = stringResource(R.string.habitAnalyze_statistics_maxAbstinenceTime),
                                value = abstinenceTimes.maxOrNull()?.let { abstinenceTimeFormatter.format(it, 2) } ?: "-"
                            ),
                            StatisticData(
                                name = stringResource(R.string.habitAnalyze_statistics_minAbstinenceTime),
                                value = abstinenceTimes.dropLast(1).minOrNull()?.let { abstinenceTimeFormatter.format(it, 2) } ?: "-"
                            ),
                            StatisticData(
                                name = stringResource(R.string.habitAnalyze_statistics_timeFromFirstEvent),
                                value = events.minByOrNull { it.time }?.time?.let { abstinenceTimeFormatter.format(currentTime - it, 2) } ?: "-"
                            ),
                            StatisticData(
                                name = stringResource(R.string.habitAnalyze_statistics_countEventsInCurrentMonth),
                                value = events.count { monthEquals(Calendar.getInstance().apply { timeInMillis = it.time }, Calendar.getInstance()) }.toString()
                            ),
                            StatisticData(
                                name = stringResource(R.string.habitAnalyze_statistics_countEventsInPreviousMonth),
                                value = events.count {
                                    monthEquals(
                                        Calendar.getInstance().apply {
                                            timeInMillis = it.time
                                        },
                                        Calendar.getInstance().apply {
                                            if (get(Calendar.MONTH) - 1 == -1) {
                                                set(Calendar.MONTH, 11)
                                                set(Calendar.YEAR, get(Calendar.YEAR) - 1)
                                            } else {
                                                set(Calendar.MONTH, get(Calendar.MONTH) - 1)
                                            }

                                            set(Calendar.DAY_OF_MONTH, 1)
                                        }
                                    )
                                }.toString()
                            ),
                            StatisticData(
                                name = stringResource(R.string.habitAnalyze_statistics_countEvents),
                                value = events.size.toString()
                            )
                        )
                    )
                }
            }
        }
    }
}

private fun calculateAbstinenceTimes(
    habitEvents: List<HabitEventData>,
    currentTime: Long
): List<Long> {
    val points = mutableListOf<Long>()

    for (i in habitEvents.indices) {
        val value = if (i != habitEvents.indices.last) {
            habitEvents[i + 1].time - habitEvents[i].time
        } else {
            currentTime - habitEvents[i].time
        }
        points.add(value)
    }

    return points
}