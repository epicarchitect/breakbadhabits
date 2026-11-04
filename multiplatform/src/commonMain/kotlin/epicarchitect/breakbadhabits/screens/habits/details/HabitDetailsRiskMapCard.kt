package epicarchitect.breakbadhabits.screens.habits.details

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import epicarchitect.breakbadhabits.resources.Res
import epicarchitect.breakbadhabits.resources.risk_map_day_fri
import epicarchitect.breakbadhabits.resources.risk_map_day_mon
import epicarchitect.breakbadhabits.resources.risk_map_day_sat
import epicarchitect.breakbadhabits.resources.risk_map_day_sun
import epicarchitect.breakbadhabits.resources.risk_map_day_thu
import epicarchitect.breakbadhabits.resources.risk_map_day_tue
import epicarchitect.breakbadhabits.resources.risk_map_day_wed
import epicarchitect.breakbadhabits.resources.risk_map_description
import epicarchitect.breakbadhabits.resources.risk_map_open
import epicarchitect.breakbadhabits.resources.risk_map_period_night
import epicarchitect.breakbadhabits.resources.risk_map_period_day
import epicarchitect.breakbadhabits.resources.risk_map_period_day_range
import epicarchitect.breakbadhabits.resources.risk_map_period_evening
import epicarchitect.breakbadhabits.resources.risk_map_period_evening_range
import epicarchitect.breakbadhabits.resources.risk_map_period_morning
import epicarchitect.breakbadhabits.resources.risk_map_period_morning_range
import epicarchitect.breakbadhabits.resources.risk_map_period_night_range
import epicarchitect.breakbadhabits.resources.risk_map_summary_and
import epicarchitect.breakbadhabits.resources.risk_map_summary_day_fri
import epicarchitect.breakbadhabits.resources.risk_map_summary_day_mon
import epicarchitect.breakbadhabits.resources.risk_map_summary_day_sat
import epicarchitect.breakbadhabits.resources.risk_map_summary_day_sun
import epicarchitect.breakbadhabits.resources.risk_map_summary_day_thu
import epicarchitect.breakbadhabits.resources.risk_map_summary_day_tue
import epicarchitect.breakbadhabits.resources.risk_map_summary_day_wed
import epicarchitect.breakbadhabits.resources.risk_map_summary_period_day
import epicarchitect.breakbadhabits.resources.risk_map_summary_period_evening
import epicarchitect.breakbadhabits.resources.risk_map_summary_period_morning
import epicarchitect.breakbadhabits.resources.risk_map_summary_period_night
import epicarchitect.breakbadhabits.resources.risk_map_summary_prefix
import epicarchitect.breakbadhabits.resources.risk_map_summary_zone_pattern
import epicarchitect.breakbadhabits.resources.risk_map_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun HabitDetailsRiskMapCard(
    modifier: Modifier = Modifier,
    riskMap: HabitRiskMapData,
    premium: Boolean,
    onOpenPremium: () -> Unit
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(
            containerColor = if (premium) {
                MaterialTheme.colorScheme.surfaceContainerLow
            } else {
                MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
            }
        ),
        border = if (premium) {
            null
        } else {
            BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = stringResource(Res.string.risk_map_title),
                    style = MaterialTheme.typography.titleMedium
                )
                if (!premium) {
                    Icon(
                        modifier = Modifier.size(18.dp),
                        imageVector = Icons.Filled.Lock,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            if (premium) {
                Spacer(Modifier.height(16.dp))
                RiskHeatmap(riskMap)
                Spacer(Modifier.height(12.dp))
                Text(
                    text = riskMapSummary(riskMap),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 18.sp
                )
            } else {
                Spacer(Modifier.height(16.dp))
                Text(
                    text = stringResource(Res.string.risk_map_description),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 22.sp
                )
                Spacer(Modifier.height(16.dp))
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    onClick = onOpenPremium,
                    shape = RoundedCornerShape(15.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(
                        text = stringResource(Res.string.risk_map_open),
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }
        }
    }
}

@Composable
private fun riskMapSummary(riskMap: HabitRiskMapData): AnnotatedString {
    val labels = RiskMapSummaryLabels(
        prefix = stringResource(Res.string.risk_map_summary_prefix),
        zonePattern = stringResource(Res.string.risk_map_summary_zone_pattern),
        conjunction = stringResource(Res.string.risk_map_summary_and),
        dayNames = listOf(
            stringResource(Res.string.risk_map_summary_day_mon),
            stringResource(Res.string.risk_map_summary_day_tue),
            stringResource(Res.string.risk_map_summary_day_wed),
            stringResource(Res.string.risk_map_summary_day_thu),
            stringResource(Res.string.risk_map_summary_day_fri),
            stringResource(Res.string.risk_map_summary_day_sat),
            stringResource(Res.string.risk_map_summary_day_sun)
        ),
        periodNames = listOf(
            stringResource(Res.string.risk_map_summary_period_morning),
            stringResource(Res.string.risk_map_summary_period_day),
            stringResource(Res.string.risk_map_summary_period_evening),
            stringResource(Res.string.risk_map_summary_period_night)
        )
    )
    val text = buildRiskMapSummary(riskMap, labels)
    val highlightStyle = SpanStyle(
        color = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.Bold
    )
    return buildAnnotatedString {
        append(text)
        riskMapSummaryHighlightRanges(text, labels).forEach { range ->
            addStyle(highlightStyle, range.first, range.last + 1)
        }
    }
}

@Composable
private fun RiskHeatmap(riskMap: HabitRiskMapData) {
    val days = listOf(
        Res.string.risk_map_day_mon,
        Res.string.risk_map_day_tue,
        Res.string.risk_map_day_wed,
        Res.string.risk_map_day_thu,
        Res.string.risk_map_day_fri,
        Res.string.risk_map_day_sat,
        Res.string.risk_map_day_sun
    )
    val times = listOf(
        Res.string.risk_map_period_morning to Res.string.risk_map_period_morning_range,
        Res.string.risk_map_period_day to Res.string.risk_map_period_day_range,
        Res.string.risk_map_period_evening to Res.string.risk_map_period_evening_range,
        Res.string.risk_map_period_night to Res.string.risk_map_period_night_range
    )

    Column(Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Spacer(Modifier.width(52.dp))
            days.forEach { day ->
                Text(
                    modifier = Modifier.weight(1f),
                    text = stringResource(day),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        }
        Spacer(Modifier.height(4.dp))
        times.forEachIndexed { timeBucket, time ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(34.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.width(52.dp)) {
                    Text(
                        text = stringResource(time.first),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = stringResource(time.second),
                        fontSize = 8.sp,
                        lineHeight = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.65f)
                    )
                }
                repeat(7) { day ->
                    val cellIndex = timeBucket * 7 + day
                    val count = riskMap.highlightedCells[cellIndex]
                    val color = if (count == null) {
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.18f)
                    } else if (count >= riskMap.maxCount) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.primary.copy(
                            alpha = 0.24f + 0.5f * count.toFloat() / riskMap.maxCount
                        )
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(4.dp))
                            .background(color)
                    )
                }
            }
            Spacer(Modifier.height(4.dp))
        }
    }
}
