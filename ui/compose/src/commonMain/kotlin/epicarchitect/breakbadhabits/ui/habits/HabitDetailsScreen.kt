package epicarchitect.breakbadhabits.ui.habits

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import epicarchitect.breakbadhabits.di.holder.LocalAppModule
import epicarchitect.breakbadhabits.foundation.controller.LoadingController
import epicarchitect.breakbadhabits.foundation.datetime.MonthOfYear
import epicarchitect.breakbadhabits.foundation.datetime.duration
import epicarchitect.breakbadhabits.foundation.uikit.Card
import epicarchitect.breakbadhabits.foundation.uikit.Histogram
import epicarchitect.breakbadhabits.foundation.uikit.IconButton
import epicarchitect.breakbadhabits.foundation.uikit.LoadingBox
import epicarchitect.breakbadhabits.foundation.uikit.StatisticData
import epicarchitect.breakbadhabits.foundation.uikit.Statistics
import epicarchitect.breakbadhabits.foundation.uikit.button.Button
import epicarchitect.breakbadhabits.foundation.uikit.calendar.EpicCalendar
import epicarchitect.breakbadhabits.foundation.uikit.calendar.rememberEpicCalendarState
import epicarchitect.breakbadhabits.foundation.uikit.text.Text
import epicarchitect.breakbadhabits.logic.habits.model.DailyHabitEventCount
import epicarchitect.breakbadhabits.logic.habits.model.Habit
import epicarchitect.breakbadhabits.logic.habits.model.HabitAbstinence
import epicarchitect.breakbadhabits.logic.habits.model.HabitStatistics
import epicarchitect.breakbadhabits.ui.format.DurationFormatter
import kotlinx.datetime.TimeZone
import java.time.format.TextStyle
import java.util.Locale
import kotlin.time.Duration.Companion.seconds

