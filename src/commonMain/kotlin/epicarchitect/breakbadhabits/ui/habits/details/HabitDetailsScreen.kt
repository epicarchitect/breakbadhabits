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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import epicarchitect.breakbadhabits.data.AppData
import epicarchitect.breakbadhabits.data.HabitTrack
import epicarchitect.breakbadhabits.data.resources.strings.HabitDetailsStrings
import epicarchitect.breakbadhabits.operation.datetime.DurationFormattingAccuracy
import epicarchitect.breakbadhabits.operation.datetime.averageDuration
import epicarchitect.breakbadhabits.operation.datetime.formatted
import epicarchitect.breakbadhabits.operation.datetime.fromEpic
import epicarchitect.breakbadhabits.operation.datetime.maxDuration
import epicarchitect.breakbadhabits.operation.datetime.minDuration
import epicarchitect.breakbadhabits.operation.datetime.monthOfYear
import epicarchitect.breakbadhabits.operation.datetime.orZero
import epicarchitect.breakbadhabits.operation.datetime.previous
import epicarchitect.breakbadhabits.operation.habits.abstinence
import epicarchitect.breakbadhabits.operation.habits.abstinenceDurationsInSeconds
import epicarchitect.breakbadhabits.operation.habits.abstinenceRangesByFailedRanges
import epicarchitect.breakbadhabits.operation.habits.countEvents
import epicarchitect.breakbadhabits.operation.habits.countEventsInMonth
import epicarchitect.breakbadhabits.operation.habits.failedRanges
import epicarchitect.breakbadhabits.operation.habits.habitAbstinenceDurationSinceFirstTrack
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
import epicarchitect.calendar.compose.basis.contains
import epicarchitect.calendar.compose.basis.state.LocalBasisEpicCalendarState
import epicarchitect.calendar.compose.pager.EpicCalendarPager
import epicarchitect.calendar.compose.pager.state.rememberEpicCalendarPagerState
import epicarchitect.calendar.compose.ranges.drawEpicRanges
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration.Companion.seconds

class HabitDetailsScreen(private val habitId: Int) : Screen {
    @Composable
    override fun Content() {
        HabitDetails(habitId)
    }
}

@Composable
fun HabitDetails(habitId: Int) {
    val currentTime by AppData.dateTime.currentInstantState.collectAsState()
    val timeZone by AppData.dateTime.currentTimeZoneState.collectAsState()
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
        val failedRanges = remember(habitTracks) {
            habitTracks.failedRanges()
        }
        val abstinenceRanges = remember(failedRanges, habitTracks, currentTime) {
            abstinenceRangesByFailedRanges(failedRanges, currentTime)
        }

        val abstinence = lastTrack?.abstinence(currentTime)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().height(40.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = navigator::pop
                ) {
                    Icon(icons.commonIcons.navigationBack)
                }

                IconButton(
                    modifier = Modifier.padding(end = 8.dp),
                    onClick = {
                        navigator += HabitEditingScreen(habitId)
                    }
                ) {
                    Icon(icons.commonIcons.settings)
                }
            }

            Icon(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(44.dp),
                icon = icons.habitIcons.getById(habit?.iconId ?: 0)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = habit?.name ?: "error",
                type = Text.Type.Title
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                text = abstinence?.formatted(
                    accuracy = DurationFormattingAccuracy.SECONDS
                ) ?: habitDetailsStrings.habitHasNoEvents(),
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                val calendarState = rememberEpicCalendarPagerState()

                Text(
                    modifier = Modifier.padding(
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 12.dp,
                        top = 16.dp
                    ),
                    text = calendarState.currentMonth.fromEpic().formatted(),
                    type = Text.Type.Title
                )

                val rangeColor = AppTheme.colorScheme.primary
                val ranges = habitTracks.map {
                    it.startTime.toLocalDateTime(timeZone).date..it.endTime.toLocalDateTime(timeZone).date
                }

                EpicCalendarPager(
                    pageModifier = {
                        Modifier.drawEpicRanges(ranges, rangeColor)
                    },
                    dayOfMonthContent = { date ->
                        val basisState = LocalBasisEpicCalendarState.current!!

                        val isSelected = ranges.any { date in it }

                        androidx.compose.material3.Text(
                            modifier = Modifier.alpha(
                                if (date in basisState.currentMonth) 1.0f
                                else 0.5f
                            ),
                            text = date.dayOfMonth.toString(),
                            textAlign = TextAlign.Center,
                            color = if (isSelected) AppTheme.colorScheme.onPrimary
                            else AppTheme.colorScheme.onSurface
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

            val abstinenceDurationsInSeconds = remember(abstinenceRanges) {
                abstinenceDurationsInSeconds(abstinenceRanges)
            }

            if (abstinenceDurationsInSeconds.size > 2) {
                Card(
                    modifier = Modifier
                        .padding(top = 24.dp)
                        .padding(horizontal = 16.dp)
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
                            values = abstinenceDurationsInSeconds,
                            valueFormatter = {
                                it.seconds.formatted(accuracy = DurationFormattingAccuracy.DAYS)
                            }
                        )
                    }
                }
            }


            Card(
                modifier = Modifier
                    .padding(16.dp)
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
                            habitTracks = habitTracks,
                            abstinenceRanges = abstinenceRanges,
                            failedRanges = failedRanges,
                            currentTime = currentTime,
                            timeZone = timeZone,
                            strings = habitDetailsStrings
                        )
                    )
                }
            }
        }
    }
}

private fun buildStatisticsData(
    habitTracks: List<HabitTrack>,
    abstinenceRanges: List<ClosedRange<Instant>>,
    failedRanges: List<ClosedRange<Instant>>,
    currentTime: Instant,
    timeZone: TimeZone,
    strings: HabitDetailsStrings
) = listOf(
    StatisticData(
        name = strings.statisticsAverageAbstinenceTime(),
        value = abstinenceRanges.averageDuration().orZero().formatted(DurationFormattingAccuracy.HOURS)
    ),
    StatisticData(
        name = strings.statisticsMaxAbstinenceTime(),
        value = abstinenceRanges.maxDuration().orZero().formatted(DurationFormattingAccuracy.HOURS)
    ),
    StatisticData(
        name = strings.statisticsMinAbstinenceTime(),
        value = abstinenceRanges.minDuration().orZero().formatted(DurationFormattingAccuracy.HOURS)
    ),
    StatisticData(
        name = strings.statisticsDurationSinceFirstTrack(),
        value = habitAbstinenceDurationSinceFirstTrack(failedRanges, currentTime).orZero()
            .formatted(DurationFormattingAccuracy.HOURS)
    ),
    StatisticData(
        name = strings.statisticsCountEventsInCurrentMonth(),
        value = habitTracks.countEventsInMonth(
            monthOfYear = currentTime.monthOfYear(timeZone),
            timeZone = timeZone
        ).toString()
    ),
    StatisticData(
        name = strings.statisticsCountEventsInPreviousMonth(),
        value = habitTracks.countEventsInMonth(
            monthOfYear = currentTime.monthOfYear(timeZone).previous(),
            timeZone = timeZone
        ).toString()
    ),
    StatisticData(
        name = strings.statisticsTotalCountEvents(),
        value = habitTracks.countEvents().toString()
    )
)