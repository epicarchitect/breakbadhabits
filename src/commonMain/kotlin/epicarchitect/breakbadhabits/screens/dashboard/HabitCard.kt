package epicarchitect.breakbadhabits.screens.dashboard

import androidx.compose.foundation.layout.Row
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
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import epicarchitect.breakbadhabits.database.Habit
import epicarchitect.breakbadhabits.environment.LocalAppEnvironment
import epicarchitect.breakbadhabits.format.DurationFormatter
import epicarchitect.breakbadhabits.habits.abstinence
import epicarchitect.breakbadhabits.screens.root.LocalRootNavController
import epicarchitect.breakbadhabits.screens.root.RootRoute
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

@Composable
fun HabitCard(
    habit: Habit,
    modifier: Modifier = Modifier
) {
    val environment = LocalAppEnvironment.current
    val navController = LocalRootNavController.current
    val durationFormatter = environment.format.durationFormatter
    val eventRecordQueries = environment.database.habitEventRecordQueries
    val strings = environment.resources.strings.appDashboardStrings
    val icons = environment.habits.icons

    val currentTime by environment.habits.timePulse.state.collectAsState()
    val lastRecordState = remember {
        eventRecordQueries.recordByHabitIdAndMaxEndTime(habit.id)
            .asFlow()
            .mapToOneOrNull(Dispatchers.IO)
    }.collectAsState(null)
    val abstinence = lastRecordState.value?.abstinence(currentTime)

    Card(
        modifier = modifier,
        onClick = {
            navController.navigate(RootRoute.HabitDetails(habit.id))
        },
    ) {
        Row(
            modifier = Modifier.padding(
                start = 16.dp,
                top = 16.dp,
                end = 16.dp
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icons.getById(habit.iconId).imageVector,
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
                bottom = 16.dp
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.AccessTime,
                contentDescription = null
            )

            Text(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .weight(1f),
                text = abstinence?.let {
                    durationFormatter.format(
                        duration = it,
                        accuracy = DurationFormatter.Accuracy.SECONDS
                    )
                } ?: strings.habitHasNoEvents(),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}