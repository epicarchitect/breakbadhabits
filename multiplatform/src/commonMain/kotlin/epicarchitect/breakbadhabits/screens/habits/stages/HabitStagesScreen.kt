package epicarchitect.breakbadhabits.screens.habits.stages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import epicarchitect.breakbadhabits.habits.HabitProgress
import epicarchitect.breakbadhabits.habits.habitProgress
import epicarchitect.breakbadhabits.habits.HabitStage
import epicarchitect.breakbadhabits.resources.Res
import epicarchitect.breakbadhabits.resources.habit_stage_advice_title
import epicarchitect.breakbadhabits.resources.habit_stage_after_90
import epicarchitect.breakbadhabits.resources.habit_stage_current
import epicarchitect.breakbadhabits.resources.habit_stage_days
import epicarchitect.breakbadhabits.resources.habit_stage_freedom
import epicarchitect.breakbadhabits.resources.habit_stage_freedom_inspiring
import epicarchitect.breakbadhabits.resources.habit_stages_title
import epicarchitect.breakbadhabits.screens.root.LocalNavBackStack
import epicarchitect.breakbadhabits.uikit.AppBackButton
import epicarchitect.breakbadhabits.uikit.AppScaffold
import epicarchitect.breakbadhabits.uikit.rememberEnterAlwaysTopBarScrollBehavior
import epicarchitect.breakbadhabits.uikit.PreviewTheme
import epicarchitect.breakbadhabits.uikit.HabitProgressBar
import epicarchitect.breakbadhabits.uikit.advice
import epicarchitect.breakbadhabits.uikit.description
import epicarchitect.breakbadhabits.uikit.title
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.uuid.Uuid

