package epicarchitect.breakbadhabits.screens.habits.details

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import epicarchitect.breakbadhabits.format.Accuracy
import epicarchitect.breakbadhabits.format.formatted
import epicarchitect.breakbadhabits.resources.Res
import epicarchitect.breakbadhabits.resources.abstinence_chart_attempts
import epicarchitect.breakbadhabits.resources.abstinence_chart_average
import epicarchitect.breakbadhabits.resources.abstinence_chart_now
import epicarchitect.breakbadhabits.resources.abstinence_chart_record
import epicarchitect.breakbadhabits.resources.abstinence_chart_record_reached
import epicarchitect.breakbadhabits.resources.abstinence_chart_title
import epicarchitect.breakbadhabits.resources.abstinence_chart_until_record
import epicarchitect.breakbadhabits.uikit.PreviewTheme
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Duration.Companion.seconds

private val HistogramBarShape = RoundedCornerShape(
    topStart = 7.dp,
    topEnd = 7.dp,
    bottomStart = 3.dp,
    bottomEnd = 3.dp
)
private val HistogramBarWidth = 40.dp
private val HistogramBarGap = 8.dp
private val HistogramBarMaxHeight = 140.dp
private val HistogramChartHeight = 170.dp

@Composable
fun HabitDetailsHistogramCard(
    modifier: Modifier = Modifier,
    state: HabitDetailsScreenState
) {
    val values = state.abstinenceHistogramValues

    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        if (values.isNotEmpty()) {
            val maxValue = values.maxOrNull() ?: 0f
            val recordValue = maxValue
            val currentValue = values.last()
            val currentIsRecord = currentValue >= recordValue
            val recordIndex = if (currentIsRecord) {
                values.lastIndex
            } else {
                values.indexOf(recordValue)
            }
            val recordText = recordValue.toInt().seconds.formatted(Accuracy.DAYS)
            val averageText = values.average().toInt().seconds.formatted(Accuracy.DAYS)
            val insightText = if (currentIsRecord) {
                stringResource(Res.string.abstinence_chart_record_reached)
            } else {
                stringResource(
                    Res.string.abstinence_chart_until_record,
                    (recordValue - currentValue).toInt().seconds.formatted(Accuracy.HOURS)
                )
            }

            Column(
                modifier = Modifier.padding(vertical = 18.dp)
            ) {
                Column(Modifier.padding(horizontal = 18.dp)) {
                    Text(
                        text = stringResource(Res.string.abstinence_chart_title),
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(Modifier.height(4.dp))

                    HistogramInsight(text = insightText)
                }

                Spacer(Modifier.height(10.dp))

                HistogramChart(
                    modifier = Modifier.fillMaxWidth(),
                    values = values,
                    maxValue = maxValue,
                    recordIndex = recordIndex
                )

                Spacer(Modifier.height(14.dp))

                HistogramSummary(
                    modifier = Modifier.padding(horizontal = 18.dp),
                    recordText = recordText,
                    averageText = averageText,
                    attemptsCount = values.size
                )
            }
        }
    }
}

@Composable
private fun HistogramInsight(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(5.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
        )
        Spacer(Modifier.width(6.dp))
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            fontSize = 10.sp,
            lineHeight = 13.sp
        )
    }
}

@Composable
private fun HistogramChart(
    modifier: Modifier = Modifier,
    values: List<Float>,
    maxValue: Float,
    recordIndex: Int
) {
    val listState = rememberLazyListState()
    val cardColor = MaterialTheme.colorScheme.surfaceContainerLow
    val normalizedMaxValue = maxValue.coerceAtLeast(1f)
    val showBarLabels = LocalDensity.current.fontScale < 1.3f

    Box(
        modifier = modifier.height(HistogramChartHeight)
    ) {
        LazyRow(
            modifier = Modifier.fillMaxSize(),
            state = listState,
            reverseLayout = true,
            contentPadding = PaddingValues(horizontal = 18.dp),
            horizontalArrangement = Arrangement.spacedBy(HistogramBarGap, Alignment.End),
            verticalAlignment = Alignment.Bottom
        ) {
            items(count = values.size) { index ->
                val originalIndex = values.lastIndex - index
                val value = values[originalIndex]

                HistogramBar(
                    value = value,
                    maxValue = normalizedMaxValue,
                    isCurrent = originalIndex == values.lastIndex,
                    isRecord = originalIndex == recordIndex,
                    showLabel = showBarLabels
                )
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .fillMaxHeight()
                .width(18.dp)
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            cardColor,
                            cardColor.copy(alpha = 0.3f),
                            Color.Transparent
                        )
                    )
                )
        )

        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight()
                .width(18.dp)
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            Color.Transparent,
                            cardColor.copy(alpha = 0.3f),
                            cardColor
                        )
                    )
                )
        )
    }
}

