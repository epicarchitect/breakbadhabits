package epicarchitect.breakbadhabits.features.habits.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import epicarchitect.breakbadhabits.UpdatingAppTime
import epicarchitect.breakbadhabits.features.LocalAppModule
import epicarchitect.breakbadhabits.features.dashboard.LastHabitTrackAbstinence
import epicarchitect.breakbadhabits.foundation.coroutines.DefaultCoroutineDispatchers
import epicarchitect.breakbadhabits.foundation.icons.get
import epicarchitect.breakbadhabits.foundation.uikit.Card
import epicarchitect.breakbadhabits.foundation.uikit.Icon
import epicarchitect.breakbadhabits.foundation.uikit.IconButton
import epicarchitect.breakbadhabits.foundation.uikit.StatisticData
import epicarchitect.breakbadhabits.foundation.uikit.Statistics
import epicarchitect.breakbadhabits.foundation.uikit.button.Button
import epicarchitect.breakbadhabits.foundation.uikit.text.Text
import epicarchitect.breakbadhabits.newarch.habits.CachedHabitAbstinenceHistory
import epicarchitect.breakbadhabits.newarch.habits.CachedHabitAbstinenceStatistics
import epicarchitect.breakbadhabits.newarch.habits.CachedHabitEventAmountStatistics
import epicarchitect.breakbadhabits.newarch.habits.DefaultHabitAbstinenceHistory
import epicarchitect.breakbadhabits.newarch.habits.DefaultHabitAbstinenceStatistics
import epicarchitect.breakbadhabits.newarch.habits.DefaultHabitEventAmountStatistics
import epicarchitect.breakbadhabits.newarch.habits.HabitAbstinenceStatistics
import epicarchitect.breakbadhabits.newarch.habits.HabitEventAmountStatistics
import epicarchitect.breakbadhabits.ui.format.DurationFormatter
import epicarchitect.breakbadhabits.ui.icons.VectorIcons
import kotlinx.coroutines.Dispatchers
import kotlin.time.Duration

@Composable
fun HabitDetails(dependencies: HabitDetailsDependencies) {
    val habitState = remember(dependencies) {
        dependencies.mainDatabase.habitQueries
            .selectById(dependencies.habitId)
            .asFlow()
            .mapToOneOrNull(Dispatchers.IO)
    }.collectAsState(null)

    val habit = habitState.value
    val durationFormatter = LocalAppModule.current.format.durationFormatter

    if (habit != null) {
        val habitTracks by remember(dependencies) {
            dependencies.mainDatabase.habitTrackQueries
                .selectByHabitId(dependencies.habitId)
                .asFlow()
                .mapToList(Dispatchers.IO)
        }.collectAsState(emptyList())

        val appTime by UpdatingAppTime.state().collectAsState()

        val abstinenceHistory = remember(habitTracks, appTime) {
            CachedHabitAbstinenceHistory(
                DefaultHabitAbstinenceHistory(
                    habitTracks,
                    appTime
                )
            )
        }

        val abstinenceStatistics = remember(appTime, abstinenceHistory) {
            CachedHabitAbstinenceStatistics(
                DefaultHabitAbstinenceStatistics(
                    abstinenceHistory,
                    appTime
                )
            )
        }

        val habitEventAmountStatistics = remember(habitTracks, appTime) {
            CachedHabitEventAmountStatistics(
                DefaultHabitEventAmountStatistics(
                    habitTracks,
                    appTime
                )
            )
        }

        val lastTrack by dependencies.mainDatabase.habitTrackQueries
            .selectByHabitIdAndMaxEndTime(dependencies.habitId)
            .asFlow()
            .mapToOneOrNull(DefaultCoroutineDispatchers.io)
            .collectAsState(null)

        val abstinence = lastTrack?.let { LastHabitTrackAbstinence(it, appTime.instant()) }


        Column {
            Spacer(modifier = Modifier.height(16.dp))

            Icon(
                modifier = Modifier
                    .size(44.dp)
                    .align(Alignment.CenterHorizontally),
                icon = dependencies.habitIcons[habit.iconId]
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = habit.name,
                type = Text.Type.Title
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .align(Alignment.CenterHorizontally),
                text = abstinence?.let {
                    durationFormatter.format(it.duration)
                } ?: dependencies.resources.habitHasNoEvents(),
                type = Text.Type.Description,
                priority = Text.Priority.Medium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = dependencies.navigation::openHabitTrackCreation,
                text = dependencies.resources.addHabitTrack(),
                type = Button.Type.Main
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
//                LoadingBox(viewModel.currentMonthDailyCountsController) { dailyCounts ->
//                    Column(modifier = Modifier.fillMaxWidth()) {
//                        val yearMonth = remember { MonthOfYear.now(timeZone) }
////
////                        val epicCalendarState = rememberEpicCalendarState(
////                            timeZone = timeZone,
////                            monthOfYear = yearMonth,
////                            ranges = remember(dailyCounts.tracks) {
////                                dailyCounts.tracks.map {
////                                    it.dateTimeRange.let {
////                                        it.start.instant..it.endInclusive.instant
////                                    }
////                                }
////                            }
////                        )
////
////                        val title = remember(yearMonth) {
////                            "${
////                                yearMonth.month.getDisplayName(
////                                    TextStyle.FULL_STANDALONE,
////                                    Locale.getDefault()
////                                ).replaceFirstChar { it.titlecase(Locale.getDefault()) }
////                            } ${yearMonth.year}"
////                        }
//
////                        Text(
////                            modifier = Modifier.padding(
////                                start = 16.dp,
////                                end = 16.dp,
////                                bottom = 12.dp,
////                                top = 16.dp
////                            ),
////                            text = title,
////                            type = Text.Type.Title
////                        )
////
////                        EpicCalendar(
////                            state = epicCalendarState,
////                            horizontalInnerPadding = 8.dp,
////                            dayBadgeText = { day ->
//// //                                val count = dailyCounts.dateToCount[day.date] ?: 0
//// //                                if (count == 0) null
//// //                                else if (count > 100) "99+"
//// //                                else count.toString()
////                                "n"
////                            }
////                        )
//
//                        Spacer(modifier = Modifier.height(8.dp))
//
//                        Box(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .height(44.dp)
//                                .clickable(onClick = viewModel.allTracksController::request)
//                        ) {
//                            Text(
//                                modifier = Modifier.align(Alignment.Center),
//                                text = "Перейти к событиям"
//                                // stringResource(R.string.habit_showAllEvents)
//                            )
//                        }
//                    }
//                }
            }

//            LoadingBox(viewModel.abstinenceListController) { abstinenceList ->
//                if (abstinenceList.size < 3) return@LoadingBox
//                Card(
//                    modifier = Modifier
//                        .padding(top = 24.dp)
//                        .fillMaxWidth()
//                ) {
//                    Column {
//                        Text(
//                            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
//                            text = "stringResource(R.string.habitAnalyze_abstinenceChart_title)",
//                            type = Text.Type.Title
//                        )
//
//                        val abstinenceTimes = remember(abstinenceList) {
//                            abstinenceList.map {
//                                it.duration.inWholeSeconds.toFloat()
//                            }
//                        }
//
//                        Histogram(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .height(300.dp),
//                            values = abstinenceTimes,
//                            valueFormatter = {
//                                durationFormatter.format(
//                                    duration = it.toLong().seconds,
//                                    accuracy = DurationFormatter.Accuracy.DAYS
//                                )
//                            },
//                            startPadding = 16.dp,
//                            endPadding = 16.dp
//                        )
//                    }
//                }
//            }


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
                        text = dependencies.resources.statisticsTitle(),
                        type = Text.Type.Title
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Statistics(
                        modifier = Modifier.fillMaxWidth(),
                        statistics = remember(habitEventAmountStatistics, abstinenceStatistics) {
                            buildStatisticsData(
                                abstinenceStatistics,
                                habitEventAmountStatistics,
                                durationFormatter,
                                dependencies.resources
                            )
                        }
                    )
                }
            }
        }

        IconButton(
//            modifier = Modifier.align(Alignment.TopStart),
            onClick = dependencies.navigation::back
        ) {
            Icon(VectorIcons.ArrowBack)
        }

        IconButton(
//            modifier = Modifier.align(Alignment.TopEnd),
            onClick = dependencies.navigation::openEditingScreen
        ) {
            Icon(VectorIcons.Settings)
        }
    }

