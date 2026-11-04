package epicarchitect.breakbadhabits.screens.dashboard

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import epicarchitect.breakbadhabits.infrastructure.Ticks
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Surface
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import epicarchitect.breakbadhabits.database.HabitWithLastEvent
import epicarchitect.breakbadhabits.datetime.duration
import epicarchitect.breakbadhabits.format.Accuracy
import epicarchitect.breakbadhabits.format.formatted
import epicarchitect.breakbadhabits.habits.HabitIcons
import epicarchitect.breakbadhabits.habits.HabitProgress
import epicarchitect.breakbadhabits.habits.habitProgress
import epicarchitect.breakbadhabits.resources.Res
import epicarchitect.breakbadhabits.resources.habit_has_no_events
import epicarchitect.breakbadhabits.uikit.AppIconTile
import epicarchitect.breakbadhabits.uikit.HabitProgressBar
import epicarchitect.breakbadhabits.uikit.title
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Duration
import androidx.compose.ui.tooling.preview.Preview
import epicarchitect.breakbadhabits.database.Habit
import epicarchitect.breakbadhabits.uikit.PreviewTheme
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.uuid.Uuid

@Composable
fun HabitCard(
    habitWithLastEvent: HabitWithLastEvent,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource? = null,
    dragHandleModifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val now by Ticks.collectAsState()
    val duration = habitWithLastEvent.lastEventEndTime?.let { lastEventEndTime ->
        (lastEventEndTime..now).duration()
    }
    val progress = duration?.let(::habitProgress)

    val content: @Composable () -> Unit = {
        HabitCardContent(
            habitWithLastEvent = habitWithLastEvent,
            duration = duration,
            progress = progress
        )
    }

    Surface(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceContainerHighest,
        interactionSource = interactionSource
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .then(dragHandleModifier)
        ) {
            content()
        }
    }
}

@Composable
private fun HabitCardContent(
    habitWithLastEvent: HabitWithLastEvent,
    duration: Duration?,
    progress: HabitProgress?
) {
    val density = LocalDensity.current
    val typography = MaterialTheme.typography
    val textMeasurer = rememberTextMeasurer()
    val timerSlotMinHeight = remember(density, typography, textMeasurer) {
        with(density) {
            val headlineHeight = textMeasurer.measure(
                text = AnnotatedString("00d"),
                style = typography.headlineSmall,
                maxLines = 1
            ).size.height.toDp()
            val bodyHeight = textMeasurer.measure(
                text = AnnotatedString("00h 00m 00s"),
                style = typography.bodySmall,
                maxLines = 1
            ).size.height.toDp()
            headlineHeight + bodyHeight + 1.dp
        }
    }

    Column(
        modifier = Modifier.padding(
            horizontal = 18.dp,
            vertical = 16.dp
        )
    ) {
        Row(
            modifier = Modifier.heightIn(min = timerSlotMinHeight),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppIconTile(
                imageVector = HabitIcons.getById(habitWithLastEvent.habit.iconId).imageVector
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp)
            ) {
                Text(
                    text = habitWithLastEvent.habit.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = progress?.title() ?: stringResource(Res.string.habit_has_no_events),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }
            if (duration != null) {
                Spacer(Modifier.width(12.dp))
                TimerText(duration = duration)
            }
        }

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
@Composable
private fun TimerText(duration: Duration) {
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
                style = MaterialTheme.typography.headlineSmall,
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
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Preview(widthDp = 360, heightDp = 160, name = "HabitCard active")
@Composable
private fun HabitCardActivePreview() {
    PreviewTheme {
        HabitCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            habitWithLastEvent = HabitWithLastEvent(
                habit = Habit(
                    id = Uuid.random(),
                    name = "Курение",
                    iconId = 0
                ),
                lastEventEndTime = Clock.System.now() - 34.days - 4.hours
            ),
            onClick = {}
        )
    }
}

@Preview(widthDp = 360, heightDp = 160, name = "HabitCard freedom")
@Composable
private fun HabitCardFreedomPreview() {
    PreviewTheme {
        HabitCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            habitWithLastEvent = HabitWithLastEvent(
                habit = Habit(
                    id = Uuid.random(),
                    name = "Курение",
                    iconId = 0
                ),
                lastEventEndTime = Clock.System.now() - 100.days
            ),
            onClick = {}
        )
    }
}

@Preview(widthDp = 360, heightDp = 130, name = "HabitCard empty")
@Composable
private fun HabitCardEmptyPreview() {
    PreviewTheme {
        HabitCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            habitWithLastEvent = HabitWithLastEvent(
                habit = Habit(
                    id = Uuid.random(),
                    name = "Сладкое",
                    iconId = 2
                ),
                lastEventEndTime = null
            ),
            onClick = {}
        )
    }
}
