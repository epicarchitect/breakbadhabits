package breakbadhabits.foundation.uikit

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import breakbadhabits.foundation.uikit.theme.AppTheme
import androidx.compose.material3.Card as MaterialCard

@Composable
fun Card(
    modifier: Modifier = Modifier,
    border: BorderStroke? = null,
    elevation: Dp = 1.dp,
    backgroundColor: Color = AppTheme.colorScheme.surface,
    contentColor: Color = contentColorFor(backgroundColor),
    content: @Composable BoxScope.() -> Unit
) {
    MaterialCard(
        modifier = modifier,
        border = border,
        elevation = CardDefaults.cardElevation(elevation),
        content = {
            Box {
                if (AppTheme.colorScheme.isDark) {
                    Backlight()
                }
                Box(
                    content = content
                )
            }
        },
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor,
            contentColor = contentColor
        ),
    )
}

@Composable
fun BoxScope.Backlight() {
    var brushBoxSize by remember { mutableStateOf(IntSize.Zero) }
    val brushEndOffset = remember(brushBoxSize) {
        Offset(brushBoxSize.width / 2f, brushBoxSize.width / 2f)
    }
    Box(
        modifier = Modifier
            .matchParentSize()
            .onSizeChanged { brushBoxSize = it }
            .background(
                Brush.linearGradient(
                    listOf(
                        AppTheme.colorScheme.primary.copy(alpha = 0.2f),
                        Color.Transparent,
                    ),
                    end = brushEndOffset
                ),
            )
    )
}