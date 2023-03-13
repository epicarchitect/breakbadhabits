package breakbadhabits.android.app.ui.habits

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import breakbadhabits.android.app.R
import breakbadhabits.android.app.ui.app.LocalDateTimeFormatter
import breakbadhabits.android.app.ui.app.LocalHabitIconResourceProvider
import breakbadhabits.app.entity.Habit
import breakbadhabits.app.entity.HabitTrack
import breakbadhabits.foundation.controller.LoadingController
import breakbadhabits.foundation.datetime.MonthOfYear
import breakbadhabits.foundation.uikit.EpicCalendar
import breakbadhabits.foundation.uikit.EpicCalendarState
import breakbadhabits.foundation.uikit.LoadingBox
import breakbadhabits.foundation.uikit.button.Button
import breakbadhabits.foundation.uikit.rememberEpicCalendarState
import breakbadhabits.foundation.uikit.text.Text
import breakbadhabits.foundation.uikit.text.Title
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
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
    val context = LocalContext.current
    val habitIconResources = LocalHabitIconResourceProvider.current
    val dateTimeFormatter = LocalDateTimeFormatter.current

    LoadingBox(habitTracksController) { tracks ->
        val months = tracks.keys
        var currentMonth by remember { mutableStateOf(months.first()) }
        val currentTracks = remember(currentMonth) { tracks[currentMonth] ?: emptyList() }
        val currentRanges = remember(currentTracks) {
            currentTracks.map {
                EpicCalendarState.Range(
                    start = it.range.value.start
                        .toLocalDateTime(TimeZone.currentSystemDefault())
                        .toJavaLocalDateTime()
                        .toLocalDate(),
                    endInclusive = it.range.value.endInclusive
                        .toLocalDateTime(TimeZone.currentSystemDefault())
                        .toJavaLocalDateTime()
                        .toLocalDate()
                )
            }
        }
        val jtYearMonth = remember { YearMonth.of(currentMonth.year, currentMonth.month) }
        val epicCalendarState = rememberEpicCalendarState(
            yearMonth = jtYearMonth,
            ranges = currentRanges
        )

        Column(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = {
                    val r = YearMonth.of(currentMonth.year, currentMonth.month).minusMonths(1)
                    currentMonth = MonthOfYear(r.year, r.month)
                },
                text = "minus"
            )

            Button(
                onClick = {
                    val r = YearMonth.of(currentMonth.year, currentMonth.month).plusMonths(1)
                    currentMonth = MonthOfYear(r.year, r.month)
                },
                text = "plus"
            )

            val title = remember(jtYearMonth) {
                "${
                    jtYearMonth.month.getDisplayName(
                        TextStyle.FULL_STANDALONE,
                        Locale.getDefault()
                    ).replaceFirstChar { it.titlecase(Locale.getDefault()) }
                } ${jtYearMonth.year}"
            }

            Title(
                modifier = Modifier.padding(16.dp),
                text = title
            )

            EpicCalendar(
                state = epicCalendarState,
                horizontalInnerPadding = 8.dp
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn {
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
                                text = track.range.value.let {
                                    val start =
                                        dateTimeFormatter.formatInstantAsDate(it.start)
                                    val end =
                                        dateTimeFormatter.formatInstantAsDate(it.endInclusive)
                                    "$start - $end"
                                }
                            )

                            Text(
                                modifier = Modifier.padding(2.dp),
                                text = "dailyCount: " + track.eventCount.dailyCount
                            )

                            Text(
                                modifier = Modifier.padding(2.dp),
                                text = track.comment?.value ?: stringResource(R.string.habitEvents_noComment)
                            )
                        }
                    }
                }
            }
        }
    }
}