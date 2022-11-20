package breakbadhabits.android.compose.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.material.Card as MaterialCard

@Composable
fun Card(
    modifier: Modifier = Modifier,
    border: BorderStroke? = null,
    elevation: Dp = 3.dp,
    content: @Composable () -> Unit
) {
    MaterialCard(
        modifier = modifier,
        border = border,
        elevation = elevation,
        content = content
    )
}