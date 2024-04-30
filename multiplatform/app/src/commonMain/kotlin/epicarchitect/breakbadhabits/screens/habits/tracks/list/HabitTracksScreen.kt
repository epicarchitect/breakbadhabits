package epicarchitect.breakbadhabits.screens.habits.tracks.list

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import epicarchitect.breakbadhabits.UpdatingAppTime
import epicarchitect.breakbadhabits.VectorIcons
import epicarchitect.breakbadhabits.database.AppData
import epicarchitect.breakbadhabits.foundation.datetime.MonthOfYear
import epicarchitect.breakbadhabits.foundation.datetime.monthOfYear
import epicarchitect.breakbadhabits.foundation.datetime.mountsBetween
import epicarchitect.breakbadhabits.foundation.uikit.IconButton
import epicarchitect.breakbadhabits.foundation.uikit.button.Button
import epicarchitect.breakbadhabits.foundation.uikit.text.Text
import epicarchitect.breakbadhabits.screens.habits.tracks.creation.HabitTrackCreationScreen
import epicarchitect.breakbadhabits.screens.habits.tracks.editing.HabitTrackEditingScreen
import epicarchitect.breakbadhabits.sqldelight.main.HabitTrack
import epicarchitect.calendar.compose.basis.EpicMonth
import epicarchitect.calendar.compose.pager.EpicCalendarPager
import epicarchitect.calendar.compose.pager.state.rememberEpicCalendarPagerState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone

class HabitTracksScreen(private val habitId: Int) : Screen {
    @Composable
    override fun Content() {
        HabitTracks(habitId)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HabitTracks(habitId: Int) {
    val resources = LocalHabitTracksResources.current
    val navigator = LocalNavigator.currentOrThrow
    val coroutineScope = rememberCoroutineScope()
    val appTime by UpdatingAppTime.state().collectAsState()

    val habit by remember(habitId) {
        AppData.mainDatabase.habitQueries
            .selectById(habitId)
            .asFlow()
            .mapToOneOrNull(Dispatchers.IO)
    }.collectAsState(null)

    val tracks by remember(habitId) {
        AppData.mainDatabase.habitTrackQueries
            .selectByHabitId(habitId)
            .asFlow()
            .mapToList(Dispatchers.IO)
    }.collectAsState(emptyList())

    val groupedByMonthTracks = remember(tracks, appTime) {
        tracks.groupByMonth(appTime.timeZone())
    }

    val epicCalendarState = rememberEpicCalendarPagerState()

    val currentTracks = remember(epicCalendarState.currentMonth, groupedByMonthTracks) {
        groupedByMonthTracks[epicCalendarState.currentMonth.fromEpic()]?.toList() ?: emptyList()
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            habit?.let {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = it.name,
                    type = Text.Type.Title,
                    maxLines = 1
                )
            }
        }

        val title = remember(epicCalendarState.currentMonth) {
            "${epicCalendarState.currentMonth.month} ${epicCalendarState.currentMonth.year}"
        }

        Row {
            IconButton(
                onClick = {
                    coroutineScope.launch {
                        epicCalendarState.scrollMonths(-1)
                    }
                }
            ) {
                epicarchitect.breakbadhabits.foundation.uikit.Icon(VectorIcons.ArrowBack)
            }

            Text(
                modifier = Modifier.defaultMinSize(minWidth = 110.dp),
                text = title,
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
                epicarchitect.breakbadhabits.foundation.uikit.Icon(VectorIcons.ArrowForward)
            }
        }

        EpicCalendarPager(
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
                            text = track.startTime.toString() + track.endTime.toString(),
                            type = Text.Type.Title
                        )

                        Text(
                            modifier = Modifier.padding(2.dp),
                            text = "dailyCount: " + track.eventCount
                        )

                        Text(
                            modifier = Modifier.padding(2.dp),
                            text = track.comment.ifBlank(resources::habitTrackNoComment)
                        )
                    }
                }
            }
        }
    }
    Button(
        modifier = Modifier
            .padding(16.dp),
        onClick = {
            navigator += HabitTrackCreationScreen(habitId)
        },
        text = resources.newEventButton(),
        type = Button.Type.Main
    )
}

