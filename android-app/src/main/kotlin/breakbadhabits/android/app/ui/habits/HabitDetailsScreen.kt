package breakbadhabits.android.app.ui.habits

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import breakbadhabits.android.app.R
import breakbadhabits.android.app.format.DateTimeFormatter
import breakbadhabits.android.app.ui.app.LocalDateTimeFormatter
import breakbadhabits.android.app.ui.app.LocalHabitIconResourceProvider
import breakbadhabits.app.entity.Habit
import breakbadhabits.app.entity.HabitAbstinence
import breakbadhabits.app.entity.HabitStatistics
import breakbadhabits.app.entity.HabitTrack
import breakbadhabits.foundation.controller.LoadingController
import breakbadhabits.foundation.datetime.toDuration
import breakbadhabits.foundation.uikit.Card
import breakbadhabits.foundation.uikit.EpicCalendar
import breakbadhabits.foundation.uikit.EpicCalendarState
import breakbadhabits.foundation.uikit.Histogram
import breakbadhabits.foundation.uikit.IconButton
import breakbadhabits.foundation.uikit.LoadingBox
import breakbadhabits.foundation.uikit.LocalResourceIcon
import breakbadhabits.foundation.uikit.StatisticData
import breakbadhabits.foundation.uikit.Statistics
import breakbadhabits.foundation.uikit.button.Button
import breakbadhabits.foundation.uikit.button.InteractionType
import breakbadhabits.foundation.uikit.rememberEpicCalendarState
import breakbadhabits.foundation.uikit.text.Text
import breakbadhabits.foundation.uikit.text.Title
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*
import kotlin.time.Duration.Companion.seconds

