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
import breakbadhabits.android.app.createCurrentMonthHabitEventCountFeature
import breakbadhabits.android.app.createCurrentMonthHabitEventIdsFeature
import breakbadhabits.android.app.createCurrentMonthHabitEventTimesFeature
import breakbadhabits.android.app.createHabitAbstinenceTimeFeature
import breakbadhabits.android.app.createHabitAbstinenceTimesFeature
import breakbadhabits.android.app.createHabitAverageAbstinenceTimeFeature
import breakbadhabits.android.app.createHabitEventCommentFeature
import breakbadhabits.android.app.createHabitEventCountFeature
import breakbadhabits.android.app.createHabitEventIdsFeature
import breakbadhabits.android.app.createHabitEventTimeFeature
import breakbadhabits.android.app.createHabitIconIdFeature
import breakbadhabits.android.app.createHabitMaxAbstinenceTimeFeature
import breakbadhabits.android.app.createHabitMinAbstinenceTimeFeature
import breakbadhabits.android.app.createHabitNameFeature
import breakbadhabits.android.app.createHabitTimeSinceFirstEventFeature
import breakbadhabits.android.app.createPreviousMonthHabitEventCountFeature
import breakbadhabits.android.app.formatter.AbstinenceTimeFormatter
import breakbadhabits.android.app.formatter.DateTimeFormatter
import breakbadhabits.android.app.resources.HabitIconResources
import breakbadhabits.android.app.utils.get
import breakbadhabits.android.compose.ui.Button
import breakbadhabits.android.compose.ui.Card
import breakbadhabits.android.compose.ui.EventsCalendar
import breakbadhabits.android.compose.ui.Histogram
import breakbadhabits.android.compose.ui.Icon
import breakbadhabits.android.compose.ui.IconButton
import breakbadhabits.android.compose.ui.InteractionType
import breakbadhabits.android.compose.ui.StatisticData
import breakbadhabits.android.compose.ui.Statistics
import breakbadhabits.android.compose.ui.Text
import breakbadhabits.android.compose.ui.Title
import breakbadhabits.android.compose.ui.rememberEventsCalendarState
import epicarchitect.epicstore.compose.rememberEpicStoreEntry
import java.time.format.TextStyle
import java.util.*

