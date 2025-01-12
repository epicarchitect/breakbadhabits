package epicarchitect.breakbadhabits.screens.habits.eventRecords.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import epicarchitect.breakbadhabits.datetime.toMonthOfYear
import epicarchitect.breakbadhabits.environment.LocalAppEnvironment
import epicarchitect.breakbadhabits.screens.root.LocalRootNavController
import epicarchitect.breakbadhabits.screens.root.RootRoute
import epicarchitect.calendar.compose.basis.contains
import epicarchitect.calendar.compose.basis.state.LocalBasisEpicCalendarState
import epicarchitect.calendar.compose.pager.EpicCalendarPager
import epicarchitect.calendar.compose.ranges.drawEpicRanges
import kotlinx.coroutines.launch

@Composable
fun HabitEventRecordsDetailsScreen(habitId: Int) {
    val state = rememberHabitEventRecordsDetailsScreenState(habitId) ?: return
    val environment = LocalAppEnvironment.current
    val navController = LocalRootNavController.current
    val monthFormatter = environment.format.monthFormatter
    val strings = environment.resources.strings.habitEventRecordsDashboardStrings

    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val rangeColor = MaterialTheme.colorScheme.primary

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
                    IconButton(onClick = navController::popBackStack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }

                    Text(
                        text = state.habit.name,
                        maxLines = 1,
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            coroutineScope.launch {
                                state.calendarState.scrollMonths(-1)
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = null
                        )
                    }

                    Text(
                        modifier = Modifier.defaultMinSize(minWidth = 110.dp),
                        text = monthFormatter.format(state.calendarState.currentMonth.toMonthOfYear()),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleSmall
                    )

                    IconButton(
                        onClick = {
                            coroutineScope.launch {
                                state.calendarState.scrollMonths(1)
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = null
                        )
                    }
                }
            }
            EpicCalendarPager(
                pageModifier = {
                    Modifier.drawEpicRanges(state.calendarRanges, rangeColor)
                },
                dayOfMonthContent = { date ->
                    val basisState = LocalBasisEpicCalendarState.current!!
                    val isSelected = state.calendarRanges.any { date in it }
                    Text(
                        modifier = Modifier.alpha(
                            if (date in basisState.currentMonth) 1.0f
                            else 0.5f
                        ),
                        text = date.dayOfMonth.toString(),
                        textAlign = TextAlign.Center,
                        color = if (isSelected) MaterialTheme.colorScheme.onPrimary
                        else MaterialTheme.colorScheme.onSurface
                    )
                },
                state = state.calendarState
            )

            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {
                items(
                    items = state.currentMonthRecords,
                    key = { it.id }
                ) {
                    HabitRecordItem(it)
                }
            }
        }

        Button(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            onClick = {
                navController.navigate(
                    RootRoute.HabitEventRecordEditing(
                        habitEventRecordId = null,
                        habitId = habitId
                    )
                )
            }
        ) {
            Text(text = strings.newTrackButton())
        }
    }
}