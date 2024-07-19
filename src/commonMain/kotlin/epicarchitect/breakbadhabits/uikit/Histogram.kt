package epicarchitect.breakbadhabits.uikit

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import epicarchitect.breakbadhabits.uikit.theme.AppTheme

@Composable
fun Histogram(
    modifier: Modifier = Modifier,
    values: List<Float>,
    valueFormatter: (Float) -> CharSequence,
    barPadding: Dp = 12.dp,
    startPadding: Dp = 12.dp,
    endPadding: Dp = 12.dp,
    barColor: Color = AppTheme.colorScheme.onSurface,
    maxVisibleBarCount: Int = 5,
    valueTextSize: TextUnit = 10.sp,
    valueTextPadding: Dp = 8.dp,
    valueTextColor: Color = AppTheme.colorScheme.onSurface
) {
    var boxSize by remember { mutableStateOf(IntSize.Zero) }
    val barPaddingPx = with(LocalDensity.current) { barPadding.toPx() }
    val startPaddingPx = with(LocalDensity.current) { startPadding.toPx() }
    val endPaddingPx = with(LocalDensity.current) { endPadding.toPx() }
    val valueTextSizePx = with(LocalDensity.current) { valueTextSize.toPx() }
    val valueTextPaddingPx = with(LocalDensity.current) { valueTextPadding.toPx() }
    val textMeasurer = rememberTextMeasurer()

    Box(
        modifier = modifier.onSizeChanged {
            boxSize = it
        }
    ) {
        val visibleBarCount = values.size.let {
            if (it > maxVisibleBarCount) maxVisibleBarCount else it
        }

        val barWidthPx = if (boxSize.width > 0) {
            (boxSize.width - startPaddingPx - endPaddingPx) / visibleBarCount - (barPaddingPx - barPaddingPx / visibleBarCount)
        } else {
            0f // here problem
        }

        val canvasWidth = with(LocalDensity.current) {
            (barWidthPx * values.size + (values.size - 1) * barPaddingPx + startPaddingPx + endPaddingPx).toDp()
//            val paddings = barPaddingPx + startPaddingPx + endPaddingPx
//            (barWidthPx * values.size + (values.size - 1) * paddings).toDp()
        }

        Canvas(
            modifier = Modifier
                .fillMaxHeight()
                .horizontalScroll(
                    state = rememberScrollState(),
                    reverseScrolling = true
                )
                .width(canvasWidth)
        ) {
            inset(
                left = startPaddingPx,
                right = endPaddingPx,
                top = 0f,
                bottom = 0f
            ) {
                val maxY = values.max()
                val minBarOffsetY = size.height.let { height ->
                    height - (height - valueTextPaddingPx * 2 - valueTextSizePx - barPaddingPx)
                }
                val maxBarOffsetY = size.height - size.height * 0.02f

                values.forEachIndexed { index, barValue ->
                    val barOffset = Offset(
                        x = barWidthPx * index + barPaddingPx * index,
                        y = (size.height - barValue / maxY * size.height).let {
                            if (it < minBarOffsetY) minBarOffsetY else it
                        }.let {
                            if (it > maxBarOffsetY) maxBarOffsetY else it
                        }
                    )

                    val barHeightPx = size.height - barOffset.y

                    drawText(
                        textMeasurer = textMeasurer,
                        text = valueFormatter(barValue).toString(),
                        topLeft = Offset(
                            barOffset.x + barWidthPx / 2 - valueTextSizePx / 2,
                            barOffset.y - valueTextPaddingPx - valueTextSizePx,
                        ),
                        style = TextStyle.Default.copy(
                            color = valueTextColor,
                            fontSize = valueTextSize
                        )
                    )

                    drawRect(
                        color = barColor,
                        topLeft = barOffset,
                        size = Size(barWidthPx, barHeightPx)
                    )
                }
            }
        }
    }
}