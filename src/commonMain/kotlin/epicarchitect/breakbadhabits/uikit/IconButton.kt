package epicarchitect.breakbadhabits.uikit

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.IconButton as MaterialIconButton

@Composable
fun IconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable () -> Unit
) {
    MaterialIconButton(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
        content = content
    )
}

@Composable
fun IconButton(
    onClick: () -> Unit,
    icon: epicarchitect.breakbadhabits.resources.icons.Icon,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    IconButton(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
        content = {
            Icon(icon)
        }
    )
}