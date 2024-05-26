package epicarchitect.breakbadhabits.ui.habits.tracks.list

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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import epicarchitect.breakbadhabits.data.AppData
import epicarchitect.breakbadhabits.data.HabitTrack
import epicarchitect.breakbadhabits.entity.datetime.MonthOfYear
import epicarchitect.breakbadhabits.entity.datetime.PlatformDateTimeFormatter
import epicarchitect.breakbadhabits.entity.datetime.monthOfYear
import epicarchitect.breakbadhabits.entity.datetime.mountsBetween
import epicarchitect.breakbadhabits.ui.habits.tracks.creation.HabitTrackCreationScreen
import epicarchitect.breakbadhabits.ui.habits.tracks.editing.HabitTrackEditingScreen
import epicarchitect.breakbadhabits.uikit.FlowStateContainer
import epicarchitect.breakbadhabits.uikit.Icon
import epicarchitect.breakbadhabits.uikit.IconButton
import epicarchitect.breakbadhabits.uikit.button.Button
import epicarchitect.breakbadhabits.uikit.stateOfList
import epicarchitect.breakbadhabits.uikit.stateOfOneOrNull
import epicarchitect.breakbadhabits.uikit.text.Text
import epicarchitect.breakbadhabits.uikit.theme.AppTheme
import epicarchitect.calendar.compose.basis.EpicMonth
import epicarchitect.calendar.compose.pager.EpicCalendarPager
import epicarchitect.calendar.compose.pager.state.rememberEpicCalendarPagerState
import epicarchitect.calendar.compose.ranges.drawEpicRanges
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
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
        val timeZone = AppData.userDateTime.timeZone()

        val groupedByMonthTracks = remember(tracks) {
            tracks.groupByMonth(timeZone)
        }

        val epicCalendarState = rememberEpicCalendarPagerState()

        val currentTracks = remember(epicCalendarState.currentMonth, groupedByMonthTracks) {
            groupedByMonthTracks[epicCalendarState.currentMonth.fromEpic()]?.toList() ?: emptyList()
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
                            text = PlatformDateTimeFormatter.monthOfYear(epicCalendarState.currentMonth.fromEpic()),
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

                EpicCalendarPager(
                    pageModifier = {
                        Modifier.drawEpicRanges(
                            ranges = tracks.map {
                                it.startTime.toLocalDateTime(timeZone).date..it.endTime.toLocalDateTime(timeZone).date
                            },
                            color = rangeColor
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
                                    text = track.startTime.let {
                                        PlatformDateTimeFormatter.localDateTime(it.toLocalDateTime(timeZone))
                                    } + " – " + track.endTime.let {
                                        PlatformDateTimeFormatter.localDateTime(it.toLocalDateTime(timeZone))
                                    },
                                    type = Text.Type.Title
                                )

                                Text(
                                    modifier = Modifier.padding(2.dp),
                                    text = "Event count: " + track.eventCount
                                )

                                val days = (track.endTime - track.startTime).inWholeDays

                                if (days > 0) {
                                    Text(
                                        modifier = Modifier.padding(2.dp),
                                        text = "Daily event count: " + track.eventCount / days
                                    )
                                }

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

private fun List<HabitTrack>.groupByMonth(timeZone: TimeZone): Map<MonthOfYear, Collection<HabitTrack>> {
    val map = mutableMapOf<MonthOfYear, MutableSet<HabitTrack>>()
    forEach { track ->
        val startMonth = track.startTime.monthOfYear(timeZone)
        val endMonth = track.endTime.monthOfYear(timeZone)
        val monthRange = startMonth..endMonth
        map.getOrPut(startMonth, ::mutableSetOf).add(track)
        map.getOrPut(endMonth, ::mutableSetOf).add(track)
        monthRange.mountsBetween().forEach {
            map.getOrPut(it, ::mutableSetOf).add(track)
        }
    }
    return map
}

fun EpicMonth.fromEpic() = MonthOfYear(year, month)