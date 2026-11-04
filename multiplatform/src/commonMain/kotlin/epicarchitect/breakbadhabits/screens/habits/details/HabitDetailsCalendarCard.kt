package epicarchitect.breakbadhabits.screens.habits.details

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import epicarchitect.breakbadhabits.datetime.toMonthOfYear
import epicarchitect.breakbadhabits.format.formatted
import epicarchitect.breakbadhabits.resources.Res
import epicarchitect.breakbadhabits.resources.habit_details_show_all_events
import epicarchitect.breakbadhabits.screens.root.LocalRootBackStack
import epicarchitect.breakbadhabits.screens.root.RootRoute
import epicarchitect.breakbadhabits.screens.root.push
import epicarchitect.calendar.compose.basis.contains
import epicarchitect.calendar.compose.basis.state.LocalBasisEpicCalendarState
import epicarchitect.calendar.compose.pager.EpicCalendarPager
import epicarchitect.calendar.compose.pager.state.rememberEpicCalendarPagerState
import epicarchitect.calendar.compose.ranges.drawEpicRanges
import org.jetbrains.compose.resources.stringResource

@Composable
fun HabitDetailsCalendarCard(
    modifier: Modifier = Modifier,
    state: HabitDetailsScreenState
) {
    val backStack = LocalRootBackStack.current
    val rangeColor = MaterialTheme.colorScheme.primary
    val calendarState = rememberEpicCalendarPagerState()

    Card(modifier) {
        Text(
            modifier = Modifier.padding(
                start = 16.dp,
                end = 16.dp,
                bottom = 12.dp,
                top = 16.dp
            ),
            text = calendarState.currentMonth.toMonthOfYear().formatted(),
            style = MaterialTheme.typography.titleMedium
        )

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
            state = calendarState
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .clickable {
                    backStack.push(
                        RootRoute.HabitEventRecordsDetails(
                            habitId = state.habit.id
                        )
                    )
                }
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = stringResource(Res.string.habit_details_show_all_events),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
