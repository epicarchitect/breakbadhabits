package epicarchitect.breakbadhabits.ui.habits.details

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import epicarchitect.breakbadhabits.data.AppData
import epicarchitect.breakbadhabits.data.resources.strings.HabitDetailsStrings
import epicarchitect.breakbadhabits.entity.datetime.FormattedDuration
import epicarchitect.breakbadhabits.entity.datetime.duration
import epicarchitect.breakbadhabits.entity.habits.CachedHabitAbstinenceHistory
import epicarchitect.breakbadhabits.entity.habits.CachedHabitAbstinenceStatistics
import epicarchitect.breakbadhabits.entity.habits.CachedHabitEventAmountStatistics
import epicarchitect.breakbadhabits.entity.habits.DefaultHabitAbstinenceHistory
import epicarchitect.breakbadhabits.entity.habits.DefaultHabitAbstinenceStatistics
import epicarchitect.breakbadhabits.entity.habits.DefaultHabitEventAmountStatistics
import epicarchitect.breakbadhabits.entity.habits.HabitAbstinenceStatistics
import epicarchitect.breakbadhabits.entity.habits.HabitEventAmountStatistics
import epicarchitect.breakbadhabits.ui.habits.editing.HabitEditingScreen
import epicarchitect.breakbadhabits.ui.habits.tracks.creation.HabitTrackCreationScreen
import epicarchitect.breakbadhabits.ui.habits.tracks.list.HabitTracksScreen
import epicarchitect.breakbadhabits.uikit.Card
import epicarchitect.breakbadhabits.uikit.FlowStateContainer
import epicarchitect.breakbadhabits.uikit.Histogram
import epicarchitect.breakbadhabits.uikit.Icon
import epicarchitect.breakbadhabits.uikit.IconButton
import epicarchitect.breakbadhabits.uikit.StatisticData
import epicarchitect.breakbadhabits.uikit.Statistics
import epicarchitect.breakbadhabits.uikit.button.Button
import epicarchitect.breakbadhabits.uikit.stateOfList
import epicarchitect.breakbadhabits.uikit.stateOfOneOrNull
import epicarchitect.breakbadhabits.uikit.text.Text
import epicarchitect.breakbadhabits.uikit.theme.AppTheme
import epicarchitect.calendar.compose.pager.EpicCalendarPager
import epicarchitect.calendar.compose.pager.state.rememberEpicCalendarPagerState
import epicarchitect.calendar.compose.ranges.drawEpicRanges
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class HabitDetailsScreen(private val habitId: Int) : Screen {
    @Composable
    override fun Content() {
        HabitDetails(habitId)
    }
}

