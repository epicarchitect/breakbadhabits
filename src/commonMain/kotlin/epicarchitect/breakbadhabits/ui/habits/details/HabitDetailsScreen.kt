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
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import epicarchitect.breakbadhabits.data.AppData
import epicarchitect.breakbadhabits.entity.datetime.duration
import epicarchitect.breakbadhabits.entity.datetime.onlyDays
import epicarchitect.breakbadhabits.entity.datetime.onlyHours
import epicarchitect.breakbadhabits.entity.datetime.onlyMinutes
import epicarchitect.breakbadhabits.entity.datetime.onlySeconds
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
    val resources = LocalHabitDetailsResources.current
    val navigator = LocalNavigator.currentOrThrow

    FlowStateContainer(
        state1 = stateOfOneOrNull {
            AppData.database.habitQueries.habitById(habitId)
        },
        state2 = stateOfList {
            AppData.database.habitTrackQueries.tracksByHabitId(habitId)
        },
        state3 = stateOfOneOrNull {
            AppData.database.habitTrackQueries.trackByHabitIdAndMaxEndTime(habitId)
        }
    ) { habit, habitTracks, lastTrack ->
        val appTime by AppData.userDateTime.collectAsState()
        val timeZone = appTime.timeZone()

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
                    Icon(VectorIcons.ArrowBack)
                }

                Icon(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .size(44.dp),
                    icon = HabitIcons[habit?.iconId ?: 0]
                )

                IconButton(
                    onClick = {
                        navigator += HabitEditingScreen(habitId)
                    }
                ) {
                    Icon(VectorIcons.Settings)
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
                    ).toString()
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
                        text = "Перейти к событиям"
                        // stringResource(R.string.habit_showAllEvents)
                    )
                }
            }

            val abstinenceTimes = remember(abstinenceHistory) {
                abstinenceHistory.abstinenceRanges().map {
                    it.duration().inWholeSeconds.toFloat()
                }
            }

            if (abstinenceTimes.size > 3) {
                Card(
                    modifier = Modifier
                        .padding(top = 24.dp)
                        .fillMaxWidth()
                ) {
                    Column {
                        Text(
                            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
                            text = resources.abstinenceChartTitle(),
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
                        text = resources.statisticsTitle(),
                        type = Text.Type.Title
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Statistics(
                        modifier = Modifier.fillMaxWidth(),
                        statistics = remember(habitEventAmountStatistics, abstinenceStatistics) {
                            buildStatisticsData(abstinenceStatistics, habitEventAmountStatistics, resources)
                        }
                    )
                }
            }
        }
    }
}

private fun buildStatisticsData(
    abstinenceStatistics: HabitAbstinenceStatistics,
    eventAmountStatistics: HabitEventAmountStatistics,
    resources: HabitDetailsResources
) = listOf(
    StatisticData(
        name = resources.statisticsAverageAbstinenceTime(),
        value = FormattedDuration(
            value = abstinenceStatistics.averageDuration() ?: Duration.ZERO,
            accuracy = FormattedDuration.Accuracy.HOURS
        ).toString()
    ),
    StatisticData(
        name = resources.statisticsMaxAbstinenceTime(),
        value = FormattedDuration(
            value = abstinenceStatistics.maxDuration() ?: Duration.ZERO,
            accuracy = FormattedDuration.Accuracy.HOURS
        ).toString()
    ),
    StatisticData(
        name = resources.statisticsMinAbstinenceTime(),
        value = FormattedDuration(
            value = abstinenceStatistics.minDuration() ?: Duration.ZERO,
            accuracy = FormattedDuration.Accuracy.HOURS
        ).toString()
    ),
    StatisticData(
        name = resources.statisticsDurationSinceFirstTrack(),
        value = FormattedDuration(
            value = abstinenceStatistics.durationSinceFirstTrack() ?: Duration.ZERO,
            accuracy = FormattedDuration.Accuracy.HOURS
        ).toString()
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

class FormattedDuration(
    val value: Duration,
    val accuracy: Accuracy
) : CharSequence {

    private val resources = if (Locale.current.language == "ru") {
        object : Resources {
            override fun secondsText() = "с"
            override fun minutesText() = "м"
            override fun hoursText() = "ч"
            override fun daysText() = "д"
        }
    } else {
        object : Resources {
            override fun secondsText() = "s"
            override fun minutesText() = "m"
            override fun hoursText() = "h"
            override fun daysText() = "d"
        }
    }

    private val formatted by lazy {
        format(value, accuracy)
    }
    override val length: Int
        get() = formatted.length

    override fun get(index: Int) = formatted[index]

    override fun subSequence(startIndex: Int, endIndex: Int) = formatted.subSequence(startIndex, endIndex)

    override fun toString() = formatted

    private fun format(
        duration: Duration,
        accuracy: Accuracy
    ): String {
        var result = ""
        val seconds = duration.onlySeconds
        val minutes = duration.onlyMinutes
        val hours = duration.onlyHours
        val days = duration.onlyDays

        val appendDays = days != 0L
        val appendHours = hours != 0L && (!appendDays || accuracy.order > 1)
        val appendMinutes = minutes != 0L && (!appendDays && !appendHours || accuracy.order > 2)
        val appendSeconds = !appendDays && !appendHours && !appendMinutes || accuracy.order > 3

        if (appendDays) {
            result += days
            result += resources.daysText()
        }

        if (appendHours) {
            if (appendDays) result += " "
            result += hours
            result += resources.hoursText()
        }

        if (appendMinutes) {
            if (appendHours || appendDays) result += " "
            result += minutes
            result += resources.minutesText()
        }

        if (appendSeconds) {
            if (appendMinutes || appendHours || appendDays) result += " "
            result += seconds
            result += resources.secondsText()
        }

        return result
    }

    enum class Accuracy(val order: Int) {
        DAYS(1),
        HOURS(2),
        MINUTES(3),
        SECONDS(4)
    }

    private interface Resources {
        fun secondsText(): String
        fun minutesText(): String
        fun hoursText(): String
        fun daysText(): String
    }
}