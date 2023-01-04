package breakbadhabits.ui.kit

import androidx.compose.foundation.BorderStroke
import androidx.compose.material.MaterialTheme
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.material.Card as MaterialCard

@Composable
fun Card(
    modifier: Modifier = Modifier,
    border: BorderStroke? = null,
    elevation: Dp = 3.dp,
    backgroundColor: Color = MaterialTheme.colors.surface,
    contentColor: Color = contentColorFor(backgroundColor),
    content: @Composable () -> Unit
) {
    MaterialCard(
        modifier = modifier,
        border = border,
        elevation = elevation,
        content = content,
        backgroundColor = backgroundColor,
        contentColor = contentColor
    )
}