@Composable
fun HabitDetailsScreen(
    habitController: LoadingController<Habit?>,
    habitAbstinenceController: LoadingController<HabitAbstinence?>,
    abstinenceListController: LoadingController<List<HabitAbstinence>>,
    statisticsController: LoadingController<HabitStatistics>,
    habitTracksController: LoadingController<List<HabitTrack>>,
    onEditClick: () -> Unit,
    onAddTrackClick: () -> Unit,
) {
    val habitIconResources = LocalHabitIconResourceProvider.current
    val dateTimeFormatter = LocalDateTimeFormatter.current
    val context = LocalContext.current

    LoadingBox(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        controller = habitController
    ) { habit ->
        habit ?: return@LoadingBox

        Column {
            Spacer(modifier = Modifier.height(16.dp))

            LocalResourceIcon(
                modifier = Modifier
                    .size(44.dp)
                    .align(Alignment.CenterHorizontally),
                resourceId = habitIconResources[habit.icon].resourceId
            )

            Spacer(modifier = Modifier.height(8.dp))

            Title(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = habit.name.value
            )

            Spacer(modifier = Modifier.height(8.dp))

            LoadingBox(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                controller = habitAbstinenceController
            ) { abstinence ->
                Text(
                    text = abstinence?.let {
                        dateTimeFormatter.formatDuration(it.range.value.toDuration())
                    } ?: stringResource(R.string.habits_noEvents)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = onAddTrackClick,
                text = stringResource(R.string.habit_resetTime),
                interactionType = InteractionType.MAIN
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(modifier = Modifier.fillMaxWidth()) {
                LoadingBox(habitTracksController) { tracks ->
                    Column(modifier = Modifier.fillMaxWidth()) {
                        val yearMonth = remember { YearMonth.now() }
                        val epicCalendarState = rememberEpicCalendarState(
                            yearMonth = yearMonth,
                            ranges = remember(tracks) {
                                tracks.map {
                                    EpicCalendarState.Range(
                                        start = it.range.value.start
                                            .toLocalDateTime(TimeZone.currentSystemDefault())
                                            .toJavaLocalDateTime()
                                            .toLocalDate(),
                                        endInclusive = it.range.value.endInclusive
                                            .toLocalDateTime(TimeZone.currentSystemDefault())
                                            .toJavaLocalDateTime()
                                            .toLocalDate()
                                    )
                                }
                            }
                        )
                        val title = remember(yearMonth) {
                            "${
                                yearMonth.month.getDisplayName(
                                    TextStyle.FULL_STANDALONE,
                                    Locale.getDefault()
                                ).replaceFirstChar { it.titlecase(Locale.getDefault()) }
                            } ${yearMonth.year}"
                        }

                        Title(
                            modifier = Modifier.padding(16.dp),
                            text = title
                        )

                        EpicCalendar(
                            state = epicCalendarState,
                            horizontalInnerPadding = 8.dp
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Card(modifier = Modifier.fillMaxWidth()) {
                Column {
                    Title(
                        modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
                        text = stringResource(R.string.habitAnalyze_abstinenceChart_title)
                    )

                    LoadingBox(abstinenceListController) { abstinenceList ->
                        if (abstinenceList.isNotEmpty()) {
                            val abstinenceTimes = remember(abstinenceList) {
                                abstinenceList.map {
                                    it.range.value.toDuration().inWholeSeconds.toFloat()
                                }
                            }

                            Histogram(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(300.dp),
                                values = abstinenceTimes,
                                valueFormatter = {
                                    dateTimeFormatter.formatDuration(
                                        duration = it.toLong().seconds,
                                        maxValueCount = 2
                                    )
                                },
                                startPadding = 16.dp,
                                endPadding = 16.dp,
                            )
                        } else {
                            Text("No data for chart")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Card {
                Column(
                    modifier = Modifier
                        .padding(
                            start = 16.dp,
                            top = 16.dp,
                            end = 16.dp,
                            bottom = 12.dp
                        )
                        .fillMaxWidth()
                ) {
                    Title(stringResource(R.string.habitAnalyze_statistics_title))

                    Spacer(modifier = Modifier.height(8.dp))

                    LoadingBox(statisticsController) { statistics ->
                        Statistics(
                            modifier = Modifier.fillMaxWidth(),
                            statistics = remember(statistics) {
                                statistics.toStatisticsData(
                                    context,
                                    dateTimeFormatter
                                )
                            }
                        )
                    }
                }
            }
        }

        IconButton(
            modifier = Modifier.align(Alignment.TopEnd),
            onClick = onEditClick
        ) {
            LocalResourceIcon(R.drawable.ic_settings)
        }
    }
}

private fun HabitStatistics.toStatisticsData(
    context: Context,
    dateTimeFormatter: DateTimeFormatter,
) = listOfNotNull(
    abstinence?.let {
        StatisticData(
            name = context.getString(R.string.habitAnalyze_statistics_averageAbstinenceTime),
            value = dateTimeFormatter.formatDuration(
                duration = it.averageTime,
                maxValueCount = 2
            )
        )
    },
    abstinence?.let {
        StatisticData(
            name = context.getString(R.string.habitAnalyze_statistics_maxAbstinenceTime),
            value = dateTimeFormatter.formatDuration(
                duration = it.maxTime,
                maxValueCount = 2
            )
        )
    },
    abstinence?.let {
        StatisticData(
            name = context.getString(R.string.habitAnalyze_statistics_minAbstinenceTime),
            value = dateTimeFormatter.formatDuration(
                duration = it.minTime,
                maxValueCount = 2
            )
        )
    },
    abstinence?.let {
        StatisticData(
            name = context.getString(R.string.habitAnalyze_statistics_timeFromFirstEvent),
            value = dateTimeFormatter.formatDuration(
                duration = it.timeSinceFirstTrack,
                maxValueCount = 2
            )
        )
    },
    StatisticData(
        name = context.getString(R.string.habitAnalyze_statistics_countEventsInCurrentMonth),
        value = eventCount.currentMonthCount.toString()
    ),
    StatisticData(
        name = context.getString(R.string.habitAnalyze_statistics_countEventsInPreviousMonth),
        value = eventCount.previousMonthCount.toString()
    ),
    StatisticData(
        name = context.getString(R.string.habitAnalyze_statistics_countEvents),
        value = eventCount.totalCount.toString()
    )
)