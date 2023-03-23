package breakbadhabits.android.app.ui.habits

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import breakbadhabits.android.app.R
import breakbadhabits.android.app.ui.app.LocalDateTimeConfigProvider
import breakbadhabits.android.app.ui.app.LocalDateTimeFormatter
import breakbadhabits.android.app.ui.app.LocalHabitIconResourceProvider
import breakbadhabits.app.entity.Habit
import breakbadhabits.app.entity.HabitTrack
import breakbadhabits.foundation.controller.LoadingController
import breakbadhabits.foundation.datetime.MonthOfYear
import breakbadhabits.foundation.uikit.LoadingBox
import breakbadhabits.foundation.uikit.LocalResourceIcon
import breakbadhabits.foundation.uikit.button.Button
import breakbadhabits.foundation.uikit.calendar.EpicCalendar
import breakbadhabits.foundation.uikit.calendar.rememberEpicCalendarState
import breakbadhabits.foundation.uikit.text.Text
import breakbadhabits.foundation.uikit.text.Title
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toKotlinLocalDate
import kotlinx.datetime.toLocalDateTime
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HabitTracksScreen(
    habitController: LoadingController<Habit?>,
    habitTracksController: LoadingController<Map<MonthOfYear, List<HabitTrack>>>,
    onTrackClick: (HabitTrack.Id) -> Unit
) {
    val dateTimeConfigProvider = LocalDateTimeConfigProvider.current
    val dateTimeConfigState = dateTimeConfigProvider.configFlow().collectAsState(initial = null)
    val dateTimeConfig = dateTimeConfigState.value ?: return

    val context = LocalContext.current
    val habitIconResources = LocalHabitIconResourceProvider.current
    val dateTimeFormatter = LocalDateTimeFormatter.current

    LoadingBox(habitTracksController) { tracks ->
        val allTracks = remember(tracks) { tracks.values.flatten().toSet() }
        val months = remember(tracks) { tracks.keys }
        var currentMonth by remember(months) {
            mutableStateOf(months.maxOrNull() ?: MonthOfYear.now(dateTimeConfig.appTimeZone))
        }
        val currentTracks = remember(currentMonth) { tracks[currentMonth] ?: emptyList() }
        val ranges = remember(tracks) {
            tracks.values.flatten().map {
                it.time.start
                    .toLocalDateTime(dateTimeConfig.appTimeZone)
                    .toJavaLocalDateTime()
                    .toLocalDate()..it.time.endInclusive
                    .toLocalDateTime(dateTimeConfig.appTimeZone)
                    .toJavaLocalDateTime()
                    .toLocalDate()
            }
        }
        val jtYearMonth = remember(currentMonth) {
            YearMonth.of(currentMonth.year, currentMonth.month)
        }
        val epicCalendarState = rememberEpicCalendarState(
            yearMonth = jtYearMonth,
            ranges = ranges
        )

        Column(modifier = Modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.height(16.dp))

            LoadingBox(habitController) { habit ->
                habit ?: return@LoadingBox
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.width(16.dp))

                    LocalResourceIcon(
                        modifier = Modifier.size(22.dp),
                        resourceId = habitIconResources[habit.icon].resourceId
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = habit.name.value
                    )

                    Spacer(modifier = Modifier.width(8.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val title = remember(jtYearMonth) {
                    "${
                        jtYearMonth.month.getDisplayName(
                            TextStyle.FULL_STANDALONE,
                            Locale.getDefault()
                        ).replaceFirstChar { it.titlecase(Locale.getDefault()) }
                    } ${jtYearMonth.year}"
                }

                Button(
                    onClick = {
                        val r = YearMonth.of(currentMonth.year, currentMonth.month).minusMonths(1)
                        currentMonth = MonthOfYear(r.year, r.month)
                    },
                    text = "minus"
                )

                Title(text = title)

                Button(
                    onClick = {
                        val r = YearMonth.of(currentMonth.year, currentMonth.month).plusMonths(1)
                        currentMonth = MonthOfYear(r.year, r.month)
                    },
                    text = "plus"
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            EpicCalendar(
                state = epicCalendarState,
                horizontalInnerPadding = 8.dp,
                dayBadgeText = { day ->
                    val date = day.date.toKotlinLocalDate()
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
                                fontWeight = FontWeight.Bold
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