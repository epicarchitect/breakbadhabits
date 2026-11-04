package epicarchitect.breakbadhabits.uikit

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import epicarchitect.breakbadhabits.resources.Res
import epicarchitect.breakbadhabits.resources.premium_badge
import org.jetbrains.compose.resources.stringResource
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun PremiumShimmerLabel(modifier: Modifier = Modifier) {
    val scheme = MaterialTheme.colorScheme
    val transition = rememberInfiniteTransition(label = "premiumShimmer")
    val progress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 3200,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "premiumShimmerProgress"
    )
    val highlight = lerp(scheme.primary, scheme.onPrimary, 0.90f)
    Box(
        modifier = modifier
            .graphicsLayer {
                compositingStrategy = CompositingStrategy.Offscreen
            }
            .drawWithContent {
                drawContent()
                val bandWidth = size.width * 0.55f
                val startX = -bandWidth + progress * (size.width + bandWidth * 2f)
                drawRect(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color.Transparent,
                            highlight.copy(alpha = 0.90f),
                            Color.Transparent
                        ),
                        startX = startX,
                        endX = startX + bandWidth
                    ),
                    blendMode = BlendMode.SrcAtop
                )
            }
    ) {
        PremiumText()
    }
}

@Composable
private fun PremiumText(modifier: Modifier = Modifier) {
    Text(
        modifier = modifier,
        text = stringResource(Res.string.premium_badge).uppercase(),
        color = MaterialTheme.colorScheme.primary,
        fontSize = 10.sp,
        lineHeight = 12.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 2.sp,
        maxLines = 1
    )
}

@Preview(widthDp = 160, heightDp = 80, name = "PremiumShimmerLabel")
@Composable
private fun PremiumShimmerLabelPreview() {
    PreviewTheme {
        PremiumShimmerLabel(
            modifier = Modifier.padding(16.dp)
        )
    }
}
