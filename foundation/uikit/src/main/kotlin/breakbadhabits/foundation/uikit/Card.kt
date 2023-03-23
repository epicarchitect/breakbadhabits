package breakbadhabits.foundation.uikit

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
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
            Box(content = content)
        },
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor,
            contentColor = contentColor
        ),
    )
}