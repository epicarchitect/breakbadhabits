package epicarchitect.breakbadhabits.screens.habits.eventRecords.details
import androidx.compose.foundation.background

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Card
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
import epicarchitect.breakbadhabits.format.formatted
import epicarchitect.breakbadhabits.resources.Res
import epicarchitect.breakbadhabits.resources.add_event_record
import epicarchitect.breakbadhabits.resources.habit_details_empty_title
import epicarchitect.breakbadhabits.screens.root.LocalNavBackStack
import epicarchitect.breakbadhabits.screens.root.RootRoute
import epicarchitect.breakbadhabits.screens.root.push
import epicarchitect.breakbadhabits.uikit.AdaptiveContent
import epicarchitect.breakbadhabits.uikit.AppBackButton
import epicarchitect.breakbadhabits.uikit.AppTopBar
import epicarchitect.breakbadhabits.uikit.AdaptiveContentWidth
import epicarchitect.calendar.compose.basis.contains
import epicarchitect.calendar.compose.basis.state.LocalBasisEpicCalendarState
import epicarchitect.calendar.compose.pager.EpicCalendarPager
import epicarchitect.calendar.compose.ranges.drawEpicRanges
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import kotlin.uuid.Uuid

@Composable
fun HabitEventRecordsDetailsScreen(habitId: Uuid) {
    val state = rememberHabitEventRecordsDetailsScreenState(habitId) ?: return
    val navBackStack = LocalNavBackStack.current

    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val rangeColor = MaterialTheme.colorScheme.primary
    val navigationBarBottom = WindowInsets.navigationBars
        .asPaddingValues()
        .calculateBottomPadding()

    AdaptiveContent(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        width = AdaptiveContentWidth.Wide
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            AppTopBar(
                title = state.habit.name,
                navigationIcon = {
                    AppBackButton(onClick = { navBackStack.removeLastOrNull() })
                },
                actions = {
                    IconButton(
                        onClick = {
                            navBackStack.push(
                                RootRoute.HabitEventRecordEditing(
                                    habitEventRecordId = null,
                                    habitId = habitId
                                )
                            )
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = stringResource(Res.string.add_event_record),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 16.dp,
                        vertical = 12.dp
                    )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
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
                        text = state.calendarState.currentMonth.toMonthOfYear().formatted(),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
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
                            text = date.day.toString(),
                            textAlign = TextAlign.Center,
                            color = if (isSelected) {
                                MaterialTheme.colorScheme.onPrimary
                            } else {
                                MaterialTheme.colorScheme.onSurface
                            }
                        )
                    },
                    state = state.calendarState
                )
            }

            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    top = 12.dp,
                    bottom = 24.dp + navigationBarBottom
                )
            ) {
                if (state.currentMonthRecords.isEmpty()) {
                    item {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 32.dp, vertical = 24.dp),
                            text = stringResource(Res.string.habit_details_empty_title),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                items(
                    items = state.currentMonthRecords,
                    key = { it.id }
                ) {
                    HabitRecordItem(it)
                }
            }
        }
    }
}
