package epicarchitect.breakbadhabits.screens.habits.widgets.creation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import epicarchitect.breakbadhabits.format.Accuracy
import epicarchitect.breakbadhabits.format.formatted
import epicarchitect.breakbadhabits.habits.HabitProgress
import epicarchitect.breakbadhabits.habits.HabitStage
import epicarchitect.breakbadhabits.habits.habitProgress
import epicarchitect.breakbadhabits.resources.Res
import epicarchitect.breakbadhabits.resources.habit_has_no_events
import epicarchitect.breakbadhabits.uikit.title
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Duration
import androidx.compose.material3.Surface
import androidx.compose.ui.tooling.preview.Preview
import epicarchitect.breakbadhabits.uikit.PreviewTheme
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours

@Composable
internal fun WidgetHabitPreview(
    name: String,
    duration: Duration?
) {
    val progress = duration?.let(::habitProgress)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, top = 6.dp, end = 10.dp, bottom = 6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = name,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 14.sp,
                lineHeight = 16.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.width(10.dp))
            if (duration != null) {
                WidgetTimerText(duration = duration)
            }
        }
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = progress?.title() ?: stringResource(Res.string.habit_has_no_events),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
            fontSize = 11.sp,
            lineHeight = 13.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        if (progress != null) {
            Spacer(Modifier.height(4.dp))
            WidgetProgressSegments(progress = progress)
        }
    }
}

@Composable
private fun WidgetTimerText(duration: Duration) {
    val formatted = duration.formatted(Accuracy.HOURS)
    val firstSpace = formatted.indexOf(' ')
    val hasDays = duration.inWholeDays > 0 && firstSpace > 0
    val text = buildAnnotatedString {
        if (hasDays) {
            withStyle(
                SpanStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            ) {
                append(formatted.take(firstSpace))
            }
            withStyle(
                SpanStyle(
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )
            ) {
                append(formatted.substring(firstSpace))
            }
        } else {
            withStyle(
                SpanStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            ) {
                append(formatted)
            }
        }
    }
    Text(
        text = text,
        lineHeight = 20.sp,
        maxLines = 1
    )
}

@Composable
private fun WidgetProgressSegments(progress: HabitProgress) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(4.dp)
    ) {
        HabitStage.entries.forEach { stage ->
            val segmentProgress = when {
                progress.isComplete || stage.ordinal < progress.stage.ordinal -> 1f
                stage == progress.stage -> progress.stageFraction
                else -> 0f
            }
            val trackColor = if (stage == progress.stage && progress.isComplete.not()) {
                MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
            } else {
                MaterialTheme.colorScheme.outlineVariant
            }
            Box(
                modifier = Modifier
                    .weight((stage.endDay - stage.startDay).toFloat())
                    .fillMaxHeight()
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
            Spacer(Modifier.width(3.dp))
        }
    }
}

@Preview(widthDp = 300, heightDp = 90, name = "WidgetHabitPreview")
@Composable
private fun WidgetHabitPreviewPreview() {
    PreviewTheme {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            WidgetHabitPreview(
                name = "Курение",
                duration = 34.days + 4.hours
            )
        }
    }
}
