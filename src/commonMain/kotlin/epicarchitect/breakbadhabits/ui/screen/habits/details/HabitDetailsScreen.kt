package epicarchitect.breakbadhabits.ui.screen.habits.details

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import epicarchitect.breakbadhabits.data.AppData
import epicarchitect.breakbadhabits.data.Habit
import epicarchitect.breakbadhabits.data.HabitEventRecord
import epicarchitect.breakbadhabits.operation.datetime.toMonthOfYear
import epicarchitect.breakbadhabits.ui.component.Card
import epicarchitect.breakbadhabits.ui.component.FlowStateContainer
import epicarchitect.breakbadhabits.ui.component.Histogram
import epicarchitect.breakbadhabits.ui.component.Icon
import epicarchitect.breakbadhabits.ui.component.IconButton
import epicarchitect.breakbadhabits.ui.component.SimpleScrollableScreen
import epicarchitect.breakbadhabits.ui.component.Statistics
import epicarchitect.breakbadhabits.ui.component.button.Button
import epicarchitect.breakbadhabits.ui.component.button.ButtonStyles
import epicarchitect.breakbadhabits.ui.component.stateOfList
import epicarchitect.breakbadhabits.ui.component.stateOfOneOrNull
import epicarchitect.breakbadhabits.ui.component.text.Text
import epicarchitect.breakbadhabits.ui.component.theme.AppTheme
import epicarchitect.breakbadhabits.ui.format.DurationFormattingAccuracy
import epicarchitect.breakbadhabits.ui.format.formatted
import epicarchitect.breakbadhabits.ui.screen.habits.editing.HabitEditingScreen
import epicarchitect.breakbadhabits.ui.screen.habits.records.creation.HabitEventRecordCreationScreen
import epicarchitect.breakbadhabits.ui.screen.habits.records.list.HabitEventRecordsScreen
import epicarchitect.calendar.compose.basis.contains
import epicarchitect.calendar.compose.basis.state.LocalBasisEpicCalendarState
import epicarchitect.calendar.compose.pager.EpicCalendarPager
import epicarchitect.calendar.compose.pager.state.rememberEpicCalendarPagerState
import epicarchitect.calendar.compose.ranges.drawEpicRanges
import kotlin.time.Duration.Companion.seconds

class HabitDetailsScreen(private val habitId: Int) : Screen {
    @Composable
    override fun Content() {
        HabitDetails(habitId)
    }
}

@Composable
fun HabitDetails(habitId: Int) {
    val habitQueries = AppData.database.habitQueries
    val habitEventRecordQueries = AppData.database.habitEventRecordQueries

    FlowStateContainer(
        state1 = stateOfOneOrNull { habitQueries.habitById(habitId) },
        state2 = stateOfList { habitEventRecordQueries.recordsByHabitId(habitId) },
        state3 = stateOfOneOrNull { habitEventRecordQueries.recordByHabitIdAndMaxEndTime(habitId) }
    ) { habit, habitEventRecords, lastHabitEventRecord ->
        val icons = AppData.resources.icons
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
            if (habit != null) {
                Content(
                    habit = habit,
                    habitEventRecords = habitEventRecords,
                    lastHabitEventRecord = lastHabitEventRecord
                )
            }
        }
    }
}

@Composable
private fun Content(
    habit: Habit,
    habitEventRecords: List<HabitEventRecord>,
    lastHabitEventRecord: HabitEventRecord?
) {
    val currentTime by AppData.dateTime.currentInstantState.collectAsState()
    val timeZone by AppData.dateTime.currentTimeZoneState.collectAsState()
    val state = rememberHabitDetailsState(
        habitEventRecords = habitEventRecords,
        lastTrack = lastHabitEventRecord,
        currentTime = currentTime,
        timeZone = timeZone
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
    val strings = AppData.resources.strings.habitDetailsStrings
    val icons = AppData.resources.icons
    val navigator = LocalNavigator.currentOrThrow

    Column(modifier) {
        Icon(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(44.dp),
            icon = icons.habitIcons.getById(habit.iconId)
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
                .align(Alignment.CenterHorizontally),
            text = state.abstinence?.formatted(
                accuracy = DurationFormattingAccuracy.SECONDS
            ) ?: strings.habitHasNoEvents(),
            type = Text.Type.Description,
            priority = Text.Priority.Medium
        )

        Spacer(modifier = Modifier.height(8.dp))

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
    val strings = AppData.resources.strings.habitDetailsStrings
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
                    navigator += HabitEventRecordsScreen(habit.id)
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
    val strings = AppData.resources.strings.habitDetailsStrings
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
    val strings = AppData.resources.strings.habitDetailsStrings
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
                    it.toInt().seconds.formatted(DurationFormattingAccuracy.DAYS)
                }
            )
        }
    }
}