//                val title = remember(currentMonth) {
//                    "${
//                        currentMonth.month.getDisplayName(
//                            TextStyle.FULL_STANDALONE,
//                            Locale.getDefault()
//                        ).replaceFirstChar { it.titlecase(Locale.getDefault()) }
//                    } ${currentMonth.year}"
//                }
//
//                IconButton(
//                    onClick = {
//                        currentMonth = currentMonth.previous()
//                    }
//                ) {
////                    LocalResourceIcon(resourceId = UikitR.drawable.uikit_arrow_left)
//                }
//
////                Text(
////                    modifier = Modifier.defaultMinSize(minWidth = 110.dp),
////                    text = title,
////                    type = Text.Type.Title,
////                    textAlign = TextAlign.Center,
////                    priority = Text.Priority.Low
////                )
//
//                IconButton(
//                    onClick = {
//                        currentMonth = currentMonth.next()
//                    }
//                ) {
////                    LocalResourceIcon(resourceId = UikitR.drawable.uikit_arrow_right)
//                }
//            }
//
////            EpicCalendar(
////                state = epicCalendarState,
////                horizontalInnerPadding = 8.dp,
////                dayBadgeText = { day ->
////                    val date = day.date
////                    val count = allTracks.fold(0) { count, track ->
////                        val inTrack = date in track.dateTimeRange.toDateRange().let {
////                            it.start.date..it.endInclusive.date
////                        }
////                        if (inTrack) count + track.eventCount
////                        else count
////                    }
////
////                    if (count == 0) null
////                    else if (count > 100) "99+"
////                    else count.toString()
////                }
////            )
//
//            Spacer(modifier = Modifier.height(8.dp))
//
//            LazyColumn(
//                modifier = Modifier.fillMaxSize(),
//                contentPadding = PaddingValues(bottom = 100.dp)
//            ) {
//                items(currentTracks, key = { it.id }) { track ->
//                    Box(
//                        modifier = Modifier
//                            .animateItemPlacement()
//                            .fillMaxWidth()
//                            .clickable {
//                                onTrackClick(track.id)
//                            }
//                    ) {
//                        Column(
//                            modifier = Modifier.padding(
//                                start = 14.dp,
//                                end = 14.dp,
//                                top = 4.dp,
//                                bottom = 4.dp
//                            )
//                        ) {
//                            Text(
//                                modifier = Modifier.padding(2.dp),
//                                text = if (track.dateTimeRange.isStartSameAsEnd) {
//                                    dateTimeFormatter.formatDateTime(
//                                        track.dateTimeRange.start
//                                    )
//                                } else {
//                                    val start = dateTimeFormatter.formatDateTime(
//                                        track.dateTimeRange.start
//                                    )
//                                    val end = dateTimeFormatter.formatDateTime(
//                                        track.dateTimeRange.endInclusive
//                                    )
//                                    "$start - $end"
//                                },
//                                type = Text.Type.Title
//                            )
//
//                            Text(
//                                modifier = Modifier.padding(2.dp),
//                                text = "dailyCount: " + track.eventCount
//                            )
//
//                            Text(
//                                modifier = Modifier.padding(2.dp),
//                                text = track.comment.ifBlank(resources::habitTrackNoComment)
//                            )
//                        }
//                    }
//                }
//            }
//        }
//
//        Button(
//            modifier = Modifier
//                .align(Alignment.BottomEnd)
//                .padding(16.dp),
//            onClick = onAddClick,
//            text = resources.newEventButton,
//            type = Button.Type.Main
////            icon = {
////                LocalResourceIcon(resourceId = R.drawable.ic_add)
////            }
//        )
//    }


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