//    LoadingBox(
//        modifier = Modifier
//            .fillMaxSize()
//            .verticalScroll(rememberScrollState())
//            .padding(16.dp),
//        controller = viewModel.habitController
//    ) { habit ->
//        habit ?: return@LoadingBox
//
//
//    }
}

private fun buildStatisticsData(
    abstinenceStatistics: HabitAbstinenceStatistics,
    eventAmountStatistics: HabitEventAmountStatistics,
    durationFormatter: DurationFormatter,
    resources: HabitDetailsResources
) = listOf(
    StatisticData(
        name = resources.statisticsAverageAbstinenceTime(),
        value = durationFormatter.format(
            duration = abstinenceStatistics.averageDuration() ?: Duration.ZERO,
            accuracy = DurationFormatter.Accuracy.HOURS
        )
    ),
    StatisticData(
        name = resources.statisticsMaxAbstinenceTime(),
        value = durationFormatter.format(
            duration = abstinenceStatistics.maxDuration() ?: Duration.ZERO,
            accuracy = DurationFormatter.Accuracy.HOURS
        )
    ),
    StatisticData(
        name = resources.statisticsMinAbstinenceTime(),
        value = durationFormatter.format(
            duration = abstinenceStatistics.minDuration() ?: Duration.ZERO,
            accuracy = DurationFormatter.Accuracy.HOURS
        )
    ),
    StatisticData(
        name = resources.statisticsDurationSinceFirstTrack(),
        value = durationFormatter.format(
            duration = abstinenceStatistics.durationSinceFirstTrack() ?: Duration.ZERO,
            accuracy = DurationFormatter.Accuracy.HOURS
        )
    ),
    StatisticData(
        name = resources.statisticsCountEventsInCurrentMonth(),
        value = eventAmountStatistics.currentMonthCount().toString()
    ),
    StatisticData(
        name = resources.statisticsCountEventsInPreviousMonth(),
        value = eventAmountStatistics.previousMonthCount().toString()
    ),
    StatisticData(
        name = resources.statisticsTotalCountEvents(),
        value = eventAmountStatistics.totalCount().toString()
    )
)