package epicarchitect.breakbadhabits.screens.habits.widgets.creation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
import epicarchitect.breakbadhabits.uikit.AdaptiveContent
import epicarchitect.breakbadhabits.uikit.AppTopBar
import epicarchitect.breakbadhabits.uikit.PreviewTheme
import epicarchitect.breakbadhabits.uikit.PrimaryButton
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours

private val PreviewSmokingDuration = 34.days + 4.hours
private val PreviewSweetsDuration = 18.days + 7.hours

@Composable
fun WidgetPremiumScreen(
    onBack: () -> Unit,
    onOpenPremium: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
        ) {
            AppTopBar(
                title = stringResource(Res.string.habit_widget_screen_title),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = stringResource(Res.string.cancel)
                        )
                    }
                }
            )

            AdaptiveContent {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Spacer(Modifier.height(10.dp))
                    Text(
                        text = stringResource(Res.string.habit_widget_preview_label).uppercase(),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.labelMedium
                    )
                    Spacer(Modifier.height(6.dp))
                    WidgetPreviewCard()
                    Spacer(Modifier.height(18.dp))
                    WidgetFacts()
                    Spacer(Modifier.height(20.dp))
                }
            }
        }

        PremiumCallToAction(onClick = onOpenPremium)
    }
}

@Composable
private fun WidgetPreviewCard() {
    Card(
        modifier = Modifier.fillMaxWidth()
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
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.55f),
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
private fun WidgetFacts() {
    val dividerColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.55f)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(Res.string.habit_widget_facts_title).uppercase(),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.labelMedium
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
            style = MaterialTheme.typography.labelSmall
        )
        Column(Modifier.weight(1f)) {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleSmall
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = subtitle,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun PremiumCallToAction(onClick: () -> Unit) {
    PrimaryButton(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 16.dp,
                end = 16.dp,
                top = 12.dp,
                bottom = 14.dp
            ),
        text = stringResource(Res.string.habit_widget_premium_open),
        onClick = onClick
    )
}

@Preview(widthDp = 360, heightDp = 800, name = "WidgetPremiumScreen")
@Composable
private fun WidgetPremiumScreenPreview() {
    PreviewTheme {
        WidgetPremiumScreen(
            onBack = {},
            onOpenPremium = {}
        )
    }
}
