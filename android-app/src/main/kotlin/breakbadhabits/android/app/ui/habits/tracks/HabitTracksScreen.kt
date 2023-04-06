package breakbadhabits.android.app.ui.habits.tracks

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import breakbadhabits.android.app.R
import breakbadhabits.android.app.di.LocalLogicModule
import breakbadhabits.android.app.di.LocalUiModule
import breakbadhabits.app.logic.habits.model.Habit
import breakbadhabits.app.logic.habits.model.HabitTrack
import breakbadhabits.foundation.controller.LoadingController
import breakbadhabits.foundation.datetime.MonthOfYear
import breakbadhabits.foundation.datetime.next
import breakbadhabits.foundation.datetime.previous
import breakbadhabits.foundation.datetime.toLocalDateRange
import breakbadhabits.foundation.math.ranges.isStartSameAsEnd
import breakbadhabits.foundation.uikit.IconButton
import breakbadhabits.foundation.uikit.LoadingBox
import breakbadhabits.foundation.uikit.LocalResourceIcon
import breakbadhabits.foundation.uikit.button.Button
import breakbadhabits.foundation.uikit.calendar.EpicCalendar
import breakbadhabits.foundation.uikit.calendar.rememberEpicCalendarState
import breakbadhabits.foundation.uikit.text.Text
import kotlinx.datetime.toLocalDateTime
import java.time.format.TextStyle
import java.util.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HabitTracksScreen(
    habitController: LoadingController<Habit?>,
    habitTracksController: LoadingController<Map<MonthOfYear, List<HabitTrack>>>,
    onTrackClick: (trackId: Int) -> Unit,
    onAddClick: () -> Unit
) {
    val logicModule = LocalLogicModule.current
    val uiModule = LocalUiModule.current
    val timeZone by logicModule.dateTimeProvider.timeZone.collectAsState()
    val dateTimeFormatter = uiModule.dateTimeFormatter

    LoadingBox(habitTracksController) { tracks ->
        val allTracks = remember(tracks) { tracks.values.flatten().toSet() }
        val months = remember(tracks) { tracks.keys }
        var currentMonth by remember(months) {
            mutableStateOf(months.maxOrNull() ?: MonthOfYear.now(timeZone))
        }
        val currentTracks = remember(currentMonth) { tracks[currentMonth] ?: emptyList() }
        val ranges = remember(tracks) {
            tracks.values.flatten().map {
                it.instantRange
            }
        }

        val epicCalendarState = rememberEpicCalendarState(
            timeZone = timeZone,
            monthOfYear = currentMonth,
            ranges = ranges
        )

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

                val title = remember(currentMonth) {
                    "${
                        currentMonth.month.getDisplayName(
                            TextStyle.FULL_STANDALONE,
                            Locale.getDefault()
                        ).replaceFirstChar { it.titlecase(Locale.getDefault()) }
                    } ${currentMonth.year}"
                }

                IconButton(
                    onClick = {
                        currentMonth = currentMonth.previous()
                    }
                ) {
                    LocalResourceIcon(resourceId = R.drawable.ic_arrow_left)
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
                        currentMonth = currentMonth.next()
                    }
                ) {
                    LocalResourceIcon(resourceId = R.drawable.ic_arrow_right)
                }
            }

            EpicCalendar(
                state = epicCalendarState,
                horizontalInnerPadding = 8.dp,
                dayBadgeText = { day ->
                    val date = day.date
                    val count = allTracks.fold(0) { count, track ->
                        val inTrack = date in track.instantRange.toLocalDateRange(timeZone)
                        if (inTrack) count + track.eventCount
                        else count
                    }

                    if (count == 0) null
                    else if (count > 100) "99+"
                    else count.toString()
                }
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
                                text = if (track.instantRange.isStartSameAsEnd) {
                                    dateTimeFormatter.formatDateTime(track.instantRange.start)
                                } else {
                                    val start = dateTimeFormatter.formatDateTime(
                                        track.instantRange.start
                                    )
                                    val end = dateTimeFormatter.formatDateTime(
                                        track.instantRange.endInclusive
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
                                    stringResource(R.string.habitEvents_noComment)
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
            icon = {
                LocalResourceIcon(resourceId = R.drawable.ic_add)
            }
        )
    }
}