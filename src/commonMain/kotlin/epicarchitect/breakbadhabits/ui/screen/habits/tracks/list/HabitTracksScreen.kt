package epicarchitect.breakbadhabits.ui.screen.habits.tracks.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import epicarchitect.breakbadhabits.data.AppData
import epicarchitect.breakbadhabits.operation.datetime.toMonthOfYear
import epicarchitect.breakbadhabits.operation.habits.dailyHabitEventCount
import epicarchitect.breakbadhabits.operation.habits.groupByMonth
import epicarchitect.breakbadhabits.operation.math.ranges.ascended
import epicarchitect.breakbadhabits.ui.format.formatted
import epicarchitect.breakbadhabits.ui.screen.habits.tracks.creation.HabitTrackCreationScreen
import epicarchitect.breakbadhabits.ui.screen.habits.tracks.editing.HabitTrackEditingScreen
import epicarchitect.breakbadhabits.ui.component.FlowStateContainer
import epicarchitect.breakbadhabits.ui.component.Icon
import epicarchitect.breakbadhabits.ui.component.IconButton
import epicarchitect.breakbadhabits.ui.component.button.Button
import epicarchitect.breakbadhabits.ui.component.stateOfList
import epicarchitect.breakbadhabits.ui.component.stateOfOneOrNull
import epicarchitect.breakbadhabits.ui.component.text.Text
import epicarchitect.breakbadhabits.ui.component.theme.AppTheme
import epicarchitect.calendar.compose.basis.contains
import epicarchitect.calendar.compose.basis.state.LocalBasisEpicCalendarState
import epicarchitect.calendar.compose.pager.EpicCalendarPager
import epicarchitect.calendar.compose.pager.state.rememberEpicCalendarPagerState
import epicarchitect.calendar.compose.ranges.drawEpicRanges
import kotlinx.coroutines.launch
import kotlinx.datetime.toLocalDateTime

class HabitTracksScreen(private val habitId: Int) : Screen {
    @Composable
    override fun Content() {
        HabitTracks(habitId)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HabitTracks(habitId: Int) {
    val navigator = LocalNavigator.currentOrThrow
    val coroutineScope = rememberCoroutineScope()
    val habitQueries = AppData.database.habitQueries
    val habitTrackQueries = AppData.database.habitTrackQueries
    val habitTracksStrings = AppData.resources.strings.habitTracksStrings
    val icons = AppData.resources.icons

    FlowStateContainer(
        state1 = stateOfOneOrNull { habitQueries.habitById(habitId) },
        state2 = stateOfList { habitTrackQueries.tracksByHabitId(habitId) }
    ) { habit, tracks ->
        val timeZone by AppData.dateTime.currentTimeZoneState.collectAsState()

        val groupedByMonthTracks = remember(tracks) {
            tracks.groupByMonth(timeZone)
        }

        val epicCalendarState = rememberEpicCalendarPagerState()

        val currentTracks = remember(epicCalendarState.currentMonth, groupedByMonthTracks) {
            groupedByMonthTracks[epicCalendarState.currentMonth.toMonthOfYear()]?.toList() ?: emptyList()
        }
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
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
                            text = habit?.name ?: "",
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

                val rangeColor = AppTheme.colorScheme.primary
                val ranges = tracks.map {
                    it.startTime.toLocalDateTime(timeZone).date..it.endTime.toLocalDateTime(timeZone).date
                }.map {
                    it.ascended()
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

                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 100.dp)
                ) {
                    items(currentTracks, key = { it.id }) { track ->
                        Box(
                            modifier = Modifier
                                .animateItemPlacement()
                                .fillMaxWidth()
                                .clickable {
                                    navigator += HabitTrackEditingScreen(track.id)
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
                                    text = track.startTime.formatted(timeZone) + " – " + track.endTime.formatted(timeZone),
                                    type = Text.Type.Title
                                )

                                Text(
                                    modifier = Modifier.padding(2.dp),
                                    text = "Event count: " + track.eventCount
                                )

                                Text(
                                    modifier = Modifier.padding(2.dp),
                                    text = "Daily event count: " + dailyHabitEventCount(
                                        eventCount = track.eventCount,
                                        startTime = track.startTime,
                                        endTime = track.endTime,
                                        timeZone = timeZone
                                    )
                                )

                                if (track.comment.isNotBlank()) {
                                    Text(
                                        modifier = Modifier.padding(2.dp),
                                        text = track.comment
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Button(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                onClick = {
                    navigator += HabitTrackCreationScreen(habitId)
                },
                text = habitTracksStrings.newEventButton(),
                type = Button.Type.Main
            )
        }
    }
}