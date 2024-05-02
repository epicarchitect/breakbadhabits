package epicarchitect.breakbadhabits.ui.habits.details

import androidx.compose.foundation.layout.Box
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
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import epicarchitect.breakbadhabits.database.AppData
import epicarchitect.breakbadhabits.entity.datetime.duration
import epicarchitect.breakbadhabits.entity.habits.CachedHabitAbstinenceHistory
import epicarchitect.breakbadhabits.entity.habits.CachedHabitAbstinenceStatistics
import epicarchitect.breakbadhabits.entity.habits.CachedHabitEventAmountStatistics
import epicarchitect.breakbadhabits.entity.habits.DefaultHabitAbstinenceHistory
import epicarchitect.breakbadhabits.entity.habits.DefaultHabitAbstinenceStatistics
import epicarchitect.breakbadhabits.entity.habits.DefaultHabitEventAmountStatistics
import epicarchitect.breakbadhabits.entity.habits.HabitAbstinenceStatistics
import epicarchitect.breakbadhabits.entity.habits.HabitEventAmountStatistics
import epicarchitect.breakbadhabits.entity.icons.HabitIcons
import epicarchitect.breakbadhabits.entity.icons.VectorIcons
import epicarchitect.breakbadhabits.entity.time.UpdatingAppTime
import epicarchitect.breakbadhabits.ui.habits.editing.HabitEditingScreen
import epicarchitect.breakbadhabits.ui.habits.tracks.creation.HabitTrackCreationScreen
import epicarchitect.breakbadhabits.ui.habits.tracks.list.HabitTracksScreen
import epicarchitect.breakbadhabits.uikit.Card
import epicarchitect.breakbadhabits.uikit.Icon
import epicarchitect.breakbadhabits.uikit.IconButton
import epicarchitect.breakbadhabits.uikit.StatisticData
import epicarchitect.breakbadhabits.uikit.Statistics
import epicarchitect.breakbadhabits.uikit.button.Button
import epicarchitect.breakbadhabits.uikit.text.Text
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

class HabitDetailsScreen(private val habitId: Int) : Screen {
    @Composable
    override fun Content() {
        HabitDetails(habitId)
    }
}

@Composable
fun HabitDetails(habitId: Int) {
    val resources = LocalHabitDetailsResources.current
    val navigator = LocalNavigator.currentOrThrow

    val habitState = remember(habitId) {
        AppData.database.habitQueries
            .selectById(habitId)
            .asFlow()
            .mapToOneOrNull(Dispatchers.IO)
    }.collectAsState(null)

    val habit = habitState.value

    if (habit != null) {
        val habitTracks by remember(habit) {
            AppData.database.habitTrackQueries
                .selectByHabitId(habitId)
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

        val lastTrack by remember(habitId) {
            AppData.database.habitTrackQueries
                .selectByHabitIdAndMaxEndTime(habitId)
                .asFlow()
                .mapToOneOrNull(Dispatchers.IO)
        }.collectAsState(null)

        val abstinence = lastTrack?.let { (it.endTime..appTime.instant()).duration() }


        Column {
            Spacer(modifier = Modifier.height(16.dp))

            Icon(
                modifier = Modifier
                    .size(44.dp)
                    .align(Alignment.CenterHorizontally),
                icon = HabitIcons[habit.iconId]
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
                    it.toString()
                } ?: resources.habitHasNoEvents(),
                type = Text.Type.Description,
                priority = Text.Priority.Medium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = {
                    navigator += HabitTrackCreationScreen(habitId)
                },
                text = resources.addHabitTrack(),
                type = Button.Type.Main
            )


            Button(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = {
                    navigator += HabitTracksScreen(habitId)
                },
                text = "tracks",
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
                        text = resources.statisticsTitle(),
                        type = Text.Type.Title
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Statistics(
                        modifier = Modifier.fillMaxWidth(),
                        statistics = remember(habitEventAmountStatistics, abstinenceStatistics) {
                            buildStatisticsData(
                                abstinenceStatistics,
                                habitEventAmountStatistics,
                                resources
                            )
                        }
                    )
                }
            }
        }

        Box {
            IconButton(
                modifier = Modifier.align(Alignment.TopStart),
                onClick = navigator::pop
            ) {
                Icon(VectorIcons.ArrowBack)
            }

            IconButton(
                modifier = Modifier.align(Alignment.TopEnd),
                onClick = {
                    navigator += HabitEditingScreen(habitId)
                }
            ) {
                Icon(VectorIcons.Settings)
            }
        }

    }
}

private fun buildStatisticsData(
    abstinenceStatistics: HabitAbstinenceStatistics,
    eventAmountStatistics: HabitEventAmountStatistics,
    resources: HabitDetailsResources
) = listOf<StatisticData>(
//    StatisticData(
//        name = resources.statisticsAverageAbstinenceTime(),
//        value = durationFormatter.format(
//            duration = abstinenceStatistics.averageDuration() ?: Duration.ZERO,
//            accuracy = DurationFormatter.Accuracy.HOURS
//        )
//    ),
//    StatisticData(
//        name = resources.statisticsMaxAbstinenceTime(),
//        value = durationFormatter.format(
//            duration = abstinenceStatistics.maxDuration() ?: Duration.ZERO,
//            accuracy = DurationFormatter.Accuracy.HOURS
//        )
//    ),
//    StatisticData(
//        name = resources.statisticsMinAbstinenceTime(),
//        value = durationFormatter.format(
//            duration = abstinenceStatistics.minDuration() ?: Duration.ZERO,
//            accuracy = DurationFormatter.Accuracy.HOURS
//        )
//    ),
//    StatisticData(
//        name = resources.statisticsDurationSinceFirstTrack(),
//        value = durationFormatter.format(
//            duration = abstinenceStatistics.durationSinceFirstTrack() ?: Duration.ZERO,
//            accuracy = DurationFormatter.Accuracy.HOURS
//        )
//    ),
//    StatisticData(
//        name = resources.statisticsCountEventsInCurrentMonth(),
//        value = eventAmountStatistics.currentMonthCount().toString()
//    ),
//    StatisticData(
//        name = resources.statisticsCountEventsInPreviousMonth(),
//        value = eventAmountStatistics.previousMonthCount().toString()
//    ),
//    StatisticData(
//        name = resources.statisticsTotalCountEvents(),
//        value = eventAmountStatistics.totalCount().toString()
//    )
)