@Composable
fun HabitDetails(
    habitController: LoadingController<Habit?>,
    habitAbstinenceController: LoadingController<HabitAbstinence?>,
    abstinenceListController: LoadingController<List<HabitAbstinence>>,
    statisticsController: LoadingController<HabitStatistics?>,
    currentMonthDailyCountsController: LoadingController<DailyHabitEventCount>,
    onEditClick: () -> Unit,
    onAddTrackClick: () -> Unit,
    onAllTracksClick: () -> Unit,
) {
    val logicModule = LocalAppModule.current.logic
    val uiModule = LocalAppModule.current.ui
    val timeZone by logicModule.dateTime.dateTimeProvider.currentTimeZoneFlow()
        .collectAsState(TimeZone.currentSystemDefault())

    val durationFormatter = uiModule.format.durationFormatter

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

//            LocalResourceIcon(
//                modifier = Modifier
//                    .size(44.dp)
//                    .align(Alignment.CenterHorizontally),
//                resourceId = habit.icon.resourceId
//            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = habit.name,
                type = Text.Type.Title
            )

            Spacer(modifier = Modifier.height(8.dp))

            LoadingBox(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                controller = habitAbstinenceController
            ) { abstinence ->
                Text(
                    text = abstinence?.let {
                        durationFormatter.format(it.dateTimeRange.duration)
                    } ?: " stringResource(R.string.habits_noEvents)"
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = onAddTrackClick,
                text = "stringResource(R.string.habit_resetTime)",
                type = Button.Type.Main
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                LoadingBox(currentMonthDailyCountsController) { dailyCounts ->
                    Column(modifier = Modifier.fillMaxWidth()) {
                        val yearMonth = remember { MonthOfYear.now(timeZone) }

                        val epicCalendarState = rememberEpicCalendarState(
                            timeZone = timeZone,
                            monthOfYear = yearMonth,
                            ranges = remember(dailyCounts.tracks) {
                                dailyCounts.tracks.map {
                                    it.dateTimeRange.let {
                                        it.start.instant..it.endInclusive.instant
                                    }
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

                        Text(
                            modifier = Modifier.padding(
                                start = 16.dp,
                                end = 16.dp,
                                bottom = 12.dp,
                                top = 16.dp
                            ),
                            text = title,
                            type = Text.Type.Title
                        )

                        EpicCalendar(
                            state = epicCalendarState,
                            horizontalInnerPadding = 8.dp,
                            dayBadgeText = { day ->
//                                val count = dailyCounts.dateToCount[day.date] ?: 0
//                                if (count == 0) null
//                                else if (count > 100) "99+"
//                                else count.toString()
                                "n"
                            }
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp)
                                .clickable(onClick = onAllTracksClick)
                        ) {
                            Text(
                                modifier = Modifier.align(Alignment.Center),
                                text = "Перейти к событиям"//stringResource(R.string.habit_showAllEvents)
                            )
                        }
                    }
                }
            }

            LoadingBox(abstinenceListController) { abstinenceList ->
                if (abstinenceList.size < 3) return@LoadingBox
                Card(
                    modifier = Modifier
                        .padding(top = 24.dp)
                        .fillMaxWidth()
                ) {
                    Column {
                        Text(
                            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
                            text = "stringResource(R.string.habitAnalyze_abstinenceChart_title)",
                            type = Text.Type.Title
                        )

                        val abstinenceTimes = remember(abstinenceList) {
                            abstinenceList.map {
                                it.dateTimeRange.duration.inWholeSeconds.toFloat()
                            }
                        }

                        Histogram(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp),
                            values = abstinenceTimes,
                            valueFormatter = {
                                durationFormatter.format(
                                    duration = it.toLong().seconds,
                                    accuracy = DurationFormatter.Accuracy.DAYS
                                )
                            },
                            startPadding = 16.dp,
                            endPadding = 16.dp,
                        )
                    }
                }
            }

            LoadingBox(statisticsController) { statistics ->
                statistics ?: return@LoadingBox
                Card(
                    modifier = Modifier
                        .padding(top = 24.dp)
                        .fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .padding(
                                start = 16.dp,
                                top = 16.dp,
                                end = 16.dp,
                                bottom = 16.dp
                            )
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "stringResource(R.string.habitAnalyze_statistics_title)",
                            type = Text.Type.Title
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Statistics(
                            modifier = Modifier.fillMaxWidth(),
                            statistics = remember(statistics) {
                                statistics.toStatisticsData(
                                    durationFormatter
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
//            LocalResourceIcon(R.drawable.ic_settings)
        }
    }
}

private fun HabitStatistics.toStatisticsData(
    durationFormatter: DurationFormatter,
) = listOf(
    StatisticData(
        name = "context.getString(R.string.habitAnalyze_statistics_averageAbstinenceTime)",
        value = durationFormatter.format(
            duration = abstinence.averageDuration,
            accuracy = DurationFormatter.Accuracy.HOURS
        )
    ),
    StatisticData(
        name = "context.getString(R.string.habitAnalyze_statistics_maxAbstinenceTime)",
        value = durationFormatter.format(
            duration = abstinence.maxDuration,
            accuracy = DurationFormatter.Accuracy.HOURS
        )
    ),
    StatisticData(
        name = "context.getString(R.string.habitAnalyze_statistics_minAbstinenceTime)",
        value = durationFormatter.format(
            duration = abstinence.minDuration,
            accuracy = DurationFormatter.Accuracy.HOURS
        )
    ),
    StatisticData(
        name = "context.getString(R.string.habitAnalyze_statistics_timeFromFirstEvent)",
        value = durationFormatter.format(
            duration = abstinence.durationSinceFirstTrack,
            accuracy = DurationFormatter.Accuracy.HOURS
        )
    ),
    StatisticData(
        name = "context.getString(R.string.habitAnalyze_statistics_countEventsInCurrentMonth)",
        value = eventCount.currentMonthCount.toString()
    ),
    StatisticData(
        name = "context.getString(R.string.habitAnalyze_statistics_countEventsInPreviousMonth)",
        value = eventCount.previousMonthCount.toString()
    ),
    StatisticData(
        name = "context.getString(R.string.habitAnalyze_statistics_countEvents)",
        value = eventCount.totalCount.toString()
    )
)