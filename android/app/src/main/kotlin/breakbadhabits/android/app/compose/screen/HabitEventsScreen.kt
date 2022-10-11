package breakbadhabits.android.app.compose.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import breakbadhabits.android.app.R
import breakbadhabits.android.app.appDependencies
import breakbadhabits.android.app.formatter.DateTimeFormatter
import breakbadhabits.android.app.resources.HabitIconResources
import breakbadhabits.android.compose.ui.EventsCalendar
import breakbadhabits.android.compose.ui.Icon
import breakbadhabits.android.compose.ui.Text
import breakbadhabits.android.compose.ui.Title
import breakbadhabits.android.compose.ui.rememberEventsCalendarState
import epicarchitect.epicstore.compose.epicStoreItems
import epicarchitect.epicstore.compose.rememberEpicStoreEntry
import java.time.Instant
import java.time.ZoneId
import java.util.Calendar

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HabitEventsScreen(
    habitId: Int,
    openHabitEventEditing: (habitEventId: Int) -> Unit,
) {
    val dateTimeFormatter: DateTimeFormatter = appDependencies.dateTimeFormatter
    val habitIconResources: HabitIconResources =appDependencies.habitIconResources

    val habitNameFeature = rememberEpicStoreEntry {
        appDependencies.createHabitNameFeature(habitId)
    }
    val habitIconIdFeature = rememberEpicStoreEntry {
        appDependencies.createHabitIconIdFeature(habitId)
    }
    val eventIdsFeature = rememberEpicStoreEntry {
        appDependencies.createHabitEventIdsFeature(habitId)
    }
    val eventTimesFeature = rememberEpicStoreEntry {
        appDependencies.createHabitEventTimesFeature(habitId)
    }
    val habitName by habitNameFeature.state.collectAsState()
    val habitIconId by habitIconIdFeature.state.collectAsState()
    val eventIds by eventIdsFeature.state.collectAsState()
    val eventTimes by eventTimesFeature.state.collectAsState()

    val calendarState = rememberEventsCalendarState()

    LaunchedEffect(calendarState.value) {
        eventIdsFeature.setTimeFilter {
            Instant.ofEpochMilli(it)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
                .monthValue == calendarState.value.monthValue
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 32.dp)
    ) {
        item {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Icon(
                    modifier = Modifier
                        .size(44.dp)
                        .align(Alignment.CenterHorizontally),
                    painter = painterResource(habitIconResources[habitIconId ?: 0])
                )

                Title(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .align(Alignment.CenterHorizontally),
                    text = habitName ?: ""
                )
            }
        }

        item {
            EventsCalendar(
                modifier = Modifier.padding(horizontal = 8.dp),
                calendarState = calendarState,
                events = eventTimes
            )
        }

        epicStoreItems(eventIds) { eventId ->
            val timeFeature = rememberEpicStoreEntry("HabitEventTimeFeature:$eventId") {
                appDependencies.createHabitEventTimeFeature(eventId)
            }
            val commentFeature = rememberEpicStoreEntry("HabitEventCommentFeature:$eventId") {
                appDependencies.createHabitEventCommentFeature(eventId)
            }
            val time by timeFeature.state.collectAsState()
            val comment by commentFeature.state.collectAsState()

            Box(
                modifier = Modifier
                    .animateItemPlacement()
                    .fillMaxWidth()
                    .clickable {
                        openHabitEventEditing(eventId)
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
                        text = time?.let {
                            dateTimeFormatter.formatDateTime(
                                Calendar.getInstance().apply {
                                    timeInMillis = it
                                },
                                withoutYear = true
                            )
                        } ?: "-"
                    )

                    Text(
                        modifier = Modifier.padding(2.dp),
                        text = comment ?: stringResource(R.string.habitEvents_noComment)
                    )
                }
            }
        }
    }
}