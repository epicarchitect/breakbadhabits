package epicarchitect.breakbadhabits.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import epicarchitect.breakbadhabits.database.HabitWithLastEvent
import epicarchitect.breakbadhabits.datetime.duration
import epicarchitect.breakbadhabits.format.Accuracy
import epicarchitect.breakbadhabits.format.formatted
import epicarchitect.breakbadhabits.habits.HabitIcons
import epicarchitect.breakbadhabits.habits.habitProgress
import epicarchitect.breakbadhabits.resources.Res
import epicarchitect.breakbadhabits.resources.habit_has_no_events
import epicarchitect.breakbadhabits.uikit.HabitProgressBar
import epicarchitect.breakbadhabits.uikit.LocalInstant
import epicarchitect.breakbadhabits.uikit.title
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Duration

@Composable
fun HabitCard(
    habitWithLastEvent: HabitWithLastEvent,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val duration = habitWithLastEvent.lastEventEndTime?.let { lastEventEndTime ->
        val currentTime = LocalInstant.current.value
        (lastEventEndTime..currentTime).duration()
    }
    val progress = duration?.let(::habitProgress)

    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.14f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = HabitIcons.getById(habitWithLastEvent.habit.iconId).imageVector,
                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                        contentDescription = null
                    )
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 12.dp)
                ) {
                    Text(
                        text = habitWithLastEvent.habit.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                    if (progress != null) {
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = progress.title(),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1
                        )
                    }
                }
                Spacer(Modifier.width(12.dp))
                TimerText(duration = duration)
            }

            if (progress != null) {
                Spacer(Modifier.height(10.dp))
                HabitProgressBar(
                    progress = progress,
                    modifier = Modifier.fillMaxWidth(),
                    showStageTitle = false,
                    showStageNumber = false,
                    showDays = false
                )
            }
        }
    }
}

@Composable
private fun TimerText(duration: Duration?) {
    if (duration == null) {
        Text(
            text = stringResource(Res.string.habit_has_no_events),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        return
    }

    val formatted = duration.formatted(Accuracy.SECONDS)
    val firstSpace = formatted.indexOf(' ')
    val hasDays = duration.inWholeDays > 0 && firstSpace > 0

    if (hasDays) {
        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            Text(
                text = formatted.take(firstSpace),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = formatted.substring(firstSpace + 1),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    } else {
        Text(
            text = formatted,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}
