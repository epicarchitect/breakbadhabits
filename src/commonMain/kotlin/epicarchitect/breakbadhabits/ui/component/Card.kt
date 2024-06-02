package epicarchitect.breakbadhabits.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import epicarchitect.breakbadhabits.ui.component.theme.AppTheme
import androidx.compose.material3.Card as MaterialCard

@Composable
fun Card(
    modifier: Modifier = Modifier,
    border: BorderStroke? = null,
    elevation: Dp = 1.dp,
    backgroundColor: Color = AppTheme.colorScheme.surface,
    contentColor: Color = contentColorFor(backgroundColor),
    content: @Composable ColumnScope.() -> Unit
) {
    MaterialCard(
        modifier = modifier,
        border = border,
        elevation = CardDefaults.cardElevation(elevation),
        content = content,
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor,
            contentColor = contentColor
        )
    )
}