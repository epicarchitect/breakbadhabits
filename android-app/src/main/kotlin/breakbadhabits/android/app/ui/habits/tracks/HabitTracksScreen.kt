package breakbadhabits.android.app.ui.habits.tracks

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import breakbadhabits.app.logic.habits.entity.Habit
import breakbadhabits.app.logic.habits.entity.HabitTrack
import breakbadhabits.foundation.controller.LoadingController
import breakbadhabits.foundation.datetime.MonthOfYear
import breakbadhabits.foundation.datetime.next
import breakbadhabits.foundation.datetime.previous
import breakbadhabits.foundation.uikit.IconButton
import breakbadhabits.foundation.uikit.LoadingBox
import breakbadhabits.foundation.uikit.LocalResourceIcon
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
    onTrackClick: (HabitTrack.Id) -> Unit
) {
    val logicModule = LocalLogicModule.current
    val uiModule = LocalUiModule.current
    val dateTimeConfigProvider = logicModule.dateTimeConfigProvider
    val dateTimeConfigState = dateTimeConfigProvider.configFlow().collectAsState(initial = null)
    val dateTimeConfig = dateTimeConfigState.value ?: return

    val dateTimeFormatter = uiModule.dateTimeFormatter

    LoadingBox(habitTracksController) { tracks ->
        val allTracks = remember(tracks) { tracks.values.flatten().toSet() }
        val months = remember(tracks) { tracks.keys }
        var currentMonth by remember(months) {
            mutableStateOf(months.maxOrNull() ?: MonthOfYear.now(dateTimeConfig.appTimeZone))
        }
        val currentTracks = remember(currentMonth) { tracks[currentMonth] ?: emptyList() }
        val ranges = remember(tracks) {
            tracks.values.flatten().map {
                it.time
            }
        }

        val epicCalendarState = rememberEpicCalendarState(
            timeZone = dateTimeConfig.appTimeZone,
            monthOfYear = currentMonth,
            ranges = ranges
        )

        Column(modifier = Modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
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
                        text = habit.name.value,
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
                        val inTrack = date in track.time.start.toLocalDateTime(
                            dateTimeConfig.appTimeZone
                        ).date..track.time.endInclusive.toLocalDateTime(
                            dateTimeConfig.appTimeZone
                        ).date

                        if (inTrack) count + track.eventCount.dailyCount
                        else count
                    }

                    if (count == 0) null
                    else if (count > 100) "99+"
                    else count.toString()
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(currentTracks, key = { it.id.value }) { track ->
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
                                text = when (val time = track.time) {
                                    is HabitTrack.Time.Date -> dateTimeFormatter.formatDateTime(time.value)
                                    is HabitTrack.Time.Range -> {
                                        val start = dateTimeFormatter.formatDateTime(time.start)
                                        val end =
                                            dateTimeFormatter.formatDateTime(time.endInclusive)
                                        "$start - $end"
                                    }
                                },
                                type = Text.Type.Title
                            )

                            Text(
                                modifier = Modifier.padding(2.dp),
                                text = "dailyCount: " + track.eventCount.dailyCount
                            )

                            Text(
                                modifier = Modifier.padding(2.dp),
                                text = track.comment?.value
                                    ?: stringResource(R.string.habitEvents_noComment)
                            )
                        }
                    }
                }
            }
        }
    }
}