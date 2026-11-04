package epicarchitect.breakbadhabits.screens.habits.widgets.creation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import epicarchitect.breakbadhabits.resources.cancel
import epicarchitect.breakbadhabits.resources.habit_widget_fact_composition_subtitle
import epicarchitect.breakbadhabits.resources.habit_widget_fact_composition_title
import epicarchitect.breakbadhabits.resources.habit_widget_fact_stage_subtitle
import epicarchitect.breakbadhabits.resources.habit_widget_fact_stage_title
import epicarchitect.breakbadhabits.resources.habit_widget_fact_time_subtitle
import epicarchitect.breakbadhabits.resources.habit_widget_fact_time_title
import epicarchitect.breakbadhabits.resources.habit_widget_facts_title
import epicarchitect.breakbadhabits.resources.habit_widget_premium_open
import epicarchitect.breakbadhabits.resources.habit_widget_preview_habit_smoking
import epicarchitect.breakbadhabits.resources.habit_widget_preview_habit_sweets
import epicarchitect.breakbadhabits.resources.habit_widget_preview_label
import epicarchitect.breakbadhabits.resources.habit_widget_screen_title
import epicarchitect.breakbadhabits.uikit.title
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours

private val PreviewSmokingDuration = 34.days + 4.hours
private val PreviewSweetsDuration = 18.days + 7.hours

@Composable
fun WidgetPremiumScreen(
    onBack: () -> Unit,
    onOpenPremium: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = stringResource(Res.string.habit_widget_screen_title),
                    style = MaterialTheme.typography.titleMedium
                )
            },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = stringResource(Res.string.cancel)
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background,
                scrolledContainerColor = Color.Unspecified,
                navigationIconContentColor = Color.Unspecified,
                titleContentColor = Color.Unspecified,
                actionIconContentColor = Color.Unspecified
            ),
            windowInsets = WindowInsets(0)
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 22.dp)
        ) {
            Spacer(Modifier.height(10.dp))
            Text(
                text = stringResource(Res.string.habit_widget_preview_label).uppercase(),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp
            )
            Spacer(Modifier.height(6.dp))
            WidgetPreviewCard()
            Spacer(Modifier.height(18.dp))
            WidgetFacts()
            Spacer(Modifier.height(20.dp))
        }

        PremiumCallToAction(onClick = onOpenPremium)
    }
}

@Composable
private fun WidgetPreviewCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(start = 4.dp, top = 10.dp, end = 4.dp)
        ) {
            WidgetHabitPreview(
                name = stringResource(Res.string.habit_widget_preview_habit_smoking),
                duration = PreviewSmokingDuration
            )
        }
        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
            thickness = 1.dp
        )
        Column(
            modifier = Modifier.padding(start = 4.dp, end = 4.dp, bottom = 10.dp)
        ) {
            WidgetHabitPreview(
                name = stringResource(Res.string.habit_widget_preview_habit_sweets),
                duration = PreviewSweetsDuration
            )
        }
    }
}

@Composable
private fun WidgetHabitPreview(
    name: String,
    duration: Duration
) {
    val progress = habitProgress(duration)

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
            WidgetTimerText(duration = duration)
        }
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = progress.title(),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
            fontSize = 11.sp,
            lineHeight = 13.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(Modifier.height(4.dp))
        WidgetProgressSegments(progress = progress)
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

@Composable
private fun WidgetFacts() {
    val dividerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(Res.string.habit_widget_facts_title).uppercase(),
            color = MaterialTheme.colorScheme.primary,
            fontSize = 10.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 1.5.sp
        )
        Spacer(Modifier.width(9.dp))
        Box(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.35f),
                            Color.Transparent
                        )
                    )
                )
        )
    }

    WidgetFact(
        number = "1",
        title = stringResource(Res.string.habit_widget_fact_time_title),
        subtitle = stringResource(Res.string.habit_widget_fact_time_subtitle)
    )
    HorizontalDivider(
        color = dividerColor,
        thickness = 1.dp
    )
    WidgetFact(
        number = "2",
        title = stringResource(Res.string.habit_widget_fact_stage_title),
        subtitle = stringResource(Res.string.habit_widget_fact_stage_subtitle)
    )
    HorizontalDivider(
        color = dividerColor,
        thickness = 1.dp
    )
    WidgetFact(
        number = "3",
        title = stringResource(Res.string.habit_widget_fact_composition_title),
        subtitle = stringResource(Res.string.habit_widget_fact_composition_subtitle)
    )
    HorizontalDivider(
        color = dividerColor,
        thickness = 1.dp
    )
}

@Composable
private fun WidgetFact(
    number: String,
    title: String,
    subtitle: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 11.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            modifier = Modifier.width(20.dp),
            text = number,
            color = MaterialTheme.colorScheme.primary,
            fontSize = 10.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 1.sp
        )
        Column(Modifier.weight(1f)) {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.2).sp
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = subtitle,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f),
                fontSize = 10.5.sp
            )
        }
    }
}

@Composable
private fun PremiumCallToAction(onClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.background
    ) {
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 22.dp, end = 22.dp, top = 12.dp, bottom = 14.dp)
                .height(55.dp),
            onClick = onClick,
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text(
                text = stringResource(Res.string.habit_widget_premium_open),
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}