@Composable
private fun HistogramBar(
    value: Float,
    maxValue: Float,
    isCurrent: Boolean,
    isRecord: Boolean,
    showLabel: Boolean
) {
    val colorScheme = MaterialTheme.colorScheme
    val ratio = (value / maxValue).coerceIn(0f, 1f)
    val barHeight = maxOf(2.dp, HistogramBarMaxHeight * ratio)
    val label = value.toInt().seconds.formatted(Accuracy.HOURS)
    val labelColor = if (isCurrent || isRecord) {
        colorScheme.primary
    } else {
        colorScheme.onSurface.copy(alpha = 0.55f)
    }
    val barBrush = when {
        isCurrent -> Brush.verticalGradient(
            listOf(colorScheme.primary.copy(alpha = 0.85f), colorScheme.primary)
        )

        isRecord -> Brush.verticalGradient(
            listOf(
                colorScheme.primary.copy(alpha = 0.38f),
                colorScheme.primary.copy(alpha = 0.1f)
            )
        )

        else -> Brush.verticalGradient(
            listOf(
                colorScheme.onSurface.copy(alpha = 0.32f),
                colorScheme.onSurface.copy(alpha = 0.12f)
            )
        )
    }

    Box(
        modifier = Modifier
            .width(HistogramBarWidth)
            .fillMaxHeight()
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .width(HistogramBarWidth)
                .height(barHeight)
                .clip(HistogramBarShape)
                .background(barBrush)
                .then(
                    if (isRecord) {
                        Modifier.border(
                            width = 1.dp,
                            color = colorScheme.primary.copy(alpha = 0.25f),
                            shape = HistogramBarShape
                        )
                    } else {
                        Modifier
                    }
                )
        )

        if (showLabel) {
            Text(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = -(barHeight + 4.dp)),
                text = if (isRecord) "$label ★" else label,
                color = labelColor,
                fontSize = 9.sp,
                fontWeight = if (isCurrent || isRecord) FontWeight.Medium else FontWeight.Normal,
                maxLines = 1,
                softWrap = false,
                overflow = TextOverflow.Visible,
                textAlign = TextAlign.Center
            )
        }

        if (isCurrent && showLabel) {
            Text(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = -(barHeight + 18.dp)),
                text = stringResource(Res.string.abstinence_chart_now),
                color = colorScheme.primary,
                fontSize = 8.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                softWrap = false,
                overflow = TextOverflow.Visible,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun HistogramSummary(
    modifier: Modifier = Modifier,
    recordText: String,
    averageText: String,
    attemptsCount: Int
) {
    val colorScheme = MaterialTheme.colorScheme

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .background(colorScheme.onSurface.copy(alpha = 0.04f))
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HistogramSummaryItem(
            modifier = Modifier.weight(1f),
            label = stringResource(Res.string.abstinence_chart_record),
            value = recordText,
            highlighted = true
        )

        HistogramSummaryDivider()

        HistogramSummaryItem(
            modifier = Modifier.weight(1f),
            label = stringResource(Res.string.abstinence_chart_average),
            value = averageText
        )

        HistogramSummaryDivider()

        HistogramSummaryItem(
            modifier = Modifier.weight(1f),
            label = stringResource(Res.string.abstinence_chart_attempts),
            value = attemptsCount.toString()
        )
    }
}

@Composable
private fun HistogramSummaryItem(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    highlighted: Boolean = false
) {
    val colorScheme = MaterialTheme.colorScheme

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (highlighted) {
                Text(
                    text = "★",
                    color = colorScheme.primary,
                    fontSize = 8.sp,
                    lineHeight = 8.sp
                )
                Spacer(Modifier.width(3.dp))
            }

            Text(
                text = label.uppercase(),
                color = colorScheme.onSurface.copy(alpha = 0.55f),
                fontSize = 8.5.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.6.sp,
                lineHeight = 10.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(Modifier.height(3.dp))

        Text(
            text = value,
            color = colorScheme.onSurface,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            lineHeight = 16.sp,
            maxLines = 1
        )
    }
}

@Composable
private fun HistogramSummaryDivider() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(24.dp)
            .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
    )
}

@Preview(widthDp = 360, heightDp = 430, name = "HabitDetailsHistogramCard")
@Composable
private fun HabitDetailsHistogramCardPreview() {
    PreviewTheme {
        HabitDetailsHistogramCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            state = previewHabitState()
        )
    }
}
