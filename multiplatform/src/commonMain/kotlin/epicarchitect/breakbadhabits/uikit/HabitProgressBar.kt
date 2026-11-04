package epicarchitect.breakbadhabits.uikit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import epicarchitect.breakbadhabits.habits.HabitProgress
import epicarchitect.breakbadhabits.habits.HabitStage
import epicarchitect.breakbadhabits.resources.Res
import epicarchitect.breakbadhabits.resources.habit_progress_days
import epicarchitect.breakbadhabits.resources.habit_progress_stage
import org.jetbrains.compose.resources.stringResource

@Composable
fun HabitProgressBar(
    progress: HabitProgress,
    modifier: Modifier = Modifier,
    showStageTitle: Boolean = true,
    showStageNumber: Boolean = true,
    showDays: Boolean = true
) {
    Column(modifier) {
        if (showStageTitle || showDays) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (showStageTitle) {
                    Text(
                        text = if (showStageNumber && progress.isComplete.not()) {
                            stringResource(
                                Res.string.habit_progress_stage,
                                progress.stage.ordinal + 1,
                                progress.stage.title()
                            )
                        } else {
                            progress.title()
                        },
                        style = MaterialTheme.typography.labelMedium
                    )
                }
                if (showDays) {
                    Text(
                        text = stringResource(Res.string.habit_progress_days, progress.days),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Spacer(Modifier.height(4.dp))
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            HabitStage.entries.forEach { stage ->
                val segmentProgress = when {
                    progress.isComplete || stage.ordinal < progress.stage.ordinal -> 1f
                    stage == progress.stage -> progress.stageFraction
                    else -> 0f
                }
                val trackColor = if (stage == progress.stage) {
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                } else {
                    MaterialTheme.colorScheme.surfaceVariant
                }
                Box(
                    modifier = Modifier
                        .weight((stage.endDay - stage.startDay).toFloat())
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(trackColor)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(segmentProgress)
                            .background(MaterialTheme.colorScheme.primary)
                    )
                }
            }
        }
    }
}
