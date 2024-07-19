package epicarchitect.breakbadhabits.screens.habits.records.dashboard

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import epicarchitect.breakbadhabits.Environment
import epicarchitect.breakbadhabits.uikit.FlowStateContainer
import epicarchitect.breakbadhabits.uikit.Icon
import epicarchitect.breakbadhabits.uikit.IconButton
import epicarchitect.breakbadhabits.uikit.animatedShadowElevation
import epicarchitect.breakbadhabits.uikit.button.Button
import epicarchitect.breakbadhabits.uikit.button.ButtonStyles
import epicarchitect.breakbadhabits.uikit.stateOfList
import epicarchitect.breakbadhabits.uikit.stateOfOneOrNull
import epicarchitect.breakbadhabits.uikit.text.Text
import epicarchitect.breakbadhabits.uikit.theme.AppTheme
import epicarchitect.breakbadhabits.database.Habit
import epicarchitect.breakbadhabits.database.HabitEventRecord
import epicarchitect.breakbadhabits.datetime.format.formatted
import epicarchitect.breakbadhabits.datetime.toLocalDateRange
import epicarchitect.breakbadhabits.datetime.toLocalDateTimeRange
import epicarchitect.breakbadhabits.datetime.toMonthOfYear
import epicarchitect.breakbadhabits.habits.dailyEventCount
import epicarchitect.breakbadhabits.habits.groupByMonth
import epicarchitect.breakbadhabits.habits.timeRange
import epicarchitect.breakbadhabits.math.ranges.ascended
import epicarchitect.breakbadhabits.screens.habits.records.creation.HabitEventRecordCreationScreen
import epicarchitect.breakbadhabits.screens.habits.records.editing.HabitEventRecordEditingScreen
import epicarchitect.calendar.compose.basis.contains
import epicarchitect.calendar.compose.basis.state.LocalBasisEpicCalendarState
import epicarchitect.calendar.compose.pager.EpicCalendarPager
import epicarchitect.calendar.compose.pager.state.rememberEpicCalendarPagerState
import epicarchitect.calendar.compose.ranges.drawEpicRanges
import kotlinx.coroutines.launch

class HabitEventRecordsDashboardScreen(private val habitId: Int) : Screen {
    @Composable
    override fun Content() {
        HabitEventRecordsDashboard(habitId)
    }
}

@Composable
fun HabitEventRecordsDashboard(habitId: Int) {
    val habitQueries = Environment.database.habitQueries
    val habitEventRecordQueries = Environment.database.habitEventRecordQueries

    FlowStateContainer(
        state1 = stateOfOneOrNull { habitQueries.habitById(habitId) },
        state2 = stateOfList { habitEventRecordQueries.recordsByHabitId(habitId) }
    ) { habit, records ->
        if (habit != null) {
            Content(habit, records)
        }
    }
}

@Composable
private fun Content(
    habit: Habit,
    records: List<HabitEventRecord>
) {
    val navigator = LocalNavigator.currentOrThrow
    val coroutineScope = rememberCoroutineScope()
    val strings = Environment.resources.strings.habitEventRecordsDashboardStrings
    val icons = Environment.resources.icons
    val timeZone = Environment.dateTime.currentTimeZone()

    val epicCalendarState = rememberEpicCalendarPagerState()
    val groupedByMonthRecords = remember(records) { records.groupByMonth(timeZone) }
    val currentMonthRecords = remember(
        epicCalendarState.currentMonth,
        groupedByMonthRecords
    ) {
        val monthIndex = epicCalendarState.currentMonth.toMonthOfYear()
        groupedByMonthRecords[monthIndex]?.toList() ?: emptyList()
    }

    val listState = rememberLazyListState()
    val calendarShadowElevation by listState.animatedShadowElevation(triggerScrollValue = 8.dp)
    val rangeColor = AppTheme.colorScheme.primary
    val ranges = records.map {
        it.timeRange().toLocalDateTimeRange(timeZone).toLocalDateRange().ascended()
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Surface(
                shadowElevation = calendarShadowElevation,
                color = AppTheme.colorScheme.background
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(0.5f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = navigator::pop,
                                icon = icons.commonIcons.navigationBack
                            )

                            Text(
                                text = habit.name,
                                type = Text.Type.Title,
                                maxLines = 1
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = {
                                    coroutineScope.launch {
                                        epicCalendarState.scrollMonths(-1)
                                    }
                                }
                            ) {
                                Icon(icons.commonIcons.arrowLeft)
                            }

                            Text(
                                modifier = Modifier.defaultMinSize(minWidth = 110.dp),
                                text = epicCalendarState.currentMonth.toMonthOfYear().formatted(),
                                type = Text.Type.Title,
                                textAlign = TextAlign.Center,
                                priority = Text.Priority.Low
                            )

                            IconButton(
                                onClick = {
                                    coroutineScope.launch {
                                        epicCalendarState.scrollMonths(1)
                                    }
                                }
                            ) {
                                Icon(icons.commonIcons.arrowRight)
                            }
                        }
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
                        state = epicCalendarState
                    )
                }
            }

            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {
                items(currentMonthRecords, key = { it.id }) { record ->
                   RecordItem(record)
                }
            }
        }

        Button(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            onClick = {
                navigator += HabitEventRecordCreationScreen(habit.id)
            },
            text = strings.newTrackButton(),
            style = ButtonStyles.primary
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun LazyItemScope.RecordItem(record: HabitEventRecord) {
    val navigator = LocalNavigator.currentOrThrow
    val strings = Environment.resources.strings.habitEventRecordsDashboardStrings
    val timeZone = Environment.dateTime.currentTimeZone()

    Box(
        modifier = Modifier
            .animateItemPlacement()
            .fillMaxWidth()
            .clickable {
                navigator += HabitEventRecordEditingScreen(record.id)
            }
    ) {
        Column(
            modifier = Modifier.padding(
                start = 14.dp,
                end = 14.dp,
                top = 4.dp,
                bottom = 4.dp
            )
        ) {
            Text(
                modifier = Modifier.padding(2.dp),
                text = record.timeRange().toLocalDateTimeRange(timeZone).formatted(withYear = true),
                type = Text.Type.Title
            )

            Text(
                modifier = Modifier.padding(2.dp),
                text = strings.eventCount(record.eventCount)
            )

            Text(
                modifier = Modifier.padding(2.dp),
                text = strings.dailyEventCount(record.dailyEventCount(timeZone))
            )

            if (record.comment.isNotBlank()) {
                Text(
                    modifier = Modifier.padding(2.dp),
                    text = record.comment
                )
            }
        }
    }
}