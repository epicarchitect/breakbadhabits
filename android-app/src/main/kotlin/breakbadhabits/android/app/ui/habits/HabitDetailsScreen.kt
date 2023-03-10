package breakbadhabits.android.app.ui.habits

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import breakbadhabits.android.app.R
import breakbadhabits.android.app.ui.LocalDateTimeFormatter
import breakbadhabits.android.app.ui.LocalHabitIconResourceProvider
import breakbadhabits.app.entity.Habit
import breakbadhabits.app.entity.HabitAbstinence
import breakbadhabits.app.entity.HabitStatistics
import breakbadhabits.foundation.controller.LoadingController
import breakbadhabits.foundation.datetime.toMillis
import breakbadhabits.foundation.uikit.Card
import breakbadhabits.foundation.uikit.Histogram
import breakbadhabits.foundation.uikit.Icon
import breakbadhabits.foundation.uikit.IconButton
import breakbadhabits.foundation.uikit.LoadingBox
import breakbadhabits.foundation.uikit.StatisticData
import breakbadhabits.foundation.uikit.Statistics
import breakbadhabits.foundation.uikit.button.Button
import breakbadhabits.foundation.uikit.button.InteractionType
import breakbadhabits.foundation.uikit.text.Text
import breakbadhabits.foundation.uikit.text.Title

@Composable
fun HabitDetailsScreen(
    habitController: LoadingController<Habit?>,
    habitAbstinenceController: LoadingController<HabitAbstinence?>,
    abstinenceListController: LoadingController<List<HabitAbstinence>>,
    statisticsController: LoadingController<HabitStatistics>,
    onEditClick: () -> Unit,
    onAddTrackClick: () -> Unit,
) {
    val habitIconResources = LocalHabitIconResourceProvider.current
    val dateTimeFormatter = LocalDateTimeFormatter.current
    val context = LocalContext.current

    LoadingBox(habitController) { habit ->
        if (habit == null) {
            Text("Not exist")
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Icon(
                    modifier = Modifier
                        .size(44.dp)
                        .align(Alignment.CenterHorizontally),
                    painter = painterResource(habitIconResources[habit.icon].resourceId)
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
                            dateTimeFormatter.formatDistance(it.range.value)
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
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Title(
                            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
                            text = stringResource(R.string.habitAnalyze_abstinenceChart_title)
                        )

                        LoadingBox(abstinenceListController) { abstinenceList ->
                            if (abstinenceList.isNotEmpty()) {
                                val abstinenceTimes = remember(abstinenceList) {
                                    abstinenceList.map {
                                        it.range.value.endInclusive.toMillis() - it.range.value.start.toMillis()
                                    }
                                }
                                Histogram(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(300.dp),
                                    values = abstinenceTimes,
                                    valueFormatter = {
                                        dateTimeFormatter.formatDistance(
                                            distanceInMillis = it,
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

                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 16.dp,
                                top = 16.dp,
                                end = 16.dp,
                                bottom = 8.dp
                            )
                    ) {
                        Title(stringResource(R.string.habitAnalyze_statistics_title))

                        Spacer(modifier = Modifier.height(8.dp))

                        LoadingBox(statisticsController) { statistics ->
                            val data = remember(statistics) {
                                listOfNotNull(
                                    statistics.abstinence?.let {
                                        StatisticData(
                                            name = context.getString(R.string.habitAnalyze_statistics_averageAbstinenceTime),
                                            value = dateTimeFormatter.formatDistance(
                                                distanceInMillis = it.averageTime,
                                                maxValueCount = 2
                                            )
                                        )
                                    },
                                    statistics.abstinence?.let {
                                        StatisticData(
                                            name = context.getString(R.string.habitAnalyze_statistics_maxAbstinenceTime),
                                            value = dateTimeFormatter.formatDistance(
                                                distanceInMillis = it.maxTime,
                                                maxValueCount = 2
                                            )
                                        )
                                    },
                                    statistics.abstinence?.let {
                                        StatisticData(
                                            name = context.getString(R.string.habitAnalyze_statistics_minAbstinenceTime),
                                            value = dateTimeFormatter.formatDistance(
                                                distanceInMillis = it.minTime,
                                                maxValueCount = 2
                                            )
                                        )
                                    },
                                    statistics.abstinence?.let {
                                        StatisticData(
                                            name = context.getString(R.string.habitAnalyze_statistics_timeFromFirstEvent),
                                            value = dateTimeFormatter.formatDistance(
                                                distanceInMillis = it.timeSinceFirstTrack,
                                                maxValueCount = 2
                                            )
                                        )
                                    },
                                    StatisticData(
                                        name = context.getString(R.string.habitAnalyze_statistics_countEventsInCurrentMonth),
                                        value = statistics.eventCount.currentMonthCount.toString()
                                    ),
                                    StatisticData(
                                        name = context.getString(R.string.habitAnalyze_statistics_countEventsInPreviousMonth),
                                        value = statistics.eventCount.previousMonthCount.toString()
                                    ),
                                    StatisticData(
                                        name = context.getString(R.string.habitAnalyze_statistics_countEvents),
                                        value = statistics.eventCount.totalCount.toString()
                                    )
                                )
                            }

                            Statistics(
                                modifier = Modifier.fillMaxWidth(),
                                statistics = data,
                                horizontalItemPadding = 0.dp
                            )
                        }
                    }
                }
            }

            IconButton(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.TopEnd),
                onClick = onEditClick
            ) {
                Icon(painterResource(R.drawable.ic_settings))
            }
        }
    }
}