package breakbadhabits.android.compose.ui

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.MaterialTheme
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
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class HistogramState(
    val items: List<Item>
) {
    data class Item(
        val id: Int,
        val value: Float,
        val formattedValue: String
    )
}

@Composable
fun Histogram(
    modifier: Modifier = Modifier,
    state: HistogramState,
    barPadding: Dp = 12.dp,
    startPadding: Dp = 12.dp,
    endPadding: Dp = 12.dp,
    barColor: Color = MaterialTheme.colors.onSurface,
    maxVisibleBarCount: Int = 5,
    valueTextSize: TextUnit = 10.sp,
    valueTextPadding: Dp = 8.dp,
    valueTextColor: Color = MaterialTheme.colors.onSurface
) {
    var boxSize by remember { mutableStateOf(IntSize.Zero) }
    val barPaddingPx = with(LocalDensity.current) { barPadding.toPx() }
    val startPaddingPx = with(LocalDensity.current) { startPadding.toPx() }
    val endPaddingPx = with(LocalDensity.current) { endPadding.toPx() }
    val valueTextSizePx = with(LocalDensity.current) { valueTextSize.toPx() }
    val valueTextPaddingPx = with(LocalDensity.current) { valueTextPadding.toPx() }

    Box(
        modifier = modifier.onSizeChanged {
            boxSize = it
        }
    ) {
        val visibleBarCount = state.items.size.let {
            if (it > maxVisibleBarCount) maxVisibleBarCount else it
        }

        val barWidthPx = if (boxSize.width > 0) {
            (boxSize.width - startPaddingPx - endPaddingPx) / visibleBarCount - (barPaddingPx - barPaddingPx / visibleBarCount)
        } else {
            0f
        }

        val canvasWidth = with(LocalDensity.current) {
            (barWidthPx * state.items.size + (state.items.size - 1) * barPaddingPx + startPaddingPx + endPaddingPx).toDp()
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
                val textPaint = Paint().apply {
                    color = valueTextColor.toArgb()
                    isAntiAlias = true
                    textSize = valueTextSizePx
                }

                val maxY = state.items.maxOf { it.value }
                val minBarOffsetY = size.height - (size.height - valueTextPaddingPx * 2 - valueTextSizePx - barPaddingPx)
                val maxBarOffsetY = size.height - size.height * 0.02f

                state.items.forEachIndexed { index, bar ->
                    val barOffset = Offset(
                        x = barWidthPx * index + barPaddingPx * index,
                        y = (size.height - bar.value / maxY * size.height).let {
                            if (it < minBarOffsetY) minBarOffsetY else it
                        }.let {
                            if (it > maxBarOffsetY) maxBarOffsetY else it
                        }
                    )

                    val barHeightPx = size.height - barOffset.y

                    drawContext.canvas.nativeCanvas.drawText(
                        bar.formattedValue,
                        barOffset.x + barWidthPx / 2 - textPaint.textSize,
                        barOffset.y - valueTextPaddingPx,
                        textPaint
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