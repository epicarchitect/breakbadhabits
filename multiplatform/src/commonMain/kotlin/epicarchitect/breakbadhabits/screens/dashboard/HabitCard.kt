package epicarchitect.breakbadhabits.screens.dashboard

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import epicarchitect.breakbadhabits.database.Habit
import epicarchitect.breakbadhabits.format.Accuracy
import epicarchitect.breakbadhabits.format.formatted
import epicarchitect.breakbadhabits.habits.HabitIcons
import epicarchitect.breakbadhabits.habits.HabitsTimePulse
import epicarchitect.breakbadhabits.habits.abstinence
import epicarchitect.breakbadhabits.habits.habitProgress
import epicarchitect.breakbadhabits.infrastructure.AppDatabase
import epicarchitect.breakbadhabits.resources.Res
import epicarchitect.breakbadhabits.resources.habit_has_no_events
import epicarchitect.breakbadhabits.screens.root.LocalRootBackStack
import epicarchitect.breakbadhabits.screens.root.RootRoute
import epicarchitect.breakbadhabits.screens.root.push
import epicarchitect.breakbadhabits.uikit.HabitProgressBar
import org.jetbrains.compose.resources.stringResource

@Composable
fun HabitCard(
    habit: Habit,
    modifier: Modifier = Modifier
) {
    val backStack = LocalRootBackStack.current
    val currentTime by HabitsTimePulse.state.collectAsState()
    val records by remember(habit.id) {
        AppDatabase.habitEventRecordsDao.recordsByHabitId(habit.id)
    }.collectAsState(null)
    val lastRecord = records?.maxByOrNull { it.endTime }
    val duration = lastRecord?.abstinence(currentTime)

    Card(
        modifier = modifier,
        onClick = { backStack.push(RootRoute.HabitDetails(habit.id)) }
    ) {
        Row(
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = HabitIcons.getById(habit.iconId).imageVector,
                contentDescription = null
            )
            Text(
                modifier = Modifier.padding(start = 12.dp),
                text = habit.name,
                style = MaterialTheme.typography.titleMedium,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }
        Row(
            modifier = Modifier.padding(
                start = 16.dp,
                top = 8.dp,
                end = 16.dp,
                bottom = if (duration == null) 16.dp else 8.dp
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = Icons.Filled.AccessTime, contentDescription = null)
            Text(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .weight(1f),
                text = duration?.formatted(Accuracy.SECONDS)
                    ?: stringResource(Res.string.habit_has_no_events),
                style = MaterialTheme.typography.bodyMedium
            )
        }
        duration?.let {
            HabitProgressBar(
                progress = habitProgress(it),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            )
        }
    }
}
