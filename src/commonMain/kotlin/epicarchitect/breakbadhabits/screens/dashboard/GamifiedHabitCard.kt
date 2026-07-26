package epicarchitect.breakbadhabits.screens.dashboard

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import epicarchitect.breakbadhabits.database.Habit
import epicarchitect.breakbadhabits.environment.LocalAppEnvironment
import epicarchitect.breakbadhabits.format.DurationFormatter
import epicarchitect.breakbadhabits.habits.abstinence
import epicarchitect.breakbadhabits.habits.gamification.habitGamificationData
import epicarchitect.breakbadhabits.screens.root.LocalRootNavController
import epicarchitect.breakbadhabits.screens.root.RootRoute
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

@Composable
fun GamifiedHabitCard(
    habit: Habit,
    modifier: Modifier = Modifier
) {
    val navController = LocalRootNavController.current
    val environment = LocalAppEnvironment.current
    val durationFormatter = environment.format.durationFormatter
    val eventRecordQueries = environment.database.habitEventRecordQueries
    val icons = environment.habits.icons

    val currentTime by environment.habits.timePulse.state.collectAsState()
    val lastRecordState by remember {
        eventRecordQueries.recordByHabitIdAndMaxEndTime(habit.id)
            .asFlow()
            .mapToOneOrNull(Dispatchers.IO)
    }.collectAsState(null)
    val abstinence = lastRecordState?.abstinence(currentTime)
    val gamificationData = abstinence?.let {
        habitGamificationData(
            level = environment.habitLevels.getByAbstinence(it),
            abstinence = it
        )
    }

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
                top = 16.dp,
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
                } ?: "strings.habitHasNoEvents()",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        if (gamificationData != null) {
            Row(
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(34.dp)
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.primary,
                                shape = CircleShape
                            ),
                        progress = {
                            gamificationData.progressPercentToNextLevel / 100f
                        },
                        strokeCap = StrokeCap.Round
                    )

                    Text(
                        text = gamificationData.habitLevel.index.toString()
                    )
                }

                Spacer(Modifier.width(8.dp))
                Column {
                    Text(
                        text = "abstinence for upgrade: ${gamificationData.habitLevel.nextLevel?.requiredAbstinence}",
                    )

                    Text(
                        text = "Coins per sec: ${gamificationData.habitLevel.coinsPerSecond}/s",
                    )
                }
            }
        }
    }
}