@Composable
fun HabitScreen(
    habitId: Int,
    openHabitEventCreation: () -> Unit,
    openHabitEventEditing: (habitEventId: Int) -> Unit,
    openHabitEditing: () -> Unit,
    showALlEvents: () -> Unit
) {
    val dateTimeFormatter: DateTimeFormatter = get()
    val habitIconResources: HabitIconResources = get()
    val abstinenceTimeFormatter: AbstinenceTimeFormatter = get()

    val habitNameFeature = rememberEpicStoreEntry {
        createHabitNameFeature(habitId)
    }
    val habitIconIdFeature = rememberEpicStoreEntry {
        createHabitIconIdFeature(habitId)
    }
    val habitAbstinenceTimeFeature = rememberEpicStoreEntry {
        createHabitAbstinenceTimeFeature(habitId)
    }
    val currentMonthEventTimesFeature = rememberEpicStoreEntry {
        createCurrentMonthHabitEventTimesFeature(habitId)
    }
    val currentMonthEventIdsFeature = rememberEpicStoreEntry {
        createCurrentMonthHabitEventIdsFeature(habitId)
    }
    val eventIdsFeature = rememberEpicStoreEntry {
        createHabitEventIdsFeature(habitId)
    }
    val abstinenceTimesFeature = rememberEpicStoreEntry {
        createHabitAbstinenceTimesFeature(habitId)
    }

    val habitName by habitNameFeature.state.collectAsState()
    val habitIconId by habitIconIdFeature.state.collectAsState()
    val habitAbstinenceTime by habitAbstinenceTimeFeature.state.collectAsState()
    val currentMonthEventTimes by currentMonthEventTimesFeature.state.collectAsState()
    val currentMonthEventIds by currentMonthEventIdsFeature.state.collectAsState()
    val eventIds by eventIdsFeature.state.collectAsState()
    val abstinenceTimes by abstinenceTimesFeature.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
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
                    painter = painterResource(habitIconResources[habitIconId ?: 0])
                )

                Title(
                    modifier = Modifier.padding(top = 8.dp),
                    text = habitName ?: ""
                )

                Text(
                    text = when (val time = habitAbstinenceTime) {
                        null -> stringResource(R.string.habit_noEvents)
                        else -> abstinenceTimeFormatter.format(time)
                    }
                )

                Button(
                    modifier = Modifier.padding(top = 8.dp),
                    onClick = { openHabitEventCreation() },
                    text = stringResource(R.string.habit_resetTime),
                    interactionType = InteractionType.MAIN
                )
            }

            IconButton(
                modifier = Modifier.align(Alignment.TopEnd),
                onClick = {
                    openHabitEditing()
                }
            ) {
                Icon(painterResource(R.drawable.ic_settings))
            }
        }

        if (eventIds.isNotEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 16.dp, end = 16.dp)
            ) {
                val calendarState = rememberEventsCalendarState()

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
                        events = currentMonthEventTimes,
                        canChangeMonth = false,
                        withHeader = false,
                        horizontalSwipeEnabled = false
                    )

                    currentMonthEventIds.forEach { eventId ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { openHabitEventEditing(eventId) }
                        ) {
                            Column(
                                modifier = Modifier.padding(
                                    start = 14.dp,
                                    end = 14.dp,
                                    top = 4.dp,
                                    bottom = 4.dp
                                ),
                            ) {
                                val timeFeature = rememberEpicStoreEntry {
                                    createHabitEventTimeFeature(eventId)
                                }
                                val commentFeature = rememberEpicStoreEntry {
                                    createHabitEventCommentFeature(eventId)
                                }

                                val time by timeFeature.state.collectAsState()
                                val comment by commentFeature.state.collectAsState()

                                time?.let {
                                    Text(
                                        modifier = Modifier.padding(2.dp),
                                        text = dateTimeFormatter.formatDateTime(
                                            Calendar.getInstance().apply {
                                                timeInMillis = it
                                            },
                                            withoutYear = true
                                        )
                                    )
                                }
                                Text(
                                    modifier = Modifier.padding(2.dp),
                                    text = comment ?: stringResource(R.string.habitEvents_noComment)
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
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Title(
                        modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
                        text = stringResource(R.string.habitAnalyze_abstinenceChart_title)
                    )

                    if (abstinenceTimes.isNotEmpty()) {
                        Histogram(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp),
                            values = abstinenceTimes,
                            valueFormatter = {
                                abstinenceTimeFormatter.format(it, 2)
                            },
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
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Title(
                        modifier = Modifier.padding(
                            start = 16.dp,
                            top = 16.dp,
                            end = 16.dp,
                            bottom = 4.dp
                        ),
                        text = stringResource(R.string.habitAnalyze_statistics_title)
                    )

                    val averageAbstinenceTimeFeature = rememberEpicStoreEntry {
                        createHabitAverageAbstinenceTimeFeature(habitId)
                    }
                    val maxAbstinenceTimeFeature = rememberEpicStoreEntry {
                        createHabitMaxAbstinenceTimeFeature(habitId)
                    }
                    val minAbstinenceTimeFeature = rememberEpicStoreEntry {
                        createHabitMinAbstinenceTimeFeature(habitId)
                    }
                    val timeSinceFirstEventFeature = rememberEpicStoreEntry {
                        createHabitTimeSinceFirstEventFeature(habitId)
                    }
                    val currentMonthEventCountFeature = rememberEpicStoreEntry {
                        createCurrentMonthHabitEventCountFeature(habitId)
                    }
                    val previousMonthEventCountFeature = rememberEpicStoreEntry {
                        createPreviousMonthHabitEventCountFeature(habitId)
                    }
                    val totalEventCountFeature = rememberEpicStoreEntry {
                        createHabitEventCountFeature(habitId)
                    }
                    val averageAbstinenceTime by averageAbstinenceTimeFeature.state.collectAsState()
                    val maxAbstinenceTime by maxAbstinenceTimeFeature.state.collectAsState()
                    val minAbstinenceTime by minAbstinenceTimeFeature.state.collectAsState()
                    val timeSinceFirstEvent by timeSinceFirstEventFeature.state.collectAsState()
                    val currentMonthEventCount by currentMonthEventCountFeature.state.collectAsState()
                    val previousMonthEventCount by previousMonthEventCountFeature.state.collectAsState()
                    val totalEventCount by totalEventCountFeature.state.collectAsState()

                    Statistics(
                        modifier = Modifier.fillMaxWidth(),
                        statistics = listOf(
                            StatisticData(
                                name = stringResource(R.string.habitAnalyze_statistics_averageAbstinenceTime),
                                value = averageAbstinenceTime?.let {
                                    abstinenceTimeFormatter.format(it, 2)
                                } ?: "-"
                            ),
                            StatisticData(
                                name = stringResource(R.string.habitAnalyze_statistics_maxAbstinenceTime),
                                value = maxAbstinenceTime?.let {
                                    abstinenceTimeFormatter.format(it, 2)
                                } ?: "-"
                            ),
                            StatisticData(
                                name = stringResource(R.string.habitAnalyze_statistics_minAbstinenceTime),
                                value = minAbstinenceTime?.let {
                                    abstinenceTimeFormatter.format(it, 2)
                                } ?: "-"
                            ),
                            StatisticData(
                                name = stringResource(R.string.habitAnalyze_statistics_timeFromFirstEvent),
                                value = timeSinceFirstEvent?.let {
                                    abstinenceTimeFormatter.format(it, 2)
                                } ?: "-"
                            ),
                            StatisticData(
                                name = stringResource(R.string.habitAnalyze_statistics_countEventsInCurrentMonth),
                                value = currentMonthEventCount.toString()
                            ),
                            StatisticData(
                                name = stringResource(R.string.habitAnalyze_statistics_countEventsInPreviousMonth),
                                value = previousMonthEventCount.toString()
                            ),
                            StatisticData(
                                name = stringResource(R.string.habitAnalyze_statistics_countEvents),
                                value = totalEventCount.toString()
                            )
                        )
                    )
                }
            }
        }
    }
}