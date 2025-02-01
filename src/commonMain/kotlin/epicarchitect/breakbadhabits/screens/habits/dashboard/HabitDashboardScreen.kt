package epicarchitect.breakbadhabits.screens.habits.dashboard

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import epicarchitect.breakbadhabits.Environment
import epicarchitect.breakbadhabits.database.AppSettings
import epicarchitect.breakbadhabits.database.Habit
import epicarchitect.breakbadhabits.database.HabitEventRecord
import epicarchitect.breakbadhabits.datetime.format.DurationFormattingAccuracy
import epicarchitect.breakbadhabits.datetime.format.formatted
import epicarchitect.breakbadhabits.datetime.toMonthOfYear
import epicarchitect.breakbadhabits.screens.habits.editing.HabitEditingScreen
import epicarchitect.breakbadhabits.screens.habits.records.creation.HabitEventRecordCreationScreen
import epicarchitect.breakbadhabits.screens.habits.records.dashboard.HabitEventRecordsDashboardScreen
import epicarchitect.breakbadhabits.uikit.Card
import epicarchitect.breakbadhabits.uikit.FlowStateContainer
import epicarchitect.breakbadhabits.uikit.Histogram
import epicarchitect.breakbadhabits.uikit.IconButton
import epicarchitect.breakbadhabits.uikit.SimpleScrollableScreen
import epicarchitect.breakbadhabits.uikit.Statistics
import epicarchitect.breakbadhabits.uikit.button.Button
import epicarchitect.breakbadhabits.uikit.button.ButtonStyles
import epicarchitect.breakbadhabits.uikit.stateOfList
import epicarchitect.breakbadhabits.uikit.stateOfOneOrNull
import epicarchitect.breakbadhabits.uikit.text.Text
import epicarchitect.breakbadhabits.uikit.theme.AppTheme
import epicarchitect.calendar.compose.basis.contains
import epicarchitect.calendar.compose.basis.state.LocalBasisEpicCalendarState
import epicarchitect.calendar.compose.pager.EpicCalendarPager
import epicarchitect.calendar.compose.pager.state.rememberEpicCalendarPagerState
import epicarchitect.calendar.compose.ranges.drawEpicRanges
import kotlin.time.Duration.Companion.seconds

class HabitDashboardScreen(private val habitId: Int) : Screen {
    @Composable
    override fun Content() {
        HabitDashboard(habitId)
    }
}

@Composable
fun HabitDashboard(habitId: Int) {
    val habitQueries = Environment.database.habitQueries
    val habitEventRecordQueries = Environment.database.habitEventRecordQueries
    val appSettingsQueries = Environment.database.appSettingsQueries

    FlowStateContainer(
        state1 = stateOfOneOrNull { habitQueries.habitById(habitId) },
        state2 = stateOfList { habitEventRecordQueries.recordsByHabitId(habitId) },
        state3 = stateOfOneOrNull { habitEventRecordQueries.recordByHabitIdAndMaxEndTime(habitId) },
        state4 = stateOfOneOrNull { appSettingsQueries.settings() },
    ) { habit, habitEventRecords, lastHabitEventRecord, settings ->
        val icons = Environment.resources.icons
        val navigator = LocalNavigator.currentOrThrow
        val scrollState = rememberScrollState()
        val density = LocalDensity.current
        val showNameInAppBar by remember {
            derivedStateOf {
                with(density) {
                    scrollState.value.toDp() > 80.dp
                }
            }
        }

        SimpleScrollableScreen(
            title = if (showNameInAppBar) habit?.name.orEmpty() else "",
            scrollState = scrollState,
            onBackClick = navigator::pop,
            actions = {
                if (habit != null) {
                    IconButton(
                        onClick = { navigator += HabitEditingScreen(habit.id) },
                        icon = icons.commonIcons.settings
                    )
                }
            }
        ) {
            if (habit != null && settings != null) {
                Content(
                    habit = habit,
                    habitEventRecords = habitEventRecords,
                    lastHabitEventRecord = lastHabitEventRecord,
                    settings = settings
                )
            }
        }
    }
}