@Composable
fun HabitStagesScreen(habitId: Uuid) {
    val state = rememberHabitStagesScreenState(habitId) ?: return
    val navBackStack = LocalNavBackStack.current
    val navigationBarBottom = WindowInsets.navigationBars
        .asPaddingValues()
        .calculateBottomPadding()
    val listState = rememberLazyListState()
    val topBarScrollBehavior = rememberEnterAlwaysTopBarScrollBehavior(listState)

    AppScaffold(
        title = stringResource(Res.string.habit_stages_title),
        topBarScrollBehavior = topBarScrollBehavior,
        navigationIcon = {
            AppBackButton(onClick = { navBackStack.removeLastOrNull() })
        }
    ) { topBarHeight ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                top = topBarHeight,
                bottom = navigationBarBottom
            ),
            state = listState
        ) {
            item(key = "content") {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = state.habit.name,
                        style = MaterialTheme.typography.titleLarge
                    )
                    state.progress?.let { progress ->
                        Spacer(Modifier.height(12.dp))
                        HabitProgressBar(
                            progress = progress,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    Spacer(Modifier.height(24.dp))
                    HabitStagesTimeline(progress = state.progress)
                    Spacer(Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
private fun HabitStagesTimeline(progress: HabitProgress?) {
    Column {
        HabitStage.entries.forEach { stage ->
            val completed = progress != null &&
                    (progress.isComplete || stage.ordinal < progress.stage.ordinal)
            val isCurrent = progress?.stage == stage
            HabitStageTimelineItem(
                stage = stage,
                completed = completed,
                isCurrent = isCurrent,
                upcoming = progress != null && !completed && !isCurrent
            )
        }
        HabitStagesContinuation(completed = progress?.isComplete == true)
    }
}

@Composable
private fun HabitStageTimelineItem(
    stage: HabitStage,
    completed: Boolean,
    isCurrent: Boolean,
    upcoming: Boolean
) {
    val accent = MaterialTheme.colorScheme.primary
    val contentColor = if (upcoming) {
        MaterialTheme.colorScheme.onSurfaceVariant
    } else {
        MaterialTheme.colorScheme.onSurface
    }
    val titleColor = if (isCurrent) accent else contentColor
    val nodeColor = when {
        completed || isCurrent -> accent
        else -> MaterialTheme.colorScheme.surfaceVariant
    }
    val nodeContentColor = when {
        completed || isCurrent -> MaterialTheme.colorScheme.onPrimary
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }
    val lineColor = when {
        completed -> accent.copy(alpha = 0.5f)
        else -> MaterialTheme.colorScheme.surfaceVariant
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        Column(
            modifier = Modifier
                .width(40.dp)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(nodeColor),
                contentAlignment = Alignment.Center
            ) {
                if (completed) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = null,
                        tint = nodeContentColor,
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Text(
                        text = (stage.ordinal + 1).toString(),
                        style = MaterialTheme.typography.titleSmall,
                        color = nodeContentColor
                    )
                }
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .width(2.dp)
                    .background(lineColor)
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp, bottom = 24.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(
                        Res.string.habit_stage_days,
                        stage.firstDay,
                        stage.lastDay
                    ),
                    style = MaterialTheme.typography.labelMedium,
                    color = titleColor
                )
                if (isCurrent) {
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = stringResource(Res.string.habit_stage_current),
                        style = MaterialTheme.typography.labelSmall,
                        color = accent
                    )
                }
            }
            Spacer(Modifier.height(4.dp))
            Text(
                text = stage.title(),
                style = MaterialTheme.typography.titleMedium,
                color = titleColor
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = stage.description(),
                style = MaterialTheme.typography.bodyMedium,
                color = contentColor
            )
            if (isCurrent) {
                Spacer(Modifier.height(10.dp))
                Text(
                    text = stringResource(Res.string.habit_stage_advice_title),
                    style = MaterialTheme.typography.titleSmall,
                    color = contentColor
                )
                Spacer(Modifier.height(4.dp))
                stage.advice().forEach { advice ->
                    Text(
                        text = "• $advice",
                        modifier = Modifier.padding(bottom = 4.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = contentColor
                    )
                }
            }
        }
    }
}


@Composable
private fun HabitStagesContinuation(completed: Boolean) {
    val accent = MaterialTheme.colorScheme.primary
    val lineColor = if (completed) {
        accent.copy(alpha = 0.5f)
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }
    val contentColor = if (completed) {
        MaterialTheme.colorScheme.onSurface
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }
    val titleColor = if (completed) accent else contentColor

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        Box(
            modifier = Modifier
                .width(40.dp)
                .fillMaxHeight(),
            contentAlignment = Alignment.TopCenter
        ) {
            Box(
                modifier = Modifier
                    .width(2.dp)
                    .fillMaxHeight()
                    .background(
                        Brush.verticalGradient(
                            listOf(lineColor, lineColor.copy(alpha = 0f))
                        )
                    )
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp)
        ) {
            Text(
                text = stringResource(Res.string.habit_stage_after_90),
                style = MaterialTheme.typography.labelMedium,
                color = contentColor
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = stringResource(Res.string.habit_stage_freedom),
                style = MaterialTheme.typography.titleMedium,
                color = titleColor
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = stringResource(Res.string.habit_stage_freedom_inspiring),
                style = MaterialTheme.typography.bodyMedium,
                color = contentColor
            )
        }
    }
}


@Preview(widthDp = 360, heightDp = 760, name = "HabitStagesTimeline")
@Composable
private fun HabitStagesTimelinePreview() {
    PreviewTheme {
        HabitStagesTimeline(
            progress = habitProgress(34.days + 4.hours)
        )
    }
}

@Preview(widthDp = 360, heightDp = 260, name = "HabitStageTimelineItem")
@Composable
private fun HabitStageTimelineItemPreview() {
    PreviewTheme {
        Column(Modifier.padding(16.dp)) {
            HabitStageTimelineItem(
                stage = HabitStage.REBUILDING,
                completed = false,
                isCurrent = true,
                upcoming = false
            )
            HabitStageTimelineItem(
                stage = HabitStage.ACUTE,
                completed = true,
                isCurrent = false,
                upcoming = false
            )
        }
    }
}

@Preview(widthDp = 360, heightDp = 260, name = "HabitStagesContinuation")
@Composable
private fun HabitStagesContinuationPreview() {
    PreviewTheme {
        HabitStagesContinuation(completed = true)
    }
}