@Composable
fun HabitDetails(habitId: Int) {
    val appTime by AppData.userDateTime.collectAsState()
    val timeZone = appTime.timeZone()
    val habitDetailsStrings = AppData.resources.strings.habitDetailsStrings
    val icons = AppData.resources.icons
    val habitQueries = AppData.database.habitQueries
    val habitTrackQueries = AppData.database.habitTrackQueries
    val navigator = LocalNavigator.currentOrThrow

    FlowStateContainer(
        state1 = stateOfOneOrNull { habitQueries.habitById(habitId) },
        state2 = stateOfList { habitTrackQueries.tracksByHabitId(habitId) },
        state3 = stateOfOneOrNull { habitTrackQueries.trackByHabitIdAndMaxEndTime(habitId) }
    ) { habit, habitTracks, lastTrack ->
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

        val abstinence = lastTrack?.let { (it.endTime..appTime.instant()).duration() }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 24.dp, start = 16.dp, end = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = navigator::pop
                ) {
                    Icon(icons.commonIcons.arrowBack)
                }

                Icon(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .size(44.dp),
                    icon = icons.habitIcons.getById(habit?.iconId ?: 0)
                )

                IconButton(
                    onClick = {
                        navigator += HabitEditingScreen(habitId)
                    }
                ) {
                    Icon(icons.commonIcons.settings)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = habit?.name ?: "error",
                type = Text.Type.Title
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .align(Alignment.CenterHorizontally),
                text = abstinence?.let {
                    FormattedDuration(
                        value = it,
                        accuracy = FormattedDuration.Accuracy.SECONDS
                    )
                } ?: habitDetailsStrings.habitHasNoEvents(),
                type = Text.Type.Description,
                priority = Text.Priority.Medium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = {
                    navigator += HabitTrackCreationScreen(habitId)
                },
                text = habitDetailsStrings.addHabitTrack(),
                type = Button.Type.Main
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                val calendarState = rememberEpicCalendarPagerState()
                val title = calendarState.currentMonth.toString()

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

                val rangeColor = AppTheme.colorScheme.primary

                EpicCalendarPager(
                    pageModifier = {
                        Modifier.drawEpicRanges(
                            ranges = habitTracks.map {
                                it.startTime.toLocalDateTime(timeZone).date..it.endTime.toLocalDateTime(timeZone).date
                            },
                            color = rangeColor
                        )
                    },
                    state = calendarState
                )

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .clickable(onClick = {
                            navigator += HabitTracksScreen(habitId)
                        })
                ) {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = habitDetailsStrings.showAllTracks()
                    )
                }
            }

            val abstinenceTimes = remember(abstinenceHistory) {
                abstinenceHistory.abstinenceRanges().map {
                    it.duration().inWholeSeconds.toFloat()
                }
            }

            if (abstinenceTimes.size > 2) {
                Card(
                    modifier = Modifier
                        .padding(top = 24.dp)
                        .fillMaxWidth()
                ) {
                    Column {
                        Text(
                            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
                            text = habitDetailsStrings.abstinenceChartTitle(),
                            type = Text.Type.Title
                        )

                        Histogram(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp),
                            values = abstinenceTimes,
                            valueFormatter = {
                                FormattedDuration(
                                    value = it.toLong().seconds,
                                    accuracy = FormattedDuration.Accuracy.DAYS
                                )
                            }
                        )
                    }
                }
            }


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
                        text = habitDetailsStrings.statisticsTitle(),
                        type = Text.Type.Title
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Statistics(
                        modifier = Modifier.fillMaxWidth(),
                        statistics = buildStatisticsData(
                            abstinenceStatistics = abstinenceStatistics,
                            eventAmountStatistics = habitEventAmountStatistics,
                            strings = habitDetailsStrings
                        )
                    )
                }
            }
        }
    }
}

private fun buildStatisticsData(
    abstinenceStatistics: HabitAbstinenceStatistics,
    eventAmountStatistics: HabitEventAmountStatistics,
    strings: HabitDetailsStrings
) = listOf(
    StatisticData(
        name = strings.statisticsAverageAbstinenceTime(),
        value = FormattedDuration(
            value = abstinenceStatistics.averageDuration() ?: Duration.ZERO,
            accuracy = FormattedDuration.Accuracy.HOURS
        ).toString()
    ),
    StatisticData(
        name = strings.statisticsMaxAbstinenceTime(),
        value = FormattedDuration(
            value = abstinenceStatistics.maxDuration() ?: Duration.ZERO,
            accuracy = FormattedDuration.Accuracy.HOURS
        ).toString()
    ),
    StatisticData(
        name = strings.statisticsMinAbstinenceTime(),
        value = FormattedDuration(
            value = abstinenceStatistics.minDuration() ?: Duration.ZERO,
            accuracy = FormattedDuration.Accuracy.HOURS
        ).toString()
    ),
    StatisticData(
        name = strings.statisticsDurationSinceFirstTrack(),
        value = FormattedDuration(
            value = abstinenceStatistics.durationSinceFirstTrack() ?: Duration.ZERO,
            accuracy = FormattedDuration.Accuracy.HOURS
        ).toString()
    ),
    StatisticData(
        name = strings.statisticsCountEventsInCurrentMonth(),
        value = eventAmountStatistics.currentMonthCount().toString()
    ),
    StatisticData(
        name = strings.statisticsCountEventsInPreviousMonth(),
        value = eventAmountStatistics.previousMonthCount().toString()
    ),
    StatisticData(
        name = strings.statisticsTotalCountEvents(),
        value = eventAmountStatistics.totalCount().toString()
    )
)