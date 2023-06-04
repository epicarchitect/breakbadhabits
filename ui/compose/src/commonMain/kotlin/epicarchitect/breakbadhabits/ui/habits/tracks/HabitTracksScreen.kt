package epicarchitect.breakbadhabits.ui.habits.tracks

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import epicarchitect.breakbadhabits.di.holder.LocalAppModule
import epicarchitect.breakbadhabits.foundation.controller.LoadingController
import epicarchitect.breakbadhabits.foundation.datetime.MonthOfYear
import epicarchitect.breakbadhabits.foundation.datetime.next
import epicarchitect.breakbadhabits.foundation.datetime.previous
import epicarchitect.breakbadhabits.foundation.math.ranges.isStartSameAsEnd
import epicarchitect.breakbadhabits.foundation.uikit.IconButton
import epicarchitect.breakbadhabits.foundation.uikit.LoadingBox
import epicarchitect.breakbadhabits.foundation.uikit.button.Button
import epicarchitect.breakbadhabits.foundation.uikit.text.Text
import epicarchitect.breakbadhabits.logic.habits.model.Habit
import epicarchitect.breakbadhabits.logic.habits.model.HabitTrack
import kotlinx.datetime.TimeZone

//import epicarchitect.breakbadhabits.foundation.uikit.R as UikitR

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HabitTracks(
    habitController: LoadingController<Habit?>,
    habitTracksController: LoadingController<Map<MonthOfYear, List<HabitTrack>>>,
    onTrackClick: (trackId: Int) -> Unit,
    onAddClick: () -> Unit
) {
    val logicModule = LocalAppModule.current.logic
    val uiModule = LocalAppModule.current.ui
    val timeZone by logicModule.dateTime.dateTimeProvider.currentTimeZoneFlow()
        .collectAsState(TimeZone.currentSystemDefault())
    val dateTimeFormatter = uiModule.format.dateTimeFormatter

    LoadingBox(habitTracksController) { tracks ->
        val allTracks = remember(tracks) { tracks.values.flatten().toSet() }
        val months = remember(tracks) { tracks.keys }
        var currentMonth by remember(months) {
            mutableStateOf(months.maxOrNull() ?: MonthOfYear.now(timeZone))
        }
        val currentTracks = remember(currentMonth) { tracks[currentMonth] ?: emptyList() }
        val ranges = remember(tracks) {
            tracks.values.flatten().map {
                it.dateTimeRange
            }
        }

//        val epicCalendarState = rememberEpicCalendarState(
//            timeZone = timeZone,
//            monthOfYear = currentMonth,
//            ranges = ranges.map { it.start.instant..it.endInclusive.instant }
//        )

        Column(modifier = Modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                LoadingBox(
                    modifier = Modifier.weight(1f),
                    controller = habitController
                ) { habit ->
                    habit ?: return@LoadingBox
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = habit.name,
                        type = Text.Type.Title,
                        maxLines = 1
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

                IconButton(
                    onClick = {
                        currentMonth = currentMonth.previous()
                    }
                ) {
//                    LocalResourceIcon(resourceId = UikitR.drawable.uikit_arrow_left)
                }

//                Text(
//                    modifier = Modifier.defaultMinSize(minWidth = 110.dp),
//                    text = title,
//                    type = Text.Type.Title,
//                    textAlign = TextAlign.Center,
//                    priority = Text.Priority.Low
//                )

                IconButton(
                    onClick = {
                        currentMonth = currentMonth.next()
                    }
                ) {
//                    LocalResourceIcon(resourceId = UikitR.drawable.uikit_arrow_right)
                }
            }

//            EpicCalendar(
//                state = epicCalendarState,
//                horizontalInnerPadding = 8.dp,
//                dayBadgeText = { day ->
//                    val date = day.date
//                    val count = allTracks.fold(0) { count, track ->
//                        val inTrack = date in track.dateTimeRange.toDateRange().let {
//                            it.start.date..it.endInclusive.date
//                        }
//                        if (inTrack) count + track.eventCount
//                        else count
//                    }
//
//                    if (count == 0) null
//                    else if (count > 100) "99+"
//                    else count.toString()
//                }
//            )

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
                                onTrackClick(track.id)
                            }
                    ) {
                        Column(
                            modifier = Modifier.padding(
                                start = 14.dp,
                                end = 14.dp,
                                top = 4.dp,
                                bottom = 4.dp
                            ),
                        ) {
                            Text(
                                modifier = Modifier.padding(2.dp),
                                text = if (track.dateTimeRange.isStartSameAsEnd) {
                                    dateTimeFormatter.formatDateTime(track.dateTimeRange.start.instant)
                                } else {
                                    val start = dateTimeFormatter.formatDateTime(
                                        track.dateTimeRange.start.instant
                                    )
                                    val end = dateTimeFormatter.formatDateTime(
                                        track.dateTimeRange.endInclusive.instant
                                    )
                                    "$start - $end"
                                },
                                type = Text.Type.Title
                            )

                            Text(
                                modifier = Modifier.padding(2.dp),
                                text = "dailyCount: " + track.eventCount
                            )

                            Text(
                                modifier = Modifier.padding(2.dp),
                                text = track.comment.ifBlank {
//                                    stringResource(R.string.habitEvents_noComment)
                                    "asd"
                                }
                            )
                        }
                    }
                }
            }
        }

        Button(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            onClick = onAddClick,
            text = "Add new events",
            type = Button.Type.Main,
//            icon = {
//                LocalResourceIcon(resourceId = R.drawable.ic_add)
//            }
        )
    }
}