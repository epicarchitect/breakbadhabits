package epicarchitect.breakbadhabits.screens.habits.details

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import epicarchitect.breakbadhabits.format.Accuracy
import epicarchitect.breakbadhabits.format.formatted
import epicarchitect.breakbadhabits.habits.HabitProgress
import epicarchitect.breakbadhabits.habits.HabitStage
import epicarchitect.breakbadhabits.habits.habitProgress
import epicarchitect.breakbadhabits.resources.Res
import epicarchitect.breakbadhabits.resources.habit_details_current_progress
import epicarchitect.breakbadhabits.resources.habit_details_without_slips
import epicarchitect.breakbadhabits.resources.habit_record_slip
import epicarchitect.breakbadhabits.resources.habit_stage_days_current
import epicarchitect.breakbadhabits.resources.habit_stage_days_done
import epicarchitect.breakbadhabits.resources.habit_stage_days_total
import epicarchitect.breakbadhabits.uikit.PreviewTheme
import epicarchitect.breakbadhabits.uikit.SecondaryButton
import epicarchitect.breakbadhabits.uikit.description
import epicarchitect.breakbadhabits.uikit.title
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

@Composable
fun HabitStageCard(
    progress: HabitProgress,
    duration: Duration,
    onRecordSlip: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
        Text(
            text = stringResource(Res.string.habit_details_current_progress),
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(Modifier.height(12.dp))

        TimerRow(duration = duration)

        Spacer(Modifier.height(12.dp))

        HabitStage.entries.forEachIndexed { index, stage ->
            StageRow(
                stage = stage,
                progress = progress,
                isLast = index == HabitStage.entries.lastIndex
            )
        }

        Spacer(Modifier.height(16.dp))

            SecondaryButton(
                text = stringResource(Res.string.habit_record_slip),
                onClick = onRecordSlip,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun TimerRow(duration: Duration) {
    val formatted = duration.formatted(Accuracy.SECONDS)
    val firstSpace = formatted.indexOf(' ')
    val leading = if (firstSpace == -1) formatted else formatted.take(firstSpace)
    val remainder = if (firstSpace == -1) null else formatted.substring(firstSpace + 1)

    Row(verticalAlignment = Alignment.Bottom) {
        Text(
            text = leading,
            style = MaterialTheme.typography.titleLarge
        )
        if (remainder != null) {
            Spacer(Modifier.width(6.dp))
            Text(
                text = remainder,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Spacer(Modifier.width(6.dp))
        Text(
            text = stringResource(Res.string.habit_details_without_slips),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun StageRow(
    stage: HabitStage,
    progress: HabitProgress,
    isLast: Boolean
) {
    val isDone = progress.isComplete || stage.ordinal < progress.stage.ordinal
    val isCurrent = !progress.isComplete && stage == progress.stage
    val total = stage.days.count()
    val passed = (progress.days - stage.days.first).coerceIn(0, total)
    val fraction = when {
        isCurrent -> progress.stageFraction
        isDone -> 1f
        else -> 0f
    }

    Row(Modifier.height(IntrinsicSize.Min)) {
        StageRail(
            done = isDone,
            current = isCurrent,
            isLast = isLast,
            fraction = fraction,
            modifier = Modifier
                .width(20.dp)
                .fillMaxHeight()
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(
                    start = 10.dp,
                    bottom = if (isLast) 0.dp else 12.dp
                )
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stage.title(),
                    modifier = Modifier.weight(1f, fill = false),
                    style = MaterialTheme.typography.bodyMedium,
                    color = when {
                        isCurrent -> MaterialTheme.colorScheme.onSurface
                        isDone -> MaterialTheme.colorScheme.onSurfaceVariant
                        else -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    },
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(Modifier.width(8.dp))

                Text(
                    modifier = Modifier
                        .background(
                            color = if (isCurrent) {
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.20f)
                            } else {
                                MaterialTheme.colorScheme.surfaceContainerHigh
                            },
                            shape = MaterialTheme.shapes.extraLarge
                        )
                        .padding(horizontal = 9.dp, vertical = 4.dp),
                    text = when {
                        isDone -> pluralStringResource(
                            Res.plurals.habit_stage_days_done,
                            total,
                            total
                        )

                        isCurrent -> pluralStringResource(
                            Res.plurals.habit_stage_days_current,
                            total,
                            passed,
                            total
                        )

                        else -> pluralStringResource(
                            Res.plurals.habit_stage_days_total,
                            total,
                            total
                        )
                    },
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isCurrent) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                    maxLines = 1
                )
            }

            if (isCurrent) {
                Spacer(Modifier.height(4.dp))
                Text(
                    text = stage.description(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun StageRail(
    done: Boolean,
    current: Boolean,
    isLast: Boolean,
    fraction: Float,
    modifier: Modifier = Modifier
) {
    val primary = MaterialTheme.colorScheme.primary

    Box(modifier) {
        if (!isLast) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 20.dp)
                    .fillMaxHeight()
                    .width(2.dp)
                    .background(
                        if (done) {
                            primary.copy(alpha = 0.35f)
                        } else {
                            MaterialTheme.colorScheme.surfaceVariant
                        }
                    )
            )

            if (current) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 20.dp)
                        .fillMaxHeight(fraction)
                        .width(2.dp)
                        .background(primary)
                )
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .size(20.dp)
                .drawBehind {
                    if (current) {
                        val inner = size.minDimension / 2f
                        val outer = inner + 16.dp.toPx()
                        drawCircle(
                            brush = Brush.radialGradient(
                                colorStops = arrayOf(
                                    0f to primary.copy(alpha = 0.5f),
                                    10f / 26f to primary.copy(alpha = 0.32f),
                                    14f / 26f to primary.copy(alpha = 0.19f),
                                    18f / 26f to primary.copy(alpha = 0.09f),
                                    22f / 26f to primary.copy(alpha = 0.03f),
                                    1f to primary.copy(alpha = 0f)
                                ),
                                center = center,
                                radius = outer
                            ),
                            radius = outer,
                            center = center
                        )
                    }
                }
                .clip(CircleShape)
                .background(
                    when {
                        done -> primary.copy(alpha = 0.55f)
                        current -> MaterialTheme.colorScheme.surfaceContainerLow
                        else -> MaterialTheme.colorScheme.surfaceVariant
                    }
                )
                .then(
                    when {
                        current -> Modifier.border(
                            width = 2.dp,
                            color = primary,
                            shape = CircleShape
                        )

                        done -> Modifier
                        else -> Modifier.border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outlineVariant,
                            shape = CircleShape
                        )
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            if (done) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    modifier = Modifier.size(12.dp),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@Preview(widthDp = 360, heightDp = 480, name = "HabitStageCard current")
@Composable
private fun HabitStageCardPreview() {
    PreviewTheme {
        HabitStageCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            progress = habitProgress(34.days + 4.hours),
            duration = 34.days + 4.hours,
            onRecordSlip = {}
        )
    }
}

@Preview(widthDp = 360, heightDp = 480, name = "HabitStageCard hours")
@Composable
private fun HabitStageCardHoursPreview() {
    val duration = 4.hours + 5.minutes + 6.seconds
    PreviewTheme {
        HabitStageCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            progress = habitProgress(duration),
            duration = duration,
            onRecordSlip = {}
        )
    }
}

@Preview(widthDp = 360, heightDp = 480, name = "HabitStageCard seconds")
@Composable
private fun HabitStageCardSecondsPreview() {
    val duration = 6.seconds
    PreviewTheme {
        HabitStageCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            progress = habitProgress(duration),
            duration = duration,
            onRecordSlip = {}
        )
    }
}

@Preview(widthDp = 360, heightDp = 440, name = "HabitStageCard complete")
@Composable
private fun HabitStageCardCompletePreview() {
    PreviewTheme {
        HabitStageCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            progress = habitProgress(120.days),
            duration = 120.days,
            onRecordSlip = {}
        )
    }
}
