package epicarchitect.breakbadhabits.features.habits.tracks

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf

val LocalHabitTracksResources = compositionLocalOf<HabitTracksResources> {
    error("LocalHabitTracksResources not provided")
}

interface HabitTracksResources {
    val newEventButton: String
    val habitTrackNoComment: String
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HabitTracks() {
//    val resources = LocalHabitTracksResources.current
//    val logicModule = LocalAppModule.current
//    val uiModule = LocalAppModule.current
//    val timeZone by logicModule.dateTime.dateTimeProvider.currentTimeZoneFlow()
//        .collectAsState(TimeZone.currentSystemDefault())
//    val dateTimeFormatter = uiModule.format.dateTimeFormatter
//
//    LoadingBox(viewModel.habitTracksController) { tracks ->
//        val allTracks = remember(tracks) { tracks.values.flatten().toSet() }
//        val months = remember(tracks) { tracks.keys }
//        var currentMonth by remember(months) {
//            mutableStateOf(months.maxOrNull() ?: MonthOfYear.now(timeZone))
//        }
//        val currentTracks = remember(currentMonth) { tracks[currentMonth] ?: emptyList() }
//        val ranges = remember(tracks) {
//            tracks.values.flatten().map {
//                it.dateTimeRange
//            }
//        }
//
////        val epicCalendarState = rememberEpicCalendarState(
////            timeZone = timeZone,
////            monthOfYear = currentMonth,
////            ranges = ranges.map { it.start.instant..it.endInclusive.instant }
////        )
//
//        Column(modifier = Modifier.fillMaxWidth()) {
//            Spacer(modifier = Modifier.height(16.dp))
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(start = 16.dp, end = 4.dp),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                LoadingBox(
//                    modifier = Modifier.weight(1f),
//                    controller = viewModel.habitController
//                ) { habit ->
//                    habit ?: return@LoadingBox
//                    Text(
//                        modifier = Modifier.fillMaxWidth(),
//                        text = habit.name,
//                        type = Text.Type.Title,
//                        maxLines = 1
//                    )
//                }
//
////                val title = remember(currentMonth) {
////                    "${
////                        currentMonth.month.getDisplayName(
////                            TextStyle.FULL_STANDALONE,
////                            Locale.getDefault()
////                        ).replaceFirstChar { it.titlecase(Locale.getDefault()) }
////                    } ${currentMonth.year}"
////                }
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
}