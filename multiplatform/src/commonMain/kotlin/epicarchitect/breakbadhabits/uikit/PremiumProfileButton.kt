package epicarchitect.breakbadhabits.uikit

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun PremiumProfileButton(
    isPremium: Boolean,
    contentDescription: String,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier.size(48.dp)
    ) {
        Box(
            modifier = Modifier.size(36.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = contentDescription,
                modifier = Modifier.size(22.dp),
                tint = MaterialTheme.colorScheme.onBackground
            )
            if (isPremium) {
                PremiumCrown(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .offset(y = 5.dp)
                )
            }
        }
    }
}

private val CrownGold = Color(0xFFFFE8B0)
private val CrownGoldMid = Color(0xFFD8B36A)
private val CrownGoldDark = Color(0xFFA76C2F)
private val CrownShadow = Color(0x66000000)

@Composable
private fun PremiumCrown(modifier: Modifier = Modifier) {
    Canvas(
        modifier = modifier.size(
            width = 13.5.dp,
            height = 11.dp
        )
    ) {
        val crown = Path().apply {
            moveTo(3f, 8f)
            lineTo(7.5f, 11f)
            lineTo(12f, 5.2f)
            lineTo(16.5f, 11f)
            lineTo(21f, 8f)
            lineTo(19f, 17.5f)
            lineTo(5f, 17.5f)
            close()
        }
        scale(
            scaleX = size.width / 24f,
            scaleY = size.height / 24f,
            pivot = Offset.Zero
        ) {
            translate(top = 0.8f) {
                drawPath(crown, CrownShadow)
            }
            drawPath(
                path = crown,
                brush = Brush.linearGradient(
                    colors = listOf(CrownGold, CrownGoldMid, CrownGoldDark),
                    start = Offset(0f, 0f),
                    end = Offset(24f, 24f)
                )
            )
        }
    }
}

@Preview(widthDp = 160, heightDp = 100, name = "PremiumProfileButton")
@Composable
private fun PremiumProfileButtonPreview() {
    PreviewTheme {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PremiumProfileButton(
                isPremium = false,
                contentDescription = "Профиль",
                onClick = {}
            )
            PremiumProfileButton(
                isPremium = true,
                contentDescription = "Профиль",
                onClick = {}
            )
        }
    }
}