@Composable
private fun Content(
    habit: Habit,
    settings: AppSettings,
    habitEventRecords: List<HabitEventRecord>,
    lastHabitEventRecord: HabitEventRecord?
) {
    val currentTime by Environment.habitsTimePulse.state.collectAsState()
    val timeZone = Environment.dateTime.currentTimeZone()
    val state = rememberHabitDetailsState(
        habit = habit,
        habitEventRecords = habitEventRecords,
        lastTrack = lastHabitEventRecord,
        currentTime = currentTime,
        timeZone = timeZone,
        appSettings = settings
    )

    Spacer(Modifier.height(16.dp))

    HabitSection(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        habit = habit,
        state = state
    )

    Spacer(Modifier.height(16.dp))

    CalendarCard(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        habit = habit,
        state = state
    )

    if (state.abstinenceHistogramValues.size > 2) {
        Spacer(Modifier.height(16.dp))
        HistogramCard(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            state = state
        )
    }

    if (state.statisticData.isNotEmpty()) {
        Spacer(Modifier.height(16.dp))
        StatisticsCard(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            state = state
        )
    }

    Spacer(Modifier.height(16.dp))
}

@Composable
private fun HabitSection(
    state: HabitDetailsState,
    habit: Habit,
    modifier: Modifier = Modifier
) {
    val strings = Environment.resources.strings.habitDashboardStrings
    val navigator = LocalNavigator.currentOrThrow

    Column(modifier) {

        Spacer(modifier = Modifier.height(8.dp))

        if (state.gamificationData != null) {
            Box(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(34.dp)
                        .border(
                            width = 1.dp,
                            color = AppTheme.colorScheme.primary,
                            shape = CircleShape
                        ),
                    progress = {
                        state.gamificationData.progressPercentToNextLevel / 100f
                    },
                    strokeCap = StrokeCap.Round
                )

                Text(
                    text = state.gamificationData.habitLevel.value.toString(),
                    priority = Text.Priority.High,
                    type = Text.Type.Label
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
        }

        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = habit.name,
            type = Text.Type.Title
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            text = state.abstinence?.formatted(
                accuracy = DurationFormattingAccuracy.SECONDS
            ) ?: strings.habitHasNoEvents(),
            type = Text.Type.Description,
            priority = Text.Priority.Medium
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (state.gamificationData != null) {
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = "Coins: ${state.gamificationData.earnedCoins}, ${state.gamificationData.habitLevel.coinsPerSecond}/s",
                type = Text.Type.Description,
                priority = Text.Priority.Medium
            )

            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = {
                navigator += HabitEventRecordCreationScreen(habit.id)
            },
            text = strings.addHabitEventRecord(),
            style = ButtonStyles.primary
        )
    }
}

@Composable
private fun CalendarCard(
    habit: Habit,
    state: HabitDetailsState,
    modifier: Modifier = Modifier
) {
    val strings = Environment.resources.strings.habitDashboardStrings
    val navigator = LocalNavigator.currentOrThrow
    val calendarState = rememberEpicCalendarPagerState()
    val rangeColor = AppTheme.colorScheme.primary

    Card(modifier) {
        Text(
            modifier = Modifier.padding(
                start = 16.dp,
                end = 16.dp,
                bottom = 12.dp,
                top = 16.dp
            ),
            text = calendarState.currentMonth.toMonthOfYear().formatted(),
            type = Text.Type.Title
        )

        EpicCalendarPager(
            pageModifier = {
                Modifier.drawEpicRanges(state.calendarRanges, rangeColor)
            },
            dayOfMonthContent = { date ->
                val basisState = LocalBasisEpicCalendarState.current!!
                val isSelected = state.calendarRanges.any { date in it }
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
                .clickable {
                    navigator += HabitEventRecordsDashboardScreen(habit.id)
                }
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = strings.showAllTracks()
            )
        }
    }
}

@Composable
private fun StatisticsCard(
    state: HabitDetailsState,
    modifier: Modifier = Modifier
) {
    val strings = Environment.resources.strings.habitDashboardStrings
    Card(modifier) {
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
                text = strings.statisticsTitle(),
                type = Text.Type.Title
            )

            Spacer(modifier = Modifier.height(12.dp))

            Statistics(
                modifier = Modifier.fillMaxWidth(),
                statistics = state.statisticData
            )
        }
    }
}

@Composable
private fun HistogramCard(
    modifier: Modifier = Modifier,
    state: HabitDetailsState
) {
    val strings = Environment.resources.strings.habitDashboardStrings
    Card(modifier) {
        Column {
            Text(
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
                text = strings.abstinenceChartTitle(),
                type = Text.Type.Title
            )

            Histogram(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                values = state.abstinenceHistogramValues,
                valueFormatter = {
                    it.toInt().seconds.formatted(
                        accuracy = DurationFormattingAccuracy.DAYS
                    )
                }
            )
        }